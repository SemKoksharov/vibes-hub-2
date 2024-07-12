package dev.semkoksharov.vibeshub2.dto.user;
import dev.semkoksharov.vibeshub2.dto.album.AlbumSimpleDTO;
import dev.semkoksharov.vibeshub2.dto.song.SongSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistResponseDTO {
    private Long id;
    private String description;
    private String artistName;
    private Set<AlbumSimpleDTO> albums;
    private Set<SongSimpleDTO> songs;

}
