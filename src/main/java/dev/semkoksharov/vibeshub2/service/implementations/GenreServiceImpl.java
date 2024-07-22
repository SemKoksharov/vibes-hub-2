package dev.semkoksharov.vibeshub2.service.implementations;

import dev.semkoksharov.vibeshub2.dto.genre.GenreDTO;
import dev.semkoksharov.vibeshub2.dto.genre.GenreResponseDTO;
import dev.semkoksharov.vibeshub2.exceptions.EntityUpdaterException;
import dev.semkoksharov.vibeshub2.model.Genre;
import dev.semkoksharov.vibeshub2.repository.GenreRepo;
import dev.semkoksharov.vibeshub2.service.interfaces.GenreService;
import dev.semkoksharov.vibeshub2.utils.EntityUpdater;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepo genreRepo;
    private final ModelMapper modelMapper;
    private final EntityUpdater entityUpdater;

    @Autowired
    public GenreServiceImpl(GenreRepo genreRepo, ModelMapper modelMapper, EntityUpdater entityUpdater) {
        this.genreRepo = genreRepo;
        this.modelMapper = modelMapper;
        this.entityUpdater = entityUpdater;
    }

    @Override
    public GenreResponseDTO createGenre(GenreDTO genreDTO) {
        Genre genre = modelMapper.map(genreDTO, Genre.class);
        Genre savedGenre = genreRepo.save(genre);
        return modelMapper.map(savedGenre, GenreResponseDTO.class);
    }

    @Override
    public GenreResponseDTO getGenreById(Long id) {
        Optional<Genre> genreOptional = genreRepo.findById(id);
        if (genreOptional.isEmpty()) throw new IllegalArgumentException("[Get error] Genre with id " + id + " not found");

        Genre genre = genreOptional.get();
        return modelMapper.map(genre, GenreResponseDTO.class);
    }

    @Override
    public List<GenreResponseDTO> getAllGenres() {
        List<Genre> genres = genreRepo.findAll();
        if (genres.isEmpty()) throw new IllegalArgumentException("[Get error] No genres found in the database");

        return genres.stream()
                .map(genre -> modelMapper.map(genre, GenreResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteGenreById(Long id) {
        if (!genreRepo.existsById(id)) throw new IllegalArgumentException("[Delete error] Genre with id " + id + " not found");
        genreRepo.deleteById(id);
    }

    @Override
    public GenreResponseDTO updateGenre(Long id, GenreDTO genreDTO) {
        Optional<Genre> optionalGenre = genreRepo.findById(id);
        if (optionalGenre.isEmpty()) throw new IllegalArgumentException("[Update error] Genre with id " + id + " not found");

        Genre toUpdate = optionalGenre.get();

        try {
            entityUpdater.update(genreDTO, toUpdate);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new EntityUpdaterException("[Update error] Caused by: " + e.getClass().getSimpleName() +
                    " Exception message: " + e.getMessage());
        }

        toUpdate.setTitle(genreDTO.getTitle());
        toUpdate.setDescription(genreDTO.getDescription());

        Genre updatedGenre = genreRepo.save(toUpdate);
        return modelMapper.map(updatedGenre, GenreResponseDTO.class);
    }
}
