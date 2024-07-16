package dev.semkoksharov.vibeshub2.service.implementations;

import dev.semkoksharov.vibeshub2.exceptions.MinIOServiceException;
import dev.semkoksharov.vibeshub2.exceptions.UrlShorterException;
import dev.semkoksharov.vibeshub2.service.interfaces.MinIOServiceInt;
import dev.semkoksharov.vibeshub2.utils.TinyURL;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.*;
import io.minio.http.Method;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class MinIOService implements MinIOServiceInt {

    private final MinioClient minioClient;
    private final TinyURL tinyURL;

    @Autowired
    public MinIOService(MinioClient minioClient, TinyURL tinyURL) {
        this.minioClient = minioClient;
        this.tinyURL = tinyURL;
    }


    @Override
    public Map<String, String> uploadFile(MultipartFile file, String newFileName, String destinationFolderName, String bucketName) {

        String originalFilename = file.getOriginalFilename();
        String extension = Objects.requireNonNull(FilenameUtils.getExtension(originalFilename)).toLowerCase();
        String minioObjectPath = destinationFolderName + "/" + newFileName + "." + extension;
        String contentType = file.getContentType();

        InputStream fileInputStream = null;
        long maxPartSize = 10485760L;

        Map<String, String> newObject = new HashMap<>();
        try {
            fileInputStream = file.getInputStream();

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(minioObjectPath)
                    .stream(fileInputStream, -1, maxPartSize)
                    .contentType(contentType)
                    .build());

            return new HashMap<>(Map.of(originalFilename, minioObjectPath));

        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException |
                 IOException e) {

            throw new MinIOServiceException("[File upload error] " + e.getMessage()+ " Caused by: " + e.getClass().getCanonicalName());

        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // if isTemporary = true URL expires in 10 hours
    // if isShort = true you will get a short link using https://tinyurl.com/
    private String getTempUrl(String bucketName, String minioObjectPath, boolean isTemporary, boolean isShort) {
        String objectUrl;
        try {
            if (isTemporary) {
                objectUrl = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket(bucketName)
                                .object(minioObjectPath)
                                .expiry(36000) // 10 hours = 36000 seconds !
                                .build()
                );
            } else {
                objectUrl = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket(bucketName)
                                .object(minioObjectPath)
                                .build()
                );
            }
        } catch (ErrorResponseException | ServerException | XmlParserException | NoSuchAlgorithmException |
                 IOException | InvalidResponseException | InvalidKeyException | InternalException |
                 InsufficientDataException e) {
            throw new MinIOServiceException("[Error getting URL] Caused by: " +
                    e.getClass().getSimpleName() + " Ex. Message:" + e.getMessage());
        }

        try {
            return isShort ? tinyURL.shortURL(objectUrl) : objectUrl;
        } catch (IOException e) {
            throw new UrlShorterException(
                    "[TinyURL service error] Try again with isShort = false, Caused by: " +
                            e.getClass().getSimpleName() + "Ex. Message: " + e.getMessage()
            );
        }
    }

    @Override
    public boolean deleteFile(String bucketName, String objectName) {
        boolean deleted = false;

        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
            deleted = true;
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {

            throw new MinIOServiceException("[File delete error] Caused by: " +
                    e.getClass().getSimpleName() + " Ex. Message:" + e.getMessage());
        }
        return deleted;
    }

}
