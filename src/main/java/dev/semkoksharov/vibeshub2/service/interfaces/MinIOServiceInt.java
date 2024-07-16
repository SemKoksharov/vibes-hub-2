package dev.semkoksharov.vibeshub2.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface MinIOServiceInt {

    Map<String, String> uploadFile(MultipartFile file, String newFileName, String destinationFolderName, String bucketName);

    boolean deleteFile(String bucketName, String filepath);

}
