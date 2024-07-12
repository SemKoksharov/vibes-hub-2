package dev.semkoksharov.vibeshub2.dto.album;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AlbumDTO {

    private String title;
    private Integer year;
    private Set<Long> artistIds;
}


