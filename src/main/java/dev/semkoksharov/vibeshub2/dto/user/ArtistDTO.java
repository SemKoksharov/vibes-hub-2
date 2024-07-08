package dev.semkoksharov.vibeshub2.dto.user;
import dev.semkoksharov.vibeshub2.dto.album.AlbumSimpleDTO;
import dev.semkoksharov.vibeshub2.dto.SongSimpleDTO;

import java.util.Set;

public class ArtistDTO {
    private Long id;
    private String description;
    private String artistName;
    private Set<AlbumSimpleDTO> albums;
    private Set<SongSimpleDTO> songs;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Set<AlbumSimpleDTO> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<AlbumSimpleDTO> albums) {
        this.albums = albums;
    }

    public Set<SongSimpleDTO> getSongs() {
        return songs;
    }

    public void setSongs(Set<SongSimpleDTO> songs) {
        this.songs = songs;
    }
}
