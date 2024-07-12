package dev.semkoksharov.vibeshub2.model;

import dev.semkoksharov.vibeshub2.model.base.RoleDetails;
import jakarta.persistence.*;
import org.checkerframework.checker.units.qual.A;

import java.util.Set;

@Entity
@Table(name = "artist_details")
public class Artist extends RoleDetails {

    private String artistName;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "artist_song",
            joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id"))
    private Set<Song> songs;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "artist_album",
            joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "album_id"))
    private Set<Album> albums;

    public Artist() {
    }

    public Artist(UserEntity user, String description, String artistName, Set<Song> songs, Set<Album> albums) {
        super(user, description);
        this.artistName = artistName;
        this.songs = songs;
        this.albums = albums;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Set<Song> getSongs() {
        return songs;
    }

    public void setSongs(Set<Song> songs) {
        this.songs = songs;
    }

    public Set<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<Album> albums) {
        this.albums = albums;
    }

    public void addAlbum(Album album){
        this.albums.add(album);
    }

    public void removeAlbum(Album album){
        this.albums.remove(album);
    }
}

