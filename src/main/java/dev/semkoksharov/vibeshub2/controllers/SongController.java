package dev.semkoksharov.vibeshub2.controllers;

import dev.semkoksharov.vibeshub2.dto.song.SongDTO;
import dev.semkoksharov.vibeshub2.dto.song.SongResponseDTO;
import dev.semkoksharov.vibeshub2.service.implementations.SongService;
import dev.semkoksharov.vibeshub2.service.interfaces.SongServiceInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/songs")
public class SongController {

    private final SongServiceInt songService;
    private static final Logger LOGGER = LoggerFactory.getLogger(SongController.class);
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


    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadSongToMinio(@RequestParam List<MultipartFile> audio, @RequestParam List<Long> ids) {
        LOGGER.info("Received upload request with {} files and {} ids", audio.size(), ids.size());

        Map<String, String> uploadedFileUrls = songService.uploadAudio(audio, ids);
        LOGGER.info("Files uploaded successfully");

        return ResponseEntity.status(HttpStatus.OK).body(uploadedFileUrls);
    }
}
