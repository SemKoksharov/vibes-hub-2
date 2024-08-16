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

        BaseResponseForm genreWasFound = new ResponseForm(HttpStatus.OK.toString(),
                "The genre was found",
                DateTimeUtil.getFormattedTimestamp(),
                genre);

        return ResponseEntity.ok(genreWasFound);
    }

    @GetMapping
    public ResponseEntity<BaseResponseForm> getAllGenres() {
        List<GenreResponseDTO> genres = genreService.getAllGenres();

        BaseResponseForm genresHaveBeenFound = new ResponseForm(HttpStatus.OK.toString(),
                "Genres found: " + genres.size(),
                DateTimeUtil.getFormattedTimestamp(),
                genres);

        return ResponseEntity.ok(genresHaveBeenFound);
    }

    @PostMapping
    public ResponseEntity<BaseResponseForm> createGenre(@RequestBody GenreDTO genreDTO) {
        GenreResponseDTO createdGenre = genreService.createGenre(genreDTO);

        BaseResponseForm genreWasCreated = new ResponseForm(HttpStatus.CREATED.toString(),
                "The genre was created",
                DateTimeUtil.getFormattedTimestamp(),
                createdGenre);

        return ResponseEntity.status(HttpStatus.CREATED).body(genreWasCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponseForm> updateGenre(@PathVariable Long id, GenreDTO genreDTO) {
        GenreResponseDTO genre = genreService.updateGenre(id, genreDTO);

        BaseResponseForm genreWasUpdated = new ResponseForm(HttpStatus.OK.toString(),
                "The genre was updated",
                DateTimeUtil.getFormattedTimestamp(),
                genre);

        return ResponseEntity.ok(genreWasUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponseForm> deleteGenreById(@PathVariable Long id) {
        genreService.deleteGenreById(id);

        BaseResponseForm genreWasDeleted = new ResponseForm(HttpStatus.OK.toString(),
                "Genre with id " + id + " was deleted",
                DateTimeUtil.getFormattedTimestamp());

        return ResponseEntity.ok(genreWasDeleted);
    }

    @DeleteMapping
    public ResponseEntity<BaseResponseForm> deleteAllGenres() {
        genreService.deleteAllGenres();

        BaseResponseForm allGenresHaveBeenDeleted = new ResponseForm(HttpStatus.OK.toString(),
                "All genres have been deleted",
                DateTimeUtil.getFormattedTimestamp());

        return ResponseEntity.ok(allGenresHaveBeenDeleted);
    }
}
