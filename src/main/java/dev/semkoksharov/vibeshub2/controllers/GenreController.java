package dev.semkoksharov.vibeshub2.controllers;

import dev.semkoksharov.vibeshub2.dto.forms.BaseResponseForm;
import dev.semkoksharov.vibeshub2.dto.forms.ResponseForm;
import dev.semkoksharov.vibeshub2.dto.genre.GenreDTO;
import dev.semkoksharov.vibeshub2.dto.genre.GenreResponseDTO;
import dev.semkoksharov.vibeshub2.service.interfaces.GenreService;
import dev.semkoksharov.vibeshub2.utils.DateTimeUtil;
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

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseForm> getGenreById(@PathVariable Long id) {
        GenreResponseDTO genre = genreService.getGenreById(id);

        BaseResponseForm response = new ResponseForm(
                HttpStatus.OK.toString(),
                "Genre found successfully.",
                DateTimeUtil.getFormattedTimestamp(),
                genre
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<BaseResponseForm> getAllGenres() {
        List<GenreResponseDTO> genres = genreService.getAllGenres();

        BaseResponseForm response = new ResponseForm(
                HttpStatus.OK.toString(),
                genres.size() + " genre(s) found successfully.",
                DateTimeUtil.getFormattedTimestamp(),
                genres
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<BaseResponseForm> createGenre(@RequestBody GenreDTO genreDTO) {
        GenreResponseDTO createdGenre = genreService.createGenre(genreDTO);

        BaseResponseForm response = new ResponseForm(
                HttpStatus.CREATED.toString(),
                "Genre created successfully.",
                DateTimeUtil.getFormattedTimestamp(),
                createdGenre
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponseForm> updateGenre(@PathVariable Long id, @RequestBody GenreDTO genreDTO) {
        GenreResponseDTO updatedGenre = genreService.updateGenre(id, genreDTO);

        BaseResponseForm response = new ResponseForm(
                HttpStatus.OK.toString(),
                "Genre updated successfully.",
                DateTimeUtil.getFormattedTimestamp(),
                updatedGenre
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponseForm> deleteGenreById(@PathVariable Long id) {
        genreService.deleteGenreById(id);

        BaseResponseForm response = new ResponseForm(
                HttpStatus.OK.toString(),
                "Genre with ID " + id + " has been deleted successfully.",
                DateTimeUtil.getFormattedTimestamp()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<BaseResponseForm> deleteAllGenres() {
        genreService.deleteAllGenres();

        BaseResponseForm response = new ResponseForm(
                HttpStatus.OK.toString(),
                "All genres have been deleted successfully.",
                DateTimeUtil.getFormattedTimestamp()
        );

        return ResponseEntity.ok(response);
    }
}
