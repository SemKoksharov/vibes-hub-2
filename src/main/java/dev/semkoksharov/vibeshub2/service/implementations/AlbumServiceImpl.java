package dev.semkoksharov.vibeshub2.service.implementations;

import dev.semkoksharov.vibeshub2.dto.album.AlbumDTO;
import dev.semkoksharov.vibeshub2.dto.album.AlbumResponseDTO;
import dev.semkoksharov.vibeshub2.dto.song.SongSimpleDTO;
import dev.semkoksharov.vibeshub2.dto.user.ArtistSimpleDTO;
import dev.semkoksharov.vibeshub2.exceptions.EmptyResultException;
import dev.semkoksharov.vibeshub2.exceptions.EntityUpdaterException;
import dev.semkoksharov.vibeshub2.model.Album;
import dev.semkoksharov.vibeshub2.model.Artist;
import dev.semkoksharov.vibeshub2.model.Song;
import dev.semkoksharov.vibeshub2.repository.AlbumRepo;
import dev.semkoksharov.vibeshub2.repository.ArtistDetailsRepo;
import dev.semkoksharov.vibeshub2.repository.SongRepo;
import dev.semkoksharov.vibeshub2.service.interfaces.AlbumService;
import dev.semkoksharov.vibeshub2.utils.EntityUpdater;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepo albumRepository;
    private final ArtistDetailsRepo artistRepo;
    private final SongRepo songRepo;
    private final ModelMapper modelMapper;
    private final EntityUpdater entityUpdater;

    @Autowired
    public AlbumServiceImpl(AlbumRepo albumRepository, ArtistDetailsRepo artistRepo, SongRepo songRepo, ModelMapper modelMapper, EntityUpdater entityUpdater) {
        this.albumRepository = albumRepository;
        this.artistRepo = artistRepo;
        this.songRepo = songRepo;
        this.modelMapper = modelMapper;
        this.entityUpdater = entityUpdater;
    }

    @Override
    public AlbumResponseDTO createAlbum(AlbumDTO albumDTO) {
        Album album = modelMapper.map(albumDTO, Album.class);
        Artist artist = artistRepo.findById(albumDTO.getArtistId())
                .orElseThrow(() -> new EntityNotFoundException("[Album creation error] Artist with id " +
                        albumDTO.getArtistId() + " is not found in the database"));

        album.setArtist(artist);
        Album savedAlbum = albumRepository.saveAndFlush(album);

        artist.getAlbums().add(savedAlbum);
        artistRepo.saveAndFlush(artist);

        AlbumResponseDTO responseDTO = modelMapper.map(savedAlbum, AlbumResponseDTO.class);
        ArtistSimpleDTO artistDTOs = modelMapper.map(artist, ArtistSimpleDTO.class);
        responseDTO.setArtist(artistDTOs);

        return responseDTO;
    }

    @Override
    public AlbumResponseDTO getAlbumById(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("[Get error] Album with id " + id + " not found"));
        AlbumResponseDTO responseDTO = modelMapper.map(album, AlbumResponseDTO.class);
        Artist artist = album.getArtist();

        ArtistSimpleDTO artistDTO = modelMapper.map(artist, ArtistSimpleDTO.class);
        Set<SongSimpleDTO> songSimpleDTOs = album.getSongs()
                .stream()
                .map(song -> modelMapper.map(song, SongSimpleDTO.class))
                .collect(Collectors.toSet());

        responseDTO.setArtist(artistDTO);
        responseDTO.setSongs(songSimpleDTOs);

        return responseDTO;
    }

    @Override
    public List<AlbumResponseDTO> getAllAlbums() {
        List<Album> albums = albumRepository.findAll();
        if (albums.isEmpty()) throw new EmptyResultException("[Get error] No albums found in the database");

        return albums.stream().map(album -> {
            AlbumResponseDTO responseDTO = modelMapper.map(album, AlbumResponseDTO.class);
            ArtistSimpleDTO artistDTO = modelMapper.map(album.getArtist(), ArtistSimpleDTO.class);
            Set<SongSimpleDTO> songSimpleDTOs = album.getSongs()
                    .stream()
                    .map(song -> modelMapper.map(song, SongSimpleDTO.class))
                    .collect(Collectors.toSet());

            responseDTO.setArtist(artistDTO);
            responseDTO.setSongs(songSimpleDTOs);
            return responseDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteAlbumById(Long id) {
        if (!albumRepository.existsById(id))
            throw new EntityNotFoundException("[Delete error] Album with id " + id + " not found");
        albumRepository.deleteById(id);
    }

    @Override
    public void deleteAllAlbums() {
        albumRepository.deleteAll();
    }

    @Override
    public AlbumResponseDTO updateAlbum(Long id, AlbumDTO albumDTO) {
        Album toUpdate = albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("[Update error] Album with id " + id + " not found"));

        try {
            entityUpdater.update(albumDTO, toUpdate);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new EntityUpdaterException("[Update error] Caused by: " + e.getClass().getSimpleName() +
                    " Exception message: " + e.getMessage());
        }

        Album updatedAlbum = albumRepository.save(toUpdate);
        Artist artist = updatedAlbum.getArtist();
        AlbumResponseDTO responseDTO = modelMapper.map(updatedAlbum, AlbumResponseDTO.class);
        ArtistSimpleDTO artistDTO = modelMapper.map(artist, ArtistSimpleDTO.class);
        responseDTO.setArtist(artistDTO);

        return responseDTO;
    }

    @Override
    public void addSongToAlbum(Long albumId, Song song) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("[Update error] Album with id " + albumId + " not found"));

        album.addSong(song);
        song.setAlbum(album);
        albumRepository.saveAndFlush(album);
        songRepo.saveAndFlush(song);
    }

    @Override
    public void removeSongFromAlbum(Long albumId, Song song) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("[Update error] Album with id " + albumId + " not found"));

        album.removeSong(song);
        song.setAlbum(null);
        albumRepository.saveAndFlush(album);
        songRepo.saveAndFlush(song);
    }
}
