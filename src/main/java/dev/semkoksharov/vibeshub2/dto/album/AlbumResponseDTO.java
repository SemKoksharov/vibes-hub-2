package dev.semkoksharov.vibeshub2.dto.album;

import dev.semkoksharov.vibeshub2.dto.user.ArtistSimpleDTO;

import java.util.Set;

public class AlbumResponseDTO {

    private Long id;
    private String title;
    private String coverPhotoUrl;
    private Integer year;
    private Set<ArtistSimpleDTO> artists;

    public AlbumResponseDTO() {
    }

    public AlbumResponseDTO(Long id, String title, String coverPhotoUrl, Integer year, Set<ArtistSimpleDTO> artists) {
        this.id = id;
        this.title = title;
        this.coverPhotoUrl = coverPhotoUrl;
        this.year = year;
        this.artists = artists;
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

    public String getCoverPhotoUrl() {
        return coverPhotoUrl;
    }

    public void setCoverPhotoUrl(String coverPhotoUrl) {
        this.coverPhotoUrl = coverPhotoUrl;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Set<ArtistSimpleDTO> getArtists() {
        return artists;
    }

    public void setArtists(Set<ArtistSimpleDTO> artists) {
        this.artists = artists;
    }
}
