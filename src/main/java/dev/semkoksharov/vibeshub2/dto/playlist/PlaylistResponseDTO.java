package dev.semkoksharov.vibeshub2.dto.playlist;

import dev.semkoksharov.vibeshub2.dto.song.SongSimpleDTO;
import dev.semkoksharov.vibeshub2.dto.user.UserResponseDTO;
import dev.semkoksharov.vibeshub2.dto.user.UserSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistResponseDTO {
    private Long id;
    private UserSimpleDTO user;
    private String title;
    private Set<SongSimpleDTO> songs;
}
