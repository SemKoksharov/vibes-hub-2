package dev.semkoksharov.vibeshub2.dto.genre;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreResponseDTO {
    private String title;
    private String description;
}
