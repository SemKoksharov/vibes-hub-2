package dev.semkoksharov.vibeshub2.genre;

import dev.semkoksharov.vibeshub2.song.Song;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.util.HashSet;
import java.util.Set;

public class Genre {

    private Long id;
    private String title;
    private String description;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Song> songs = new HashSet<>();


    private Genre() {
    }

    public Genre(Long id, String title, String description, Set<Song> songs) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.songs = songs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Song> getSongs() {
        return songs;
    }

    public void setSongs(Set<Song> songs) {
        this.songs = songs;
    }
}
