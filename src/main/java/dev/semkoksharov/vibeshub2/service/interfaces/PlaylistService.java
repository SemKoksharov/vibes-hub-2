package dev.semkoksharov.vibeshub2.service.interfaces;

import dev.semkoksharov.vibeshub2.dto.playlist.PlaylistRequestDTO;
import dev.semkoksharov.vibeshub2.dto.playlist.PlaylistResponseDTO;
import dev.semkoksharov.vibeshub2.model.Playlist;

import java.util.List;

public interface PlaylistService {

    PlaylistResponseDTO getPlaylistById(Long id);

    List<PlaylistResponseDTO> getAllPlaylists();

    PlaylistResponseDTO createPlaylist(PlaylistRequestDTO playlistRequestDTO);

    PlaylistResponseDTO updatePlaylist(PlaylistRequestDTO playlistRequestDTO);

    void deletePlaylistById(Long id);

    void deleteAllPlaylists();
}
