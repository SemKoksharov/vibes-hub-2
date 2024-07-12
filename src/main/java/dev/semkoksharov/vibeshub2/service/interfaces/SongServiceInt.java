package dev.semkoksharov.vibeshub2.service.interfaces;

import dev.semkoksharov.vibeshub2.dto.song.SongDTO;
import dev.semkoksharov.vibeshub2.dto.song.SongResponseDTO;

import java.util.List;

public interface SongServiceInt {
    SongResponseDTO createSong(SongDTO songDTO);

    SongResponseDTO getSongById(Long id);

    List<SongResponseDTO> getAllSongs();

    void deleteSongById(Long id);

    SongResponseDTO updateSong(Long id, SongDTO songDTO);
}

