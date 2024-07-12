package dev.semkoksharov.vibeshub2.dto.genre;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreSimpleDTO {
    private Long id;
    private String title;
}
