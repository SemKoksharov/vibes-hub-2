package dev.semkoksharov.vibeshub2.controllers;

import dev.semkoksharov.vibeshub2.dto.album.AlbumDTO;
import dev.semkoksharov.vibeshub2.dto.album.AlbumResponseDTO;
import dev.semkoksharov.vibeshub2.service.implementations.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @PostMapping
    public ResponseEntity<AlbumResponseDTO> createAlbum(@RequestBody AlbumDTO albumDTO) {
        AlbumResponseDTO createdAlbum = albumService.createAlbum(albumDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAlbum);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumResponseDTO> getAlbumById(@PathVariable("id") Long id) {
        AlbumResponseDTO album = albumService.getAlbumById(id);
        return ResponseEntity.ok(album);
    }

}
