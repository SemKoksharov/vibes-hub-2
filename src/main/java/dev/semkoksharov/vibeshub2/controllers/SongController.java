package dev.semkoksharov.vibeshub2.controllers;

import dev.semkoksharov.vibeshub2.dto.forms.BaseResponseForm;
import dev.semkoksharov.vibeshub2.dto.forms.ResponseForm;
import dev.semkoksharov.vibeshub2.dto.song.SongDTO;
import dev.semkoksharov.vibeshub2.dto.song.SongResponseDTO;
import dev.semkoksharov.vibeshub2.service.implementations.SongServiceImpl;
import dev.semkoksharov.vibeshub2.service.interfaces.SongService;
import dev.semkoksharov.vibeshub2.utils.DateTimeUtil;
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

    private final SongService songService;
    private static final Logger LOGGER = LoggerFactory.getLogger(SongController.class);

    @Autowired
    public SongController(SongServiceImpl songService) {
        this.songService = songService;
    }

    @PostMapping
    public ResponseEntity<BaseResponseForm> createSong(@RequestBody SongDTO songDTO) {
        SongResponseDTO createdSong = songService.createSong(songDTO);

        BaseResponseForm songWasCreated = new ResponseForm(
                HttpStatus.CREATED.toString(),
                "The song was created",
                DateTimeUtil.getFormattedTimestamp(),
                createdSong
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(songWasCreated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseForm> getSongById(@PathVariable Long id) {
        SongResponseDTO song = songService.getSongById(id);

        BaseResponseForm songWasFound = new ResponseForm(
                HttpStatus.OK.toString(),
                "The song was found",
                DateTimeUtil.getFormattedTimestamp(),
                song
        );

        return ResponseEntity.ok(songWasFound);
    }

    @GetMapping
    public ResponseEntity<BaseResponseForm> getAllSongs() {
        List<SongResponseDTO> songs = songService.getAllSongs();

        BaseResponseForm songsWereFound = new ResponseForm(
                HttpStatus.OK.toString(),
                "The songs were found",
                DateTimeUtil.getFormattedTimestamp(),
                songs
        );

        return ResponseEntity.ok(songsWereFound);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponseForm> updateSong(@PathVariable Long id, @RequestBody SongDTO songDTO) {
        SongResponseDTO updatedSong = songService.updateSong(id, songDTO);

        BaseResponseForm songWasUpdated = new ResponseForm(
                HttpStatus.OK.toString(),
                "The song was updated",
                DateTimeUtil.getFormattedTimestamp(),
                updatedSong
        );

        return ResponseEntity.ok(songWasUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponseForm> deleteSongById(@PathVariable Long id) {
        songService.deleteSongById(id);

        BaseResponseForm songWasDeleted = new ResponseForm(
                HttpStatus.OK.toString(),
                "Song with id " + id + " has been deleted",
                DateTimeUtil.getFormattedTimestamp()
        );

        return ResponseEntity.ok(songWasDeleted);
    }

    @PostMapping("/upload")
    public ResponseEntity<BaseResponseForm> uploadSongToMinio(@RequestParam List<MultipartFile> audio, @RequestParam List<Long> ids) {
        LOGGER.info("Received upload request with {} files and {} ids", audio.size(), ids.size());

        Map<String, String> uploadResult = songService.uploadAudio(audio, ids);
        LOGGER.info("Files uploaded successfully");

        BaseResponseForm uploadSuccessful = new ResponseForm(
                HttpStatus.OK.toString(),
                "You can see the upload results below in the 'data' subsection",
                DateTimeUtil.getFormattedTimestamp(),
                uploadResult
        );

        return ResponseEntity.ok(uploadSuccessful);
    }

    @DeleteMapping("/del")
    public ResponseEntity<BaseResponseForm> deleteSongFromMinio(@RequestParam List<Long> songIDs){
        Map<String, String> delResult = songService.multiDeleteAudioFromBlobStorage(songIDs);

        BaseResponseForm deleted = new ResponseForm(
                HttpStatus.OK.toString(),
                "You can see the delete results below in the 'data' subsection",
                DateTimeUtil.getFormattedTimestamp(),
                delResult
                );

        return ResponseEntity.ok(deleted);
    }
}
