package dev.semkoksharov.vibeshub2.service.implementations;

import dev.semkoksharov.vibeshub2.dto.album.AlbumSimpleDTO;
import dev.semkoksharov.vibeshub2.dto.genre.GenreSimpleDTO;
import dev.semkoksharov.vibeshub2.dto.song.SongDTO;
import dev.semkoksharov.vibeshub2.dto.song.SongResponseDTO;
import dev.semkoksharov.vibeshub2.exceptions.EntityUpdaterException;
import dev.semkoksharov.vibeshub2.model.Album;
import dev.semkoksharov.vibeshub2.model.Genre;
import dev.semkoksharov.vibeshub2.model.Song;
import dev.semkoksharov.vibeshub2.repository.AlbumRepo;
import dev.semkoksharov.vibeshub2.repository.GenreRepo;
import dev.semkoksharov.vibeshub2.repository.SongRepo;
import dev.semkoksharov.vibeshub2.service.interfaces.SongServiceInt;
import dev.semkoksharov.vibeshub2.utils.EntityUpdater;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SongService implements SongServiceInt {

    private final SongRepo songRepo;
    private final AlbumRepo albumRepo;
    private final GenreRepo genreRepo;
    private final ModelMapper modelMapper;
    private final EntityUpdater entityUpdater;

    @Autowired
    public SongService(SongRepo songRepo, AlbumRepo albumRepo, GenreRepo genreRepo, ModelMapper modelMapper, EntityUpdater entityUpdater) {
        this.songRepo = songRepo;
        this.albumRepo = albumRepo;
        this.genreRepo = genreRepo;
        this.modelMapper = modelMapper;
        this.entityUpdater = entityUpdater;
    }

    @Override
    public SongResponseDTO createSong(SongDTO songDTO) {
        Song song = modelMapper.map(songDTO, Song.class);

        Optional<Album> albumOptional = albumRepo.findById(songDTO.getAlbumId());
        if (albumOptional.isEmpty()) {
            throw new IllegalArgumentException("[Create error] Album with id " + songDTO.getAlbumId() + " not found");
        }
        Album album = albumOptional.get();

        Optional<Genre> genreOptional = genreRepo.findById(songDTO.getGenreId());
        if (genreOptional.isEmpty()) {
            throw new IllegalArgumentException("[Create error] Genre with id " + songDTO.getGenreId() + " not found");
        }
        Genre genre = genreOptional.get();

        song.setAlbum(album);
        song.setGenre(genre);
        album.addSong(song);

        Song savedSong = songRepo.save(song);
        return mapToResponseDTO(savedSong, album, genre);
    }

    @Override
    public SongResponseDTO getSongById(Long id) {
        Optional<Song> songOptional = songRepo.findById(id);
        if (songOptional.isEmpty()) {
            throw new IllegalArgumentException("[Get error] Song with id " + id + " is not found");
        }

        Song song = songOptional.get();
        return mapToResponseDTO(song, song.getAlbum(), song.getGenre());
    }

    @Override
    public List<SongResponseDTO> getAllSongs() {
        List<Song> songs = songRepo.findAll();
        if (songs.isEmpty()) {
            throw new IllegalArgumentException("[Get error] No songs found in the database");
        }
        return songs.stream()
                .map(song -> mapToResponseDTO(song, song.getAlbum(), song.getGenre()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSongById(Long id) {
        if (!songRepo.existsById(id)) {
            throw new IllegalArgumentException("[Delete error] Song with id " + id + " is not found");
        }
        songRepo.deleteById(id);
    }

    @Override
    public SongResponseDTO updateSong(Long id, SongDTO songDTO) {
        Optional<Song> optionalSong = songRepo.findById(id);
        if (optionalSong.isEmpty()) {
            throw new IllegalArgumentException("[Update error] Song with id " + id + " not found");
        }

        Optional<Album> albumOptional = albumRepo.findById(songDTO.getAlbumId());
        if (albumOptional.isEmpty()) {
            throw new IllegalArgumentException("[Update error] Album with id " + songDTO.getAlbumId() + " not found");
        }
        Album album = albumOptional.get();

        Optional<Genre> genreOptional = genreRepo.findById(songDTO.getGenreId());
        if (genreOptional.isEmpty()) {
            throw new IllegalArgumentException("[Update error] Genre with id " + songDTO.getGenreId() + " not found");
        }
        Genre genre = genreOptional.get();

        Song toUpdate = optionalSong.get();
        toUpdate.setAlbum(album);
        toUpdate.setGenre(genre);

        try {
            entityUpdater.update(songDTO, toUpdate);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new EntityUpdaterException("[Update error] Caused by: " + e.getClass().getSimpleName() +
                    " Exception message: " + e.getMessage());
        }

        Song updatedSong = songRepo.save(toUpdate);
        return mapToResponseDTO(updatedSong, album, genre);
    }

    private SongResponseDTO mapToResponseDTO(Song song, Album album, Genre genre) {
        SongResponseDTO responseDTO = modelMapper.map(song, SongResponseDTO.class);
        responseDTO.setAlbum(modelMapper.map(album, AlbumSimpleDTO.class));
        responseDTO.setGenre(modelMapper.map(genre, GenreSimpleDTO.class));
        return responseDTO;
    }
}
