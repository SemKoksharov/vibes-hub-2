package dev.semkoksharov.vibeshub2.dto.song;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongSimpleDTO {
    private Long id;
    private String title;
    private String genre;
}
