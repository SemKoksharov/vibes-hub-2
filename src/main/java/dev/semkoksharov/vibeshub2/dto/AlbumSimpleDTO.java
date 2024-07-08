package dev.semkoksharov.vibeshub2.dto;

public class AlbumSimpleDTO {
    private Long id;
    private String title;
    private String coverPhotoUrl;
    private Integer year;

    public AlbumSimpleDTO(Long id, String title, String coverPhotoUrl, Integer year) {
        this.id = id;
        this.title = title;
        this.coverPhotoUrl = coverPhotoUrl;
        this.year = year;
    }

    public AlbumSimpleDTO() {
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
}
