package dev.semkoksharov.vibeshub2.users.entity;

import dev.semkoksharov.vibeshub2.album.Album;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Artist {

    @Id
    private Long id;
    @MapsId
    private UserEntity user;
    private String artistName;
    private String description;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Album> albums = new HashSet<>();

    public Artist() {
    }

    public Artist(Long id, UserEntity user, String artistName, String description, Set<Album> albums) {
        this.id = id;
        this.user = user;
        this.artistName = artistName;
        this.description = description;
        this.albums = albums;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<Album> albums) {
        this.albums = albums;
    }
}
