package dev.semkoksharov.vibeshub2.controllers;

import dev.semkoksharov.vibeshub2.dto.forms.BaseResponseForm;
import dev.semkoksharov.vibeshub2.dto.forms.ResponseForm;
import dev.semkoksharov.vibeshub2.dto.playlist.PlaylistRequestDTO;
import dev.semkoksharov.vibeshub2.dto.playlist.PlaylistResponseDTO;
import dev.semkoksharov.vibeshub2.service.implementations.PlaylistServiceImpl;
import dev.semkoksharov.vibeshub2.service.interfaces.PlaylistService;
import dev.semkoksharov.vibeshub2.utils.DateTimeUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistServiceImpl playlistService) {
        this.playlistService = playlistService;
    }

    @PostMapping
    public ResponseEntity<BaseResponseForm> createPlaylist(@RequestBody PlaylistRequestDTO playlistRequestDTO) {
        PlaylistResponseDTO responseDTO = playlistService.createPlaylist(playlistRequestDTO);
        String songIDs = playlistRequestDTO.getSongIDs().toString();

        BaseResponseForm rf = new ResponseForm(
                HttpStatus.OK.toString(),
                "Playlist created successfully. Song IDs: %s".formatted(songIDs),
                DateTimeUtil.getFormattedTimestamp(),
                responseDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(rf);
    }
}
