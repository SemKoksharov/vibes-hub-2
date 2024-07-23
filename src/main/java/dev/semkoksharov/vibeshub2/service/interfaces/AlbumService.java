package dev.semkoksharov.vibeshub2.service.interfaces;

import dev.semkoksharov.vibeshub2.dto.album.AlbumDTO;
import dev.semkoksharov.vibeshub2.dto.album.AlbumResponseDTO;
import dev.semkoksharov.vibeshub2.model.Artist;
import dev.semkoksharov.vibeshub2.model.Song;

import java.util.List;

public interface AlbumService {
    AlbumResponseDTO createAlbum(AlbumDTO albumDTO);

    AlbumResponseDTO getAlbumById(Long id);

    List<AlbumResponseDTO> getAllAlbums();

    void deleteAlbumById(Long id);

    void deleteAllAlbums();

    AlbumResponseDTO updateAlbum(Long id, AlbumDTO albumDTO);

    void addArtistToAlbum(Long albumId, Artist artist);

    void removeArtistFromAlbum(Long albumId, Artist artist);

    void addSongToAlbum(Long albumId, Song song);

    void removeSongFromAlbum(Long albumId, Song song);
}
