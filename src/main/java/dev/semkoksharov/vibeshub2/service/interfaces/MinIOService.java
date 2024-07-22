package dev.semkoksharov.vibeshub2.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface MinIOService {

    Map<String, String> uploadFile(MultipartFile file, String newFileName, String destinationFolderName, String bucketName);

    String getUrl(String bucketName, String minioObjectPath, boolean isTemporary, boolean isShort);

    boolean deleteFile(String bucketName, String filepath);

}
