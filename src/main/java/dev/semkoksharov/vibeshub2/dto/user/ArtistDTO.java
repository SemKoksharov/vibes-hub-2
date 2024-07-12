package dev.semkoksharov.vibeshub2.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistDTO {
    private Long id;
    private String description;
    private String artistName;

}
