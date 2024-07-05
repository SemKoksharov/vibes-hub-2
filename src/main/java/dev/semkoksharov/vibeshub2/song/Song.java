package dev.semkoksharov.vibeshub2.song;

import dev.semkoksharov.vibeshub2.genre.Genre;
import jakarta.persistence.*;

public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private Genre genre;
    private  int duration;
    private String title;
    private String url;
    private String minioPath;

    public Song() {
    }

    public Song(Long id, Genre genre, int duration, String title, String url, String minioPath) {
        this.id = id;
        this.genre = genre;
        this.duration = duration;
        this.title = title;
        this.url = url;
        this.minioPath = minioPath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
