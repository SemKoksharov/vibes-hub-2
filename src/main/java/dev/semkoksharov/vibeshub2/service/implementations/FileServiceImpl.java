package dev.semkoksharov.vibeshub2.service.implementations;

import dev.semkoksharov.vibeshub2.exceptions.FilesNotUploadedException;
import dev.semkoksharov.vibeshub2.exceptions.MinIOServiceException;
import dev.semkoksharov.vibeshub2.interfaces.Uploadable;
import dev.semkoksharov.vibeshub2.model.Album;
import dev.semkoksharov.vibeshub2.model.Artist;
import dev.semkoksharov.vibeshub2.model.Song;
import dev.semkoksharov.vibeshub2.model.UserEntity;
import dev.semkoksharov.vibeshub2.model.enums.FileType;
import dev.semkoksharov.vibeshub2.repository.AlbumRepo;
import dev.semkoksharov.vibeshub2.repository.SongRepo;
import dev.semkoksharov.vibeshub2.repository.UserRepo;
import org.apache.commons.io.FilenameUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl {

    private final MinIOServiceImpl minIOService;
    private final SongRepo songRepo;
    private final AlbumRepo albumRepo;
    private final UserRepo userRepo;

    @Value("${minio.musicBucket}")
    private String musicBucket;
    @Value("${minio.profileDataBucket}")
    private String profileDataBucket;

    public static final Logger LOGGER = Logger.getLogger(SongServiceImpl.class);

    @Autowired
    public FileServiceImpl(MinIOServiceImpl minIOService, SongRepo songRepo, AlbumRepo albumRepo, UserRepo userRepo) {
        this.minIOService = minIOService;
        this.songRepo = songRepo;
        this.albumRepo = albumRepo;
        this.userRepo = userRepo;
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

        List<String> destinationFolderNames = new ArrayList<>();
        String bucketName = determineBucket(fileType);

        for (int i = 0; i < files.size(); i++) {
            Uploadable entity = entities.get(i);

            String destinationFolderName = determineDestinationFolder(entity, fileType);
            destinationFolderNames.add(destinationFolderName);
        }

        Map<String, String> uploadResultMap = this.uploadFiles(files, destinationFolderNames, bucketName);

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

    public String getUrl(String bucketName, String minioPath, boolean isTemporary, boolean isShort){
        return minIOService.getUrl(bucketName, minioPath, isTemporary, isShort);
    }

    public String getTempUrl(String minioPath, String bucketName, boolean isShort) {
        return minIOService.getUrl(bucketName, minioPath, true, isShort);
    }

    public String getShortUrl(String minioPath, String bucketName) {
        return minIOService.getUrl(bucketName, minioPath, false, true);
    }

    public String getDirectUrl(String minioPath, String bucketName) {
        return minIOService.getUrl(bucketName, minioPath, false, false);
    }

    private String determineBucket(FileType fileType) {
        return fileType == FileType.PROFILE_PICTURE ? profileDataBucket : musicBucket;
    }

    private String determineDestinationFolder(Uploadable entity, FileType fileType) {
        return switch (fileType) {
            case AUDIO -> {
                Song song = (Song) entity;
                Album album = song.getAlbum();
                String artistName = removeSpaces(album.getArtist().getArtistName());
                yield album.getYear() + "_" + artistName + "_" + album.getTitle();
            }
            case ALBUM_COVER -> {
                Album album = (Album) entity;
                String artistName = removeSpaces(album.getArtist().getArtistName());
                yield album.getYear() + "_" + artistName + "_" + album.getTitle();
            }
            case PROFILE_PICTURE -> {
                UserEntity user = (UserEntity) entity;
                yield "user" + "[" + user.getId() + "]" + user.getUsername() + "_profileData";
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


    private String removeSpaces(String input) {
        return input.replace(" ", "_");
    }
}
