package dev.semkoksharov.vibeshub2.dto.user;

public class ArtistDTO {

    private Long userId;
    private String artistName;
    private String description;

    public ArtistDTO() {
    }

    public ArtistDTO(Long userId, String artistName, String description) {
        this.userId = userId;
        this.artistName = artistName;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
