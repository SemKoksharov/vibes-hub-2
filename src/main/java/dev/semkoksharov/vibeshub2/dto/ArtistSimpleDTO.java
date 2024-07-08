package dev.semkoksharov.vibeshub2.dto;

public class ArtistSimpleDTO {
    private Long id;
    private String description;
    private String artistName;

    public ArtistSimpleDTO() {
    }

    public ArtistSimpleDTO(Long id, String description, String artistName) {
        this.id = id;
        this.description = description;
        this.artistName = artistName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
