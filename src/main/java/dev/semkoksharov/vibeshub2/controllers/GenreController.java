package dev.semkoksharov.vibeshub2.controllers;

import dev.semkoksharov.vibeshub2.dto.genre.GenreDTO;
import dev.semkoksharov.vibeshub2.dto.genre.GenreResponseDTO;
import dev.semkoksharov.vibeshub2.service.interfaces.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreResponseDTO> getGenreById(@PathVariable  Long id){
        GenreResponseDTO genre = genreService.getGenreById(id);

        return ResponseEntity.ok(genre);
    }

    @GetMapping
    public ResponseEntity<List<GenreResponseDTO>> getAllGenres() {
        List<GenreResponseDTO> genres = genreService.getAllGenres();

        return ResponseEntity.ok(genres);
    }

    @PostMapping
    public ResponseEntity<GenreResponseDTO> createGenre(@RequestBody GenreDTO genreDTO) {
        GenreResponseDTO createdGenre = genreService.createGenre(genreDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdGenre);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreResponseDTO> updateGenre(@PathVariable  Long id, GenreDTO genreDTO){
        GenreResponseDTO genre = genreService.updateGenre(id, genreDTO);

        return ResponseEntity.ok(genre);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGenreById(@PathVariable Long id) {
        genreService.deleteGenreById(id);

        return ResponseEntity.ok("Genre with id " + id + " was deleted");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllGenres() {
        genreService.deleteAllGenres();

        return ResponseEntity.ok("All genres have been deleted");
    }
}
