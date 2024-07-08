package dev.semkoksharov.vibeshub2.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "albums")
public class Album extends BaseEntity {

    private String title;
    private String coverPhotoUrl;
    private String minioCoverPath;
    private int year;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Artist> artist;

    public Album() {
    }

    public Album(String title, String coverPhotoUrl, String minioCoverPath, int year, Set<Artist> artist) {
        this.title = title;
        this.coverPhotoUrl = coverPhotoUrl;
        this.minioCoverPath = minioCoverPath;
        this.year = year;
        this.artist = artist;
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

    public Set<Artist> getArtist() {
        return artist;
    }

    public void setArtist(Set<Artist> artist) {
        this.artist = artist;
    }
}
