package dev.semkoksharov.vibeshub2.service.interfaces;

import dev.semkoksharov.vibeshub2.dto.song.SongDTO;
import dev.semkoksharov.vibeshub2.dto.song.SongResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface SongServiceInt {
    SongResponseDTO createSong(SongDTO songDTO);

    SongResponseDTO getSongById(Long id);

    List<SongResponseDTO> getAllSongs();

    void deleteSongById(Long id);

    SongResponseDTO updateSong(Long id, SongDTO songDTO);

    Map<String, String> uploadAudio(List<MultipartFile> files, List<Long> ids);
}

