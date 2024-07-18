package dev.semkoksharov.vibeshub2.interfaces;


import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UploadableService {

    Map<String, String> blobSingleUpload(MultipartFile file, Long id);

    Map<String, String> blobMultiUpload(List<MultipartFile> file, List<Long> id);

    Map<String, String> blobSingleRemove(Long id);

    Map<String, String> blobMultiRemove(List<Long> id);
}
