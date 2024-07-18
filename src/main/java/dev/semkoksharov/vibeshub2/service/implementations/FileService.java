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
public class FileService {

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

    public Map<String, String> uploadFile(MultipartFile file, Uploadable entity, FileType fileType) {

        String originalFilename = file.getOriginalFilename();
        Map<String, String> uploadResult = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        try {
           uploadResult.putAll(multiUploadFiles(List.of(file), List.of(entity), fileType));
        } catch (FilesNotUploadedException e) {
          uploadResult.put(originalFilename, "[Upload error]" + e.getMessage());
        }

        return uploadResult;
    }

    public Map<String, String> deleteFile(Uploadable entity, FileType fileType) {
        return multiDeleteFiles(List.of(entity), fileType);
    }

    public Map<String, String> multiUploadFiles(List<MultipartFile> files, List<Uploadable> entities, FileType fileType) {

        Map<String, String> errors = new HashMap<>();
        List<String> destinationFolderNames = new ArrayList<>();
        String bucketName = determineBucket(fileType);

        for (int i = 0; i < files.size(); i++) {
            Uploadable entity = entities.get(i);
            MultipartFile file = files.get(i);
            String originalFilename = file.getOriginalFilename();
            String extension = Objects.requireNonNull(FilenameUtils.getExtension(originalFilename)).toLowerCase();

            if (!isValidExtension(extension, fileType)) {
                errors.put(originalFilename, "[Upload error] Unsupported format");
                continue;
            }

            try {
                String destinationFolderName = determineDestinationFolder(entity, fileType);
                destinationFolderNames.add(destinationFolderName);
            } catch (Exception e) {
                errors.put(originalFilename, "[Upload error] " + e.getMessage());
            }
        }

        Map<String, String> uploadResultMap = uploadFiles(files, destinationFolderNames, bucketName);
        uploadResultMap.putAll(errors);
        return uploadResultMap;
    }

    public Map<String, String> multiDeleteFiles(List<Uploadable> entities, FileType fileType) {
        Map<String, String> errors = new HashMap<>();
        List<String> objectNames = new ArrayList<>();
        String bucketName = determineBucket(fileType);

        for (Uploadable entity : entities) {
            try {
                String objectPath = determineObjectPath(entity, fileType);
                objectNames.add(objectPath);
            } catch (Exception e) {
                errors.put(entity.getId().toString(), "[Delete error] " + e.getMessage());
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

    private String determineDestinationFolder(Uploadable entity, FileType fileType) {
        return switch (fileType) {
            case AUDIO -> {
                Song song = (Song) entity;
                Album album = song.getAlbum();
                String artistNames = album.getArtists().stream().map(Artist::getArtistName).collect(Collectors.joining("-", "[", "]"));
                yield album.getYear() + "_" + artistNames + "_" + album.getTitle();
            }
            case ALBUM_COVER -> {
                Album album = (Album) entity;
                String artistNames = album.getArtists().stream().map(Artist::getArtistName).collect(Collectors.joining("-"));
                yield album.getYear() + "_" + artistNames + "_" + album.getTitle();
            }
            case PROFILE_PICTURE -> {
                UserEntity user = (UserEntity) entity;
                yield "user_" + user.getId() + "_profileData";
            }
        };
    }

    private String determineObjectPath(Uploadable entity, FileType fileType) {
        return switch (fileType) {
            case AUDIO -> {
                Song song = (Song) entity;
                yield song.getMinioPath();
            }
            case ALBUM_COVER -> {
                Album album = (Album) entity;
                yield album.getMinioPath();
            }
            case PROFILE_PICTURE -> {
                UserEntity user = (UserEntity) entity;
                yield user.getMinioPath();
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
                resultMap.put(originalFilename, "[Upload error] " + e.getMessage());
            }
        }
        return resultMap;
    }

    private Map<String, String> deleteFiles(String bucketName, List<String> objectNames) {
        Map<String, String> delResult = new HashMap<>();
        for (String name : objectNames) {
            try {
                minIOService.deleteFile(bucketName, name);
                delResult.put(name, "[Success] file deleted");
            } catch (MinIOServiceException e) {
                delResult.put(name, "[Upload error] " + e.getMessage());
            }
        }
        return delResult;
    }
}
