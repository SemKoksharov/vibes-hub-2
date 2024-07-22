package dev.semkoksharov.vibeshub2.controllers;

import dev.semkoksharov.vibeshub2.dto.genre.GenreDTO;
import dev.semkoksharov.vibeshub2.dto.genre.GenreResponseDTO;
import dev.semkoksharov.vibeshub2.service.interfaces.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @PostMapping
    public ResponseEntity<GenreResponseDTO> createGenre(@RequestBody GenreDTO genreDTO) {
        GenreResponseDTO createdGenre = genreService.createGenre(genreDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGenre);
    }

    @GetMapping
    public ResponseEntity<List<GenreResponseDTO>> getAllGenres() {
        List<GenreResponseDTO> genres = genreService.getAllGenres();

        return ResponseEntity.ok(genres);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenreById(@PathVariable Long id) {
        genreService.deleteGenreById(id);

        return ResponseEntity.noContent().build();
    }
}
