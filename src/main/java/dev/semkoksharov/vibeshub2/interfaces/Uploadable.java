package dev.semkoksharov.vibeshub2.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface Uploadable {
    Map<String, String> uploadSingleFile(MultipartFile file, Long id);
    Map<String, String> deleteSingleFile(Long id);
    Map<String, String> deleteMultiFile(List<Long> id);
    Map<String, String> uploadMultiFile(List<MultipartFile> file, List<Long> id);
}
