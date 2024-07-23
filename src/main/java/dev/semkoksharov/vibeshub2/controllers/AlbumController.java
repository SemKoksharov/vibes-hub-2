package dev.semkoksharov.vibeshub2.controllers;

import dev.semkoksharov.vibeshub2.dto.album.AlbumDTO;
import dev.semkoksharov.vibeshub2.dto.album.AlbumResponseDTO;
import dev.semkoksharov.vibeshub2.service.implementations.AlbumServiceImpl;
import dev.semkoksharov.vibeshub2.service.interfaces.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final AlbumService albumService;

    @Autowired
    public AlbumController(AlbumServiceImpl albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumResponseDTO> getAlbumById(@PathVariable Long id) {
        AlbumResponseDTO album = albumService.getAlbumById(id);

        return ResponseEntity.ok(album);
    }

    @PostMapping
    public ResponseEntity<AlbumResponseDTO> createAlbum(@RequestBody AlbumDTO albumDTO) {
        AlbumResponseDTO createdAlbum = albumService.createAlbum(albumDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAlbum);
    }

    @PutMapping
    public ResponseEntity<AlbumResponseDTO> updateAlbum(@PathVariable Long id, AlbumDTO albumDTO){
        AlbumResponseDTO album = albumService.updateAlbum(id, albumDTO);

        return ResponseEntity.ok(album);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAlbumById(@PathVariable Long id){
        albumService.deleteAlbumById(id);

        return ResponseEntity.ok("Album with id " + id + " has been deleted");
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAlbumById(){
        albumService.deleteAllAlbums();

        return ResponseEntity.ok("All albums have been deleted");
    }
}
