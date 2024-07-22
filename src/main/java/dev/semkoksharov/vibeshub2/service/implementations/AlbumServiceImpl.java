package dev.semkoksharov.vibeshub2.service.implementations;

import dev.semkoksharov.vibeshub2.dto.album.AlbumDTO;
import dev.semkoksharov.vibeshub2.dto.album.AlbumResponseDTO;
import dev.semkoksharov.vibeshub2.dto.song.SongSimpleDTO;
import dev.semkoksharov.vibeshub2.dto.user.ArtistSimpleDTO;
import dev.semkoksharov.vibeshub2.exceptions.EntityUpdaterException;
import dev.semkoksharov.vibeshub2.model.Album;
import dev.semkoksharov.vibeshub2.model.Artist;
import dev.semkoksharov.vibeshub2.model.Song;
import dev.semkoksharov.vibeshub2.repository.AlbumRepo;
import dev.semkoksharov.vibeshub2.repository.ArtistDetailsRepo;
import dev.semkoksharov.vibeshub2.repository.SongRepo;
import dev.semkoksharov.vibeshub2.utils.EntityUpdater;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AlbumServiceImpl implements dev.semkoksharov.vibeshub2.service.interfaces.AlbumService {

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
        Set<Artist> artists = new HashSet<>(artistRepo.findAllById(albumDTO.getArtistIds()));
        if (artists.isEmpty()) throw new IllegalArgumentException("[Create error] Artists not found or empty list");

        album.setArtists(artists);
        Album savedAlbum = albumRepository.saveAndFlush(album);

        artists.forEach(artist -> artist.getAlbums().add(savedAlbum));
        artistRepo.saveAllAndFlush(artists);

        AlbumResponseDTO responseDTO = modelMapper.map(savedAlbum, AlbumResponseDTO.class);
        Set<ArtistSimpleDTO> artistDTOs = artists.stream()
                .map(artist -> modelMapper.map(artist, ArtistSimpleDTO.class))
                .collect(Collectors.toSet());
        responseDTO.setArtists(artistDTOs);

        return responseDTO;
    }

    @Override
    public AlbumResponseDTO getAlbumById(Long id) {
        Optional<Album> albumOptional = albumRepository.findById(id);
        if (albumOptional.isEmpty()) throw new IllegalArgumentException("[Get error] Album with id " + id + " not found");

        Album album = albumOptional.get();
        AlbumResponseDTO responseDTO = modelMapper.map(album, AlbumResponseDTO.class);
        Set<ArtistSimpleDTO> artistDTOs = album.getArtists().stream()
                .map(artist -> modelMapper.map(artist, ArtistSimpleDTO.class))
                .collect(Collectors.toSet());
        responseDTO.setArtists(artistDTOs);

        return responseDTO;
    }

    @Override
    public List<AlbumResponseDTO> getAllAlbums() {
        List<Album> albums = albumRepository.findAll();
        if (albums.isEmpty()) throw new IllegalArgumentException("[Get error] No albums found in the database");

        return albums.stream().map(album -> {
            AlbumResponseDTO responseDTO = modelMapper.map(album, AlbumResponseDTO.class);
            Set<ArtistSimpleDTO> artistDTOs = album.getArtists()
                    .stream()
                    .map(artist -> modelMapper.map(artist, ArtistSimpleDTO.class))
                    .collect(Collectors.toSet());

            Set<SongSimpleDTO> songSimpleDTOs = album.getSongs()
                    .stream()
                    .map(song -> modelMapper.map(song, SongSimpleDTO.class))
                    .collect(Collectors.toSet());

            responseDTO.setArtists(artistDTOs);
            responseDTO.setSongs(songSimpleDTOs);
            return responseDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteAlbumById(Long id) {
        if (!albumRepository.existsById(id)) throw new IllegalArgumentException("[Delete error] Album with id " + id + " not found");
        albumRepository.deleteById(id);
    }

    @Override
    public AlbumResponseDTO updateAlbum(Long id, AlbumDTO albumDTO) {
        Optional<Album> optionalAlbum = albumRepository.findById(id);
        if (optionalAlbum.isEmpty()) throw new IllegalArgumentException("[Update error] Album with id " + id + " not found");

        Album toUpdate = optionalAlbum.get();

        try {
            entityUpdater.update(albumDTO, toUpdate);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new EntityUpdaterException("[Update error] Caused by: " + e.getClass().getSimpleName() +
                    " Exception message: " + e.getMessage());
        }

        Album updatedAlbum = albumRepository.save(toUpdate);
        AlbumResponseDTO responseDTO = modelMapper.map(updatedAlbum, AlbumResponseDTO.class);
        Set<ArtistSimpleDTO> artistDTOs = toUpdate.getArtists().stream()
                .map(artist -> modelMapper.map(artist, ArtistSimpleDTO.class))
                .collect(Collectors.toSet());
        responseDTO.setArtists(artistDTOs);

        return responseDTO;
    }

    @Override
    public void addArtistToAlbum(Long albumId, Artist artist) {
        Optional<Album> optionalAlbum = albumRepository.findById(albumId);
        if (optionalAlbum.isEmpty()) throw new IllegalArgumentException("[Update error] Album with id " + albumId + " not found");

        Album album = optionalAlbum.get();
        album.addArtist(artist);
        albumRepository.saveAndFlush(album);
    }

    @Override
    public void removeArtistFromAlbum(Long albumId, Artist artist) {
        Optional<Album> optionalAlbum = albumRepository.findById(albumId);
        if (optionalAlbum.isEmpty()) throw new IllegalArgumentException("[Update error] Album with id " + albumId + " not found");

        Album album = optionalAlbum.get();
        album.removeArtist(artist);
        albumRepository.saveAndFlush(album);
    }

    @Override
    public void addSongToAlbum(Long albumId, Song song) {
        Optional<Album> optionalAlbum = albumRepository.findById(albumId);
        if (optionalAlbum.isEmpty()) throw new IllegalArgumentException("[Update error] Album with id " + albumId + " not found");

        Album album = optionalAlbum.get();
        album.addSong(song);
        song.setAlbum(album);
        albumRepository.saveAndFlush(album);
        songRepo.saveAndFlush(song);
    }

    @Override
    public void removeSongFromAlbum(Long albumId, Song song) {
        Optional<Album> optionalAlbum = albumRepository.findById(albumId);
        if (optionalAlbum.isEmpty()) throw new IllegalArgumentException("[Update error] Album with id " + albumId + " not found");

        Album album = optionalAlbum.get();
        album.removeSong(song);
        song.setAlbum(null);
        albumRepository.saveAndFlush(album);
        songRepo.saveAndFlush(song);
    }
}
