package dev.semkoksharov.vibeshub2.service.implementations;

import dev.semkoksharov.vibeshub2.exceptions.FilesNotUploadedException;
import dev.semkoksharov.vibeshub2.exceptions.MinIOServiceException;
import dev.semkoksharov.vibeshub2.interfaces.Uploadable;
import dev.semkoksharov.vibeshub2.model.Album;
import dev.semkoksharov.vibeshub2.model.Artist;
import dev.semkoksharov.vibeshub2.model.Song;
import dev.semkoksharov.vibeshub2.model.UserEntity;
import dev.semkoksharov.vibeshub2.repository.AlbumRepo;
import dev.semkoksharov.vibeshub2.repository.SongRepo;
import dev.semkoksharov.vibeshub2.repository.UserRepo;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileService  {

    private final MinIOService minIOService;
    private final SongRepo songRepo;
    private final AlbumRepo albumRepo;
    private final UserRepo userRepo;

    @Value("${minio.musicBucket}")
    private String musicBucket;
    @Value("${minio.profileDataBucket}")
    private String profileDataBucket;

    private static final Set<String> AUDIO_EXTENSIONS = Set.of("mp3", "wav", "flac", "m4a");
    private static final Set<String> IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif");

    @Autowired
    public FileService(MinIOService minIOService, SongRepo songRepo, AlbumRepo albumRepo, UserRepo userRepo) {
        this.minIOService = minIOService;
        this.songRepo = songRepo;
        this.albumRepo = albumRepo;
        this.userRepo = userRepo;
    }

    public enum FileType {
        AUDIO, ALBUM_COVER, PROFILE_PICTURE
    }

    public Map<String, String> uploadFile(MultipartFile file, Long id, FileType fileType) {
        return multiUploadFiles(List.of(file), List.of(id), fileType);
    }

    public Map<String, String> deleteFile(Long id, FileType fileType) {
        return multiDeleteFiles(List.of(id), fileType);
    }

    public Map<String, String> multiUploadFiles(List<MultipartFile> files, List<Long> ids, FileType fileType) {
        if (files.size() != ids.size()) {
            throw new FilesNotUploadedException(
                    "[Upload failed] The number of file(s) does not correspond to the number of identifiers! Files cannot be uploaded."
            );
        }

        Map<String, String> errors = new HashMap<>();
        List<String> destinationFolderNames = new ArrayList<>();
        String bucketName = determineBucket(fileType);

        for (int i = 0; i < files.size(); i++) {
            Long id = ids.get(i);
            MultipartFile file = files.get(i);
            String originalFilename = file.getOriginalFilename();
            String extension = Objects.requireNonNull(FilenameUtils.getExtension(originalFilename)).toLowerCase();

            if (id < 0) {
                errors.put(originalFilename, "[Upload error] current value of id < 0 (" + id + ")");
                continue;
            }

            if (!isValidExtension(extension, fileType)) {
                errors.put(originalFilename, "[Upload error] Unsupported format");
                continue;
            }

            try {
                String destinationFolderName = determineDestinationFolder(id, fileType);
                destinationFolderNames.add(destinationFolderName);
            } catch (Exception e) {
                errors.put(originalFilename, e.getMessage());
            }
        }

        Map<String, String> uploadResultMap = uploadFiles(files, destinationFolderNames, bucketName);
        uploadResultMap.putAll(errors);
        return uploadResultMap;
    }

    public Map<String, String> multiDeleteFiles(List<Long> ids, FileType fileType) {
        Map<String, String> errors = new HashMap<>();
        List<String> objectNames = new ArrayList<>();
        String bucketName = determineBucket(fileType);

        for (Long id : ids) {
            if (id < 0) {
                errors.put(id.toString(), "[Delete error] current value of id < 0 (" + id + ")");
                continue;
            }

            try {
                String objectPath = determineObjectPath(id, fileType);
                objectNames.add(objectPath);
            } catch (Exception e) {
                errors.put(id.toString(), e.getMessage());
            }
        }

        Map<String, String> resultMap = deleteFiles(bucketName, objectNames);
        resultMap.putAll(errors);
        return resultMap;
    }

    private String determineBucket(FileType fileType) {
        return fileType == FileType.PROFILE_PICTURE ? profileDataBucket : musicBucket;
    }

    private boolean isValidExtension(String extension, FileType fileType) {
        return switch (fileType) {
            case AUDIO -> AUDIO_EXTENSIONS.contains(extension);
            case ALBUM_COVER, PROFILE_PICTURE -> IMAGE_EXTENSIONS.contains(extension);
        };
    }

    private String determineDestinationFolder(Long id, FileType fileType) {
        return switch (fileType) {
            case AUDIO -> {
                Song song = songRepo.findById(id).orElseThrow(
                        () -> new IllegalArgumentException("[Upload error] Song with id " + id + " is not found in the database")
                );
                Album album = song.getAlbum();
                String artistNames = album.getArtists().stream().map(Artist::getArtistName).collect(Collectors.joining("-","[", "]"));
                yield album.getYear() + "_" +artistNames + "_" + album.getTitle();
            }
            case ALBUM_COVER -> {
                Album album = albumRepo.findById(id).orElseThrow(
                        () -> new IllegalArgumentException("[Upload error] Album with id " + id + " is not found in the database")
                );
                String artistNames = album.getArtists().stream().map(Artist::getArtistName).collect(Collectors.joining("-","[", "]"));

                yield album.getYear() + "_" + artistNames + "_" + album.getTitle();
            }
            case PROFILE_PICTURE -> {
                userRepo.findById(id).orElseThrow(
                        () -> new IllegalArgumentException("[Upload error] User with id " + id + " is not found in the database")
                );
                yield "user_" + id + "_profileData";
            }
        };
    }

    private String determineObjectPath(Long id, FileType fileType) {
        return switch (fileType) {
            case AUDIO -> {
                Song song = songRepo.findById(id).orElseThrow(
                        () -> new IllegalArgumentException("[Delete error] Song with id " + id + " is not found in the database")
                );
                yield  song.getMinioPath();
            }
            case ALBUM_COVER -> {
                Album album = albumRepo.findById(id).orElseThrow(
                        () -> new IllegalArgumentException("[Delete error] Album with id " + id + " is not found in the database")
                );
                yield  album.getMinioPath();
            }
            case PROFILE_PICTURE -> {
                UserEntity user = userRepo.findById(id).orElseThrow(
                        () -> new IllegalArgumentException("[Delete error] User with id " + id + " is not found in the database")
                );
                yield  user.getMinioPath();
            }
        };
    }

    private Map<String, String> uploadFiles(List<MultipartFile> files, List<String> destinationFolderNames, String bucketName) {
        Map<String, String> resultMap = new HashMap<>();
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            String originalFilename = file.getOriginalFilename();
            String newFileName = originalFilename + "_" + UUID.randomUUID();

            try {
                Map<String, String> uploadResult = minIOService.uploadFile(file, newFileName, destinationFolderNames.get(i), bucketName);
                resultMap.put(originalFilename, uploadResult.get(originalFilename));
            } catch (MinIOServiceException e) {
                resultMap.put(originalFilename, e.getMessage());
            }
        }
        return resultMap;
    }

    private Map<String, String> deleteFiles(String bucketName, List<String> objectNames) {
        Map<String, String> delResult = new HashMap<>();
        for (String name : objectNames) {
            try {
                minIOService.deleteFile(bucketName, name);
                delResult.put(name, "File deleted.");
            } catch (MinIOServiceException e) {
                delResult.put(name, e.getMessage());
            }
        }
        return delResult;
    }
}
