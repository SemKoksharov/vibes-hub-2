package dev.semkoksharov.vibeshub2.service.implementations;

import dev.semkoksharov.vibeshub2.dto.album.AlbumSimpleDTO;
import dev.semkoksharov.vibeshub2.dto.genre.GenreSimpleDTO;
import dev.semkoksharov.vibeshub2.dto.song.SongDTO;
import dev.semkoksharov.vibeshub2.dto.song.SongResponseDTO;
import dev.semkoksharov.vibeshub2.exceptions.EntityUpdaterException;
import dev.semkoksharov.vibeshub2.exceptions.FilesNotUploadedException;
import dev.semkoksharov.vibeshub2.interfaces.Uploadable;
import dev.semkoksharov.vibeshub2.model.Album;
import dev.semkoksharov.vibeshub2.model.Genre;
import dev.semkoksharov.vibeshub2.model.Song;
import dev.semkoksharov.vibeshub2.repository.AlbumRepo;
import dev.semkoksharov.vibeshub2.repository.GenreRepo;
import dev.semkoksharov.vibeshub2.repository.SongRepo;
import dev.semkoksharov.vibeshub2.service.interfaces.SongServiceInt;
import dev.semkoksharov.vibeshub2.utils.EntityUpdater;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SongService implements SongServiceInt {

    private final SongRepo songRepo;
    private final AlbumRepo albumRepo;
    private final GenreRepo genreRepo;
    private final ModelMapper modelMapper;
    private final EntityUpdater entityUpdater;
    private final FileService fileService;

    @Autowired
    public SongService(SongRepo songRepo, AlbumRepo albumRepo, GenreRepo genreRepo, ModelMapper modelMapper, EntityUpdater entityUpdater, FileService fileService) {
        this.songRepo = songRepo;
        this.albumRepo = albumRepo;
        this.genreRepo = genreRepo;
        this.modelMapper = modelMapper;
        this.entityUpdater = entityUpdater;
        this.fileService = fileService;
    }

    @Override
    public SongResponseDTO createSong(SongDTO songDTO) {
        Song song = modelMapper.map(songDTO, Song.class);

        Optional<Album> albumOptional = albumRepo.findById(songDTO.getAlbumId());
        if (albumOptional.isEmpty()) {
            throw new IllegalArgumentException("[Create error] Album with id " + songDTO.getAlbumId() + " not found");
        }
        Album album = albumOptional.get();

        Optional<Genre> genreOptional = genreRepo.findById(songDTO.getGenreId());
        if (genreOptional.isEmpty()) {
            throw new IllegalArgumentException("[Create error] Genre with id " + songDTO.getGenreId() + " not found");
        }
        Genre genre = genreOptional.get();

        song.setAlbum(album);
        song.setGenre(genre);
        album.addSong(song);

        Song savedSong = songRepo.save(song);
        return mapToResponseDTO(savedSong, album, genre);
    }

    @Override
    public SongResponseDTO getSongById(Long id) {
        Optional<Song> songOptional = songRepo.findById(id);
        if (songOptional.isEmpty()) {
            throw new IllegalArgumentException("[Get error] Song with id " + id + " is not found");
        }

        Song song = songOptional.get();
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
        Map<String, String> uploadResult = new HashMap<>();
        List<Uploadable> entities = new ArrayList<>();
        List<String> filenames = files.stream().map(MultipartFile::getOriginalFilename).toList();

        if (files.size() != ids.size()) {
            throw new FilesNotUploadedException(
                    "[Upload error] The number of file(s) does not correspond to the number of identifiers! Files cannot be uploaded."
            );
        }

        for (int i = 0; i < files.size(); i++) {
            Long songId = ids.get(i);
            String filename = filenames.get(i);
            Optional<Song> songOptional = songRepo.findById(songId);

            if (songOptional.isEmpty()) {
                uploadResult.put(filename, "[Upload error] Song entity with id " + songId + " is not found in the database");
            } else {
                entities.add(songOptional.get());
            }
        }

        Map<String, String> uploadFilesResult = fileService.multiUploadFiles(files, entities, FileService.FileType.AUDIO);
        uploadResult.putAll(uploadFilesResult);

        for (int i = 0; i < files.size(); i++) {
            String filename = filenames.get(i);
            Song song = entities.size() > i ? (Song) entities.get(i) : null;

            if (song != null && uploadFilesResult.containsKey(filename) && !uploadFilesResult.get(filename).startsWith("[Upload error]")) {
                String minioPath = uploadFilesResult.get(filename);
                song.setMinioPath(minioPath);
                songRepo.save(song);
            }
        }

        return uploadResult;
    }


    private SongResponseDTO mapToResponseDTO(Song song, Album album, Genre genre) {
        SongResponseDTO responseDTO = modelMapper.map(song, SongResponseDTO.class);
        responseDTO.setAlbum(modelMapper.map(album, AlbumSimpleDTO.class));
        responseDTO.setGenre(modelMapper.map(genre, GenreSimpleDTO.class));
        return responseDTO;
    }


}
