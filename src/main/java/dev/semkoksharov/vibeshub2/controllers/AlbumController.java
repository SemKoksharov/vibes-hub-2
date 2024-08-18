package dev.semkoksharov.vibeshub2.controllers;

import dev.semkoksharov.vibeshub2.dto.album.AlbumDTO;
import dev.semkoksharov.vibeshub2.dto.album.AlbumResponseDTO;
import dev.semkoksharov.vibeshub2.dto.forms.BaseResponseForm;
import dev.semkoksharov.vibeshub2.dto.forms.ResponseForm;
import dev.semkoksharov.vibeshub2.service.interfaces.AlbumService;
import dev.semkoksharov.vibeshub2.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseForm> getAlbumById(@PathVariable Long id) {
        AlbumResponseDTO album = albumService.getAlbumById(id);

        BaseResponseForm response = new ResponseForm(
                HttpStatus.OK.toString(),
                "Album found successfully.",
                DateTimeUtil.getFormattedTimestamp(),
                album);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<BaseResponseForm> getAllAlbums() {
        List<AlbumResponseDTO> allAlbums = albumService.getAllAlbums();

        BaseResponseForm response = new ResponseForm(
                HttpStatus.OK.toString(),
                allAlbums.size() + " album(s) found successfully",
                DateTimeUtil.getFormattedTimestamp(),
                allAlbums
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<BaseResponseForm> createAlbum(@RequestBody AlbumDTO albumDTO) {
        AlbumResponseDTO createdAlbum = albumService.createAlbum(albumDTO);

        BaseResponseForm response = new ResponseForm(
                HttpStatus.CREATED.toString(),
                "Album created successfully.",
                DateTimeUtil.getFormattedTimestamp(),
                createdAlbum);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponseForm> updateAlbum(@PathVariable Long id, @RequestBody AlbumDTO albumDTO) {
        AlbumResponseDTO album = albumService.updateAlbum(id, albumDTO);

        BaseResponseForm response = new ResponseForm(
                HttpStatus.OK.toString(),
                "Album updated successfully.",
                DateTimeUtil.getFormattedTimestamp(),
                album);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponseForm> deleteAlbumById(@PathVariable Long id) {
        albumService.deleteAlbumById(id);

        BaseResponseForm response = new ResponseForm(
                HttpStatus.OK.toString(),
                "Album with ID " + id + " has been deleted successfully.",
                DateTimeUtil.getFormattedTimestamp()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<BaseResponseForm> deleteAllAlbums() {
        albumService.deleteAllAlbums();

        BaseResponseForm response = new ResponseForm(
                HttpStatus.OK.toString(),
                "All albums have been deleted successfully.",
                DateTimeUtil.getFormattedTimestamp()
        );

        return ResponseEntity.ok(response);
    }
}
