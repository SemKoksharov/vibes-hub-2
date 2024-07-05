package dev.semkoksharov.vibeshub2.album;

import dev.semkoksharov.vibeshub2.song.Song;

import java.util.List;
import java.util.Set;

public class Album {

    private String title;
    private String coverPhotoUrl;
    private int year;
    private Set<Song> songs;

    public Album(String title, String coverPhotoUrl, int year, Set<Song> songs) {
        this.title = title;
        this.coverPhotoUrl = coverPhotoUrl;
        this.year = year;
        this.songs = songs;
    }

    public Album() {
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Set<Song> getSongs() {
        return songs;
    }

    public void setSongs(Set<Song> songs) {
        this.songs = songs;
    }
}
