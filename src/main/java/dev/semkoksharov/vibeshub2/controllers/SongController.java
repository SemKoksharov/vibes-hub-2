package dev.semkoksharov.vibeshub2.controllers;

import dev.semkoksharov.vibeshub2.dto.song.SongDTO;
import dev.semkoksharov.vibeshub2.dto.song.SongResponseDTO;
import dev.semkoksharov.vibeshub2.service.implementations.SongService;
import dev.semkoksharov.vibeshub2.service.interfaces.SongServiceInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
public class SongController {

    private final SongServiceInt songService;

    @Autowired
    public SongController(SongService songService) {
        this.songService = songService;
    }

    @PostMapping
    public ResponseEntity<SongResponseDTO> createSong(@RequestBody SongDTO songDTO) {
        SongResponseDTO createdSong = songService.createSong(songDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdSong);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongResponseDTO> getSongById(@PathVariable Long id) {
        SongResponseDTO song = songService.getSongById(id);

        return ResponseEntity.status(HttpStatus.OK).body(song);
    }

    @GetMapping
    public ResponseEntity<List<SongResponseDTO>> getAllSongs() {
        List<SongResponseDTO> songs = songService.getAllSongs();
        return ResponseEntity.ok(songs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SongResponseDTO> updateSong(@PathVariable Long id, @RequestBody SongDTO songDTO) {
        SongResponseDTO updatedSong = songService.updateSong(id, songDTO);

        return ResponseEntity.ok(updatedSong);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSongById(@PathVariable Long id) {
        songService.deleteSongById(id);

        return ResponseEntity.status(HttpStatus.OK).body("Song with id '" + id + "' was deleted");
    }
}
