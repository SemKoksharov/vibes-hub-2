package dev.semkoksharov.vibeshub2.dto.album;

import dev.semkoksharov.vibeshub2.dto.song.SongSimpleDTO;
import dev.semkoksharov.vibeshub2.dto.user.ArtistSimpleDTO;
import lombok.Data;

import java.util.Set;

@Data
public class AlbumResponseDTO {

    private Long id;
    private String title;
    private String coverPhotoUrl;
    private Integer year;
    private Set<ArtistSimpleDTO> artists;
    private Set<SongSimpleDTO> songs;

    public AlbumResponseDTO() {
    }

    public AlbumResponseDTO(Long id, String title, String coverPhotoUrl, Integer year, Set<ArtistSimpleDTO> artists) {
        this.id = id;
        this.title = title;
        this.coverPhotoUrl = coverPhotoUrl;
        this.year = year;
        this.artists = artists;
    }

}
