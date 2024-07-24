package dev.semkoksharov.vibeshub2.dto.album;

import dev.semkoksharov.vibeshub2.dto.song.SongSimpleDTO;
import dev.semkoksharov.vibeshub2.dto.user.ArtistSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumResponseDTO {

    private Long id;
    private String title;
    private String coverPhotoUrl;
    private Integer year;
    private ArtistSimpleDTO artist;
    private Set<SongSimpleDTO> songs;
}
