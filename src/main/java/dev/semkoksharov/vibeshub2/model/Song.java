package dev.semkoksharov.vibeshub2.model;

import jakarta.persistence.*;

@Entity
@Table(name = "songs")
public class Song extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    private int duration;
    private String title;
    private String url;
    private String minioPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

    public Song() {}

    public Song(Genre genre, int duration, String title, String url, String minioPath, Album album) {
        this.genre = genre;
        this.duration = duration;
        this.title = title;
        this.url = url;
        this.minioPath = minioPath;
        this.album = album;
    }

    // Getters and Setters
    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMinioPath() {
        return minioPath;
    }

    public void setMinioPath(String minioPath) {
        this.minioPath = minioPath;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
