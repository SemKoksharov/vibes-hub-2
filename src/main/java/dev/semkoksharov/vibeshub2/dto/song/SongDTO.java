package dev.semkoksharov.vibeshub2.dto.song;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongDTO {
    private Long id;
    private Long albumId;
    private Long genreId;
    private String title;
    private int duration;
    private String url;
    private String minioPath;

}
