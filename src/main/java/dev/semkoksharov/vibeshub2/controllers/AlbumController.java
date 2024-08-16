package dev.semkoksharov.vibeshub2.controllers;

import dev.semkoksharov.vibeshub2.dto.album.AlbumDTO;
import dev.semkoksharov.vibeshub2.dto.album.AlbumResponseDTO;
import dev.semkoksharov.vibeshub2.dto.forms.BaseResponseForm;
import dev.semkoksharov.vibeshub2.dto.forms.ResponseForm;
import dev.semkoksharov.vibeshub2.service.implementations.AlbumServiceImpl;
import dev.semkoksharov.vibeshub2.service.interfaces.AlbumService;
import dev.semkoksharov.vibeshub2.utils.DateTimeUtil;
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
    public ResponseEntity<BaseResponseForm> getAlbumById(@PathVariable Long id) {
        AlbumResponseDTO album = albumService.getAlbumById(id);

        BaseResponseForm albumWasFound = new ResponseForm(
                HttpStatus.OK.toString(),
                "The album was found",
                DateTimeUtil.getFormattedTimestamp(),
                album);

        return ResponseEntity.ok(albumWasFound);
    }
        @PostMapping
        public ResponseEntity<BaseResponseForm> createAlbum (@RequestBody AlbumDTO albumDTO){
            AlbumResponseDTO createdAlbum = albumService.createAlbum(albumDTO);

            BaseResponseForm albumWasCreated = new ResponseForm(HttpStatus.CREATED.toString(),
                    "The album was created",
                    DateTimeUtil.getFormattedTimestamp(),
                    createdAlbum);

            return ResponseEntity.status(HttpStatus.CREATED).body(albumWasCreated);
        }

        @PutMapping
        public ResponseEntity<BaseResponseForm> updateAlbum (@PathVariable Long id, AlbumDTO albumDTO){
            AlbumResponseDTO album = albumService.updateAlbum(id, albumDTO);

            BaseResponseForm albumWasUpdated = new ResponseForm(HttpStatus.OK.toString(),
                    "The album was updated",
                    DateTimeUtil.getFormattedTimestamp(),
                    album);

            return ResponseEntity.ok(albumWasUpdated);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<BaseResponseForm> deleteAlbumById (@PathVariable Long id){
            albumService.deleteAlbumById(id);

            BaseResponseForm albumWasDeleted = new ResponseForm(HttpStatus.OK.toString(),
                    "Album with id " + id + " has been deleted" ,
                     DateTimeUtil.getFormattedTimestamp()
                    );

            return ResponseEntity.ok(albumWasDeleted);
        }

        @DeleteMapping("/deleteAll")
        public ResponseEntity<BaseResponseForm> deleteAlbumById () {
            albumService.deleteAllAlbums();

            BaseResponseForm allAlbumsHaveBeenDeleted = new ResponseForm(HttpStatus.OK.toString(),
                    "All albums have been deleted" ,
                    DateTimeUtil.getFormattedTimestamp()
            );

            return ResponseEntity.ok(allAlbumsHaveBeenDeleted);
        }
    }
