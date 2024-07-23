package dev.semkoksharov.vibeshub2.service.interfaces;

import dev.semkoksharov.vibeshub2.dto.genre.GenreDTO;
import dev.semkoksharov.vibeshub2.dto.genre.GenreResponseDTO;

import java.util.List;

public interface GenreService {
    GenreResponseDTO createGenre(GenreDTO genreDTO);

    GenreResponseDTO getGenreById(Long id);

    List<GenreResponseDTO> getAllGenres();

    void deleteGenreById(Long id);

    GenreResponseDTO updateGenre(Long id, GenreDTO genreDTO);

    void deleteAllGenres();
}
