package dev.semkoksharov.vibeshub2.dto.song;

import dev.semkoksharov.vibeshub2.dto.album.AlbumSimpleDTO;
import dev.semkoksharov.vibeshub2.dto.genre.GenreSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongResponseDTO {
    private Long id;
    private String title;
    private int duration;
    private String url;
    private String minioPath;
    private AlbumSimpleDTO album;
    private GenreSimpleDTO genre;

}
