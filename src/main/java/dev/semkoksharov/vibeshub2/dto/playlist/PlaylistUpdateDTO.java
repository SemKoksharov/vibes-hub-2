package dev.semkoksharov.vibeshub2.dto.playlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistUpdateDTO {

    private String title;
    private List<Long> songIDs;
}
