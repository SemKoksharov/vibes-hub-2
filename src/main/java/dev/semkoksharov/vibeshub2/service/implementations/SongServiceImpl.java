package dev.semkoksharov.vibeshub2.service.implementations;

import dev.semkoksharov.vibeshub2.dto.album.AlbumSimpleDTO;
import dev.semkoksharov.vibeshub2.dto.genre.GenreSimpleDTO;
import dev.semkoksharov.vibeshub2.dto.song.SongDTO;
import dev.semkoksharov.vibeshub2.dto.song.SongResponseDTO;
import dev.semkoksharov.vibeshub2.exceptions.EntityUpdaterException;
import dev.semkoksharov.vibeshub2.exceptions.FilesNotUploadedException;
import dev.semkoksharov.vibeshub2.exceptions.UrlShorterException;
import dev.semkoksharov.vibeshub2.interfaces.Uploadable;
import dev.semkoksharov.vibeshub2.model.Album;
import dev.semkoksharov.vibeshub2.model.Genre;
import dev.semkoksharov.vibeshub2.model.Song;
import dev.semkoksharov.vibeshub2.model.enums.FileType;
import dev.semkoksharov.vibeshub2.repository.AlbumRepo;
import dev.semkoksharov.vibeshub2.repository.GenreRepo;
import dev.semkoksharov.vibeshub2.repository.SongRepo;
import dev.semkoksharov.vibeshub2.service.interfaces.SongService;
import dev.semkoksharov.vibeshub2.utils.EntityUpdater;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SongServiceImpl implements SongService {

    private final SongRepo songRepo;
    private final AlbumRepo albumRepo;
    private final GenreRepo genreRepo;
    private final ModelMapper modelMapper;
    private final EntityUpdater entityUpdater;
    private final FileServiceImpl fileService;
    private final MinIOServiceImpl minIOService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SongServiceImpl.class);

    @Value("${minio.musicBucket}")
    private String musicBucket;

    @Autowired
    public SongServiceImpl(SongRepo songRepo, AlbumRepo albumRepo, GenreRepo genreRepo, ModelMapper modelMapper, EntityUpdater entityUpdater, FileServiceImpl fileService, MinIOServiceImpl minIOService) {
        this.songRepo = songRepo;
        this.albumRepo = albumRepo;
        this.genreRepo = genreRepo;
        this.modelMapper = modelMapper;
        this.entityUpdater = entityUpdater;
        this.fileService = fileService;
        this.minIOService = minIOService;
    }

    @Override
    public SongResponseDTO createSong(SongDTO songDTO) {
        Song song = modelMapper.map(songDTO, Song.class);

        Album album = albumRepo.findById(songDTO.getAlbumId()).orElseThrow(() ->
                new IllegalArgumentException("[Create error] Album with id " + songDTO.getAlbumId() + " not found"));

        Genre genre = genreRepo.findById(songDTO.getGenreId()).orElseThrow(() ->
                new IllegalArgumentException("[Create error] Genre with id " + songDTO.getGenreId() + " not found"));

        song.setAlbum(album);
        song.setGenre(genre);
        album.addSong(song);

        Song savedSong = songRepo.save(song);
        return mapToResponseDTO(savedSong, album, genre);
    }

    @Override
    public SongResponseDTO getSongById(Long id) {
        Song song = songRepo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("[Get error] Song with id " + id + " is not found"));

        return mapToResponseDTO(song, song.getAlbum(), song.getGenre());
    }

    @Override
    public List<SongResponseDTO> getAllSongs() {
        List<Song> songs = songRepo.findAll();
        if (songs.isEmpty()) {
            throw new IllegalArgumentException("[Get error] No songs found in the database");
        }
        return songs.stream()
                .map(song -> mapToResponseDTO(song, song.getAlbum(), song.getGenre()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSongById(Long id) {
        if (!songRepo.existsById(id)) {
            throw new IllegalArgumentException("[Delete error] Song with id " + id + " is not found");
        }
        songRepo.deleteById(id);
    }

    @Override
    @Transactional
    public SongResponseDTO updateSong(Long id, SongDTO songDTO) {

        Song toUpdate = songRepo.findById(id).orElseThrow(
                () -> new IllegalArgumentException("[Update error] Song with id " + id + " not found")
        );

        Album currentAlbum = toUpdate.getAlbum();

        if (songDTO.getAlbumId() != null) {
            Album newAlbum = albumRepo.findById(songDTO.getAlbumId()).orElseThrow(
                    () -> new IllegalArgumentException("[Update error] Album with id " + songDTO.getAlbumId() + " not found")
            );

            if (currentAlbum != null && !currentAlbum.equals(newAlbum)) {
                currentAlbum.removeSong(toUpdate);
                albumRepo.saveAndFlush(currentAlbum);

                newAlbum.addSong(toUpdate);
                albumRepo.saveAndFlush(newAlbum);
            }
        }

        if (songDTO.getGenreId() != null) {
            Genre newGenre = genreRepo.findById(songDTO.getGenreId()).orElseThrow(
                    () -> new IllegalArgumentException("[Update error] Genre with id " + songDTO.getGenreId() + " not found")
            );
            toUpdate.setGenre(newGenre);
        }

        try {
            entityUpdater.update(songDTO, toUpdate);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new EntityUpdaterException("[Update error] Caused by: " + e.getClass().getSimpleName() +
                    " Exception message: " + e.getMessage());
        }

        Song updatedSong = songRepo.saveAndFlush(toUpdate);

        return mapToResponseDTO(updatedSong, toUpdate.getAlbum(), toUpdate.getGenre());
    }

    @Override
    public Map<String, String> uploadAudio(List<MultipartFile> files, List<Long> ids) {

        if (files.size() != ids.size()) {
            String errorMessage = "[Upload error] The number of files does not correspond to the number of identifiers! Files cannot be uploaded.";
            LOGGER.error(errorMessage);
            throw new FilesNotUploadedException(errorMessage);
        }

        Map<String, String> finalResult = new HashMap<>();
        List<MultipartFile> validFiles = new ArrayList<>();
        List<Uploadable> validEntities = new ArrayList<>();
        FileType fileType = FileType.AUDIO;

        LOGGER.info("Starting audio upload process");
        LOGGER.debug("Number of files: {}", files.size());
        LOGGER.debug("Number of ids: {}", ids.size());

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            Long songId = ids.get(i);
            String filename = file.getOriginalFilename();
            String extension = Objects.requireNonNull(FilenameUtils.getExtension(filename)).toLowerCase();

            if (extension.isBlank() || ! fileType.validExtensions.contains(extension)) {
                finalResult.put(filename, "[Upload error] Unsupported format. Supported formats: " + fileType.validExtensions.toString());
                continue;
            }

            LOGGER.debug("Processing file: {} with id: {}", filename, songId);

            Optional<Song> songOpt = songRepo.findById(songId);
            if (songOpt.isEmpty()) {
                LOGGER.error("Song entity with id {} is not found in the database! File {} cannot be uploaded", songId, filename);
                finalResult.put(filename, "[Upload error] Song entity with id " + songId + " is not found in the database! File cannot be uploaded");
            } else {
                validFiles.add(file);
                validEntities.add(songOpt.get());
            }
        }

        if (validFiles.isEmpty()) {
            return finalResult;
        }

        Map<String, String> finalResults = fileService.multiUploadFiles(validFiles, validEntities, fileType);

        assignUploadResultsToEntities(validFiles, validEntities, finalResults, finalResult);

        LOGGER.info("Audio upload process completed");
        return finalResult;
    }

    @Override
    public Map<String, String> multiDeleteAudioFromBlobStorage(List<Long> songIDs) {
        Map<String, String> delResult = new HashMap<>();

        for (Long songID : songIDs) {
            try {
                if (deleteAudioFromBlobStorage(songID)) {
                    delResult.put("[Song id] " + songID.toString(), "OK");
                }
            } catch (Exception e) {
                delResult.put("[Song id] " + songID.toString(), "[Delete error]" + e.getMessage());
            }
        }
        return delResult;
    }

    private boolean deleteAudioFromBlobStorage(Long songID) throws Exception {
        Song toDelete = songRepo.findById(songID).orElseThrow(
                () -> new EntityNotFoundException("Song is not found in the database")
        );
        String minioPath = toDelete.getMinioPath();

        if (minioPath == null) {
            //todo change exception below
            throw new Exception("Song data found in database but not in blob storage. It can't be deleted");
        }

        return minIOService.deleteFile(musicBucket, minioPath);
    }

    private void assignUploadResultsToEntities(List<MultipartFile> validFiles, List<Uploadable> validEntities, Map<String, String> uploadFilesResult, Map<String, String> uploadResult) {
        for (int i = 0; i < validFiles.size(); i++) {
            MultipartFile file = validFiles.get(i);
            String filename = file.getOriginalFilename();
            Song song = (Song) validEntities.get(i);

            if (uploadFilesResult.containsKey(filename)) {
                String minioPath = uploadFilesResult.get(filename);

                if (minioPath.startsWith("[Upload error]")) {
                    LOGGER.error("Failed to upload file: '{}'", filename);
                    uploadResult.put(filename, minioPath);
                } else {
                    String directUrl;
                    try {
                        directUrl = fileService.getShortUrl(minioPath, musicBucket);
                    } catch (UrlShorterException e) {
                        directUrl = fileService.getDirectUrl(minioPath, musicBucket);
                        LOGGER.warn("[URL shortening error] Full format URL will be used: {}", directUrl);
                    }
                    song.setMinioPath(minioPath);
                    song.setDirectUrl(directUrl);
                    songRepo.save(song);
                    uploadResult.put(filename, song.getDirectUrl());

                    LOGGER.debug("Assigned minioPath '{}' and directUrl to song with id {}, original file: '{}'", minioPath, song.getId(), filename);
                    LOGGER.info("Successfully uploaded and updated song with id {}, original file: '{}'", song.getId(), filename);
                }
            } else {
                LOGGER.error("Upload result for file '{}' is not found", filename);
                uploadResult.put(filename, "[Upload error] Upload result not found for file " + filename);
            }
        }
    }

    private SongResponseDTO mapToResponseDTO(Song song, Album album, Genre genre) {
        SongResponseDTO responseDTO = modelMapper.map(song, SongResponseDTO.class);
        responseDTO.setAlbum(modelMapper.map(album, AlbumSimpleDTO.class));
        responseDTO.setGenre(modelMapper.map(genre, GenreSimpleDTO.class));
        return responseDTO;
    }
}
