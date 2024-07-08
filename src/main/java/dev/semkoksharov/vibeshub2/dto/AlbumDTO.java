package dev.semkoksharov.vibeshub2.dto;

import dev.semkoksharov.vibeshub2.model.Artist;

import java.util.Set;

public class AlbumDTO {

    private String title;
    private Integer year;
    private Set<Long> artistIds;

    public AlbumDTO() {
    }

    public AlbumDTO(String title, Integer year, Set<Long> artistIds) {
        this.title = title;
        this.year = year;
        this.artistIds = artistIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Set<Long> getArtistIds() {
        return artistIds;
    }

    public void setArtistIds(Set<Long> artistIds) {
        this.artistIds = artistIds;
    }
}
