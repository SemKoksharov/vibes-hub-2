package dev.semkoksharov.vibeshub2.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "albums")
public class Album extends BaseEntity {

    private String title;
    private String coverPhotoUrl;
    private String minioCoverPath;
    private int year;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Artist> artists = new HashSet<>();

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Song> songs = new HashSet<>();

    public Album() {}

    public Album(String title, String coverPhotoUrl, String minioCoverPath, int year, Set<Artist> artists, Set<Song> songs) {
        this.title = title;
        this.coverPhotoUrl = coverPhotoUrl;
        this.minioCoverPath = minioCoverPath;
        this.year = year;
        this.artists = artists;
        this.songs = songs;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverPhotoUrl() {
        return coverPhotoUrl;
    }

    public void setCoverPhotoUrl(String coverPhotoUrl) {
        this.coverPhotoUrl = coverPhotoUrl;
    }

    public String getMinioCoverPath() {
        return minioCoverPath;
    }

    public void setMinioCoverPath(String minioCoverPath) {
        this.minioCoverPath = minioCoverPath;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Set<Artist> getArtists() {
        return artists;
    }

    public void setArtists(Set<Artist> artists) {
        this.artists = artists;
    }

    public Set<Song> getSongs() {
        return songs;
    }

    public void setSongs(Set<Song> songs) {
        this.songs = songs;
    }

    public void addSong(Song song) {
        this.songs.add(song);
        song.setAlbum(this);
    }

    public void removeSong(Song song) {
        this.songs.remove(song);
        song.setAlbum(null);
    }

    public void addArtist(Artist artist) {
        this.artists.add(artist);
    }

    public void removeArtist(Artist artist) {
        this.artists.remove(artist);
    }
}
