package dev.semkoksharov.vibeshub2.controllers;

import dev.semkoksharov.vibeshub2.dto.forms.BaseResponseForm;
import dev.semkoksharov.vibeshub2.dto.forms.ResponseForm;
import dev.semkoksharov.vibeshub2.dto.song.SongDTO;
import dev.semkoksharov.vibeshub2.dto.song.SongResponseDTO;
import dev.semkoksharov.vibeshub2.service.interfaces.AudioStreamingService;
import dev.semkoksharov.vibeshub2.service.interfaces.SongService;
import dev.semkoksharov.vibeshub2.utils.DateTimeUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/songs")
public class SongController {

    private final SongService songService;
    private final AudioStreamingService audioStreamingService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SongController.class);

    @Autowired
    public SongController(SongService songService, AudioStreamingService audioStreamingService) {
        this.songService = songService;
        this.audioStreamingService = audioStreamingService;
    }

    @GetMapping(value = "/stream/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void streamAudio(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        audioStreamingService.streamAudio(id, request, response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseForm> getSongById(@PathVariable Long id) {
        SongResponseDTO song = songService.getSongById(id);

        BaseResponseForm response = new ResponseForm(
                HttpStatus.OK.toString(),
                "Song found successfully.",
                DateTimeUtil.getFormattedTimestamp(),
                song
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<BaseResponseForm> getAllSongs() {
        List<SongResponseDTO> songs = songService.getAllSongs();

        BaseResponseForm response = new ResponseForm(
                HttpStatus.OK.toString(),
                songs.size() + " song(s) found successfully.",
                DateTimeUtil.getFormattedTimestamp(),
                songs
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<BaseResponseForm> createSong(@RequestBody SongDTO songDTO) {
        SongResponseDTO createdSong = songService.createSong(songDTO);

        BaseResponseForm response = new ResponseForm(
                HttpStatus.CREATED.toString(),
                "Song created successfully.",
                DateTimeUtil.getFormattedTimestamp(),
                createdSong
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/upload")
    public ResponseEntity<BaseResponseForm> uploadSongToMinio(@RequestParam List<MultipartFile> audio, @RequestParam List<Long> ids) {
        LOGGER.info("Received upload request with {} files and {} ids", audio.size(), ids.size());

        Map<String, String> uploadResult = songService.uploadAudio(audio, ids);
        LOGGER.info("Files uploaded successfully.");

        BaseResponseForm response = new ResponseForm(
                HttpStatus.OK.toString(),
                "Upload completed successfully. See details in the 'data' section.",
                DateTimeUtil.getFormattedTimestamp(),
                uploadResult
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponseForm> updateSong(@PathVariable Long id, @RequestBody SongDTO songDTO) {
        SongResponseDTO updatedSong = songService.updateSong(id, songDTO);

        BaseResponseForm response = new ResponseForm(
                HttpStatus.OK.toString(),
                "Song updated successfully.",
                DateTimeUtil.getFormattedTimestamp(),
                updatedSong
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponseForm> deleteSongById(@PathVariable Long id) {
        songService.deleteSongById(id);

        BaseResponseForm response = new ResponseForm(
                HttpStatus.OK.toString(),
                "Song with ID " + id + " has been deleted successfully.",
                DateTimeUtil.getFormattedTimestamp()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/del")
    public ResponseEntity<BaseResponseForm> deleteSongFromMinio(@RequestParam List<Long> songIDs) {
        Map<String, String> delResult = songService.multiDeleteAudioFromBlobStorage(songIDs);

        BaseResponseForm response = new ResponseForm(
                HttpStatus.OK.toString(),
                "Deletion completed successfully. See details in the 'data' section.",
                DateTimeUtil.getFormattedTimestamp(),
                delResult
        );

        return ResponseEntity.ok(response);
    }
}
