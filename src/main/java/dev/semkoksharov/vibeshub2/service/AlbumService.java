package dev.semkoksharov.vibeshub2.service;

import dev.semkoksharov.vibeshub2.dto.AlbumDTO;
import dev.semkoksharov.vibeshub2.dto.AlbumResponseDTO;
import dev.semkoksharov.vibeshub2.dto.ArtistSimpleDTO;
import dev.semkoksharov.vibeshub2.model.Album;
import dev.semkoksharov.vibeshub2.model.Artist;
import dev.semkoksharov.vibeshub2.repository.AlbumRepo;
import dev.semkoksharov.vibeshub2.repository.ArtistDetailsRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    private final AlbumRepo albumRepository;
    private final ArtistDetailsRepo artistRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public AlbumService(AlbumRepo albumRepository, ArtistDetailsRepo artistRepo, ModelMapper modelMapper) {
        this.albumRepository = albumRepository;
        this.artistRepo = artistRepo;
        this.modelMapper = modelMapper;
    }

    public AlbumResponseDTO createAlbum(AlbumDTO albumDTO) {
        Album album = modelMapper.map(albumDTO, Album.class);

        Set<Artist> artists = new HashSet<>(artistRepo.findAllById(albumDTO.getArtistIds()));
        album.setArtists(artists);

        Album savedAlbum = albumRepository.save(album);

        // Проходим по каждому артисту и добавляем созданный альбом в их список альбомов
        artists.forEach(artist -> artist.getAlbums().add(savedAlbum));
        artistRepo.saveAll(artists);

        AlbumResponseDTO responseDTO = modelMapper.map(savedAlbum, AlbumResponseDTO.class);
        Set<ArtistSimpleDTO> artistDTOs = artists.stream()
                .map(artist -> modelMapper.map(artist, ArtistSimpleDTO.class))
                .collect(Collectors.toSet());
        responseDTO.setArtists(artistDTOs);

        return responseDTO;
    }

    public AlbumResponseDTO getAlbumById(Long id) {
        Optional<Album> albumOptional = albumRepository.findById(id);
        if (albumOptional.isPresent()) {
            Album album = albumOptional.get();
            AlbumResponseDTO responseDTO = modelMapper.map(album, AlbumResponseDTO.class);
            Set<ArtistSimpleDTO> artistDTOs = album.getArtists().stream()
                    .map(artist -> modelMapper.map(artist, ArtistSimpleDTO.class))
                    .collect(Collectors.toSet());
            responseDTO.setArtists(artistDTOs);
            return responseDTO;
        } else {
            return null;
        }
    }

    public List<AlbumResponseDTO> getAllAlbums() {
        List<Album> albums = albumRepository.findAll();
        return albums.stream().map(album -> {
            AlbumResponseDTO responseDTO = modelMapper.map(album, AlbumResponseDTO.class);
            Set<ArtistSimpleDTO> artistDTOs = album.getArtists().stream()
                    .map(artist -> modelMapper.map(artist, ArtistSimpleDTO.class))
                    .collect(Collectors.toSet());
            responseDTO.setArtists(artistDTOs);
            return responseDTO;
        }).collect(Collectors.toList());
    }

    public void deleteAlbumById(Long id) {
        albumRepository.deleteById(id);
    }

    public AlbumResponseDTO updateAlbum(Long id, AlbumDTO albumDTO) {
        Optional<Album> optionalAlbum = albumRepository.findById(id);
        if (optionalAlbum.isEmpty()) {
            throw new IllegalArgumentException("Album not found");
        }

        Album album = optionalAlbum.get();
        // Update fields
        album.setTitle(albumDTO.getTitle());
        album.setYear(albumDTO.getYear());

        Album updatedAlbum = albumRepository.save(album);
        AlbumResponseDTO responseDTO = modelMapper.map(updatedAlbum, AlbumResponseDTO.class);
        Set<ArtistSimpleDTO> artistDTOs = album.getArtists().stream()
                .map(artist -> modelMapper.map(artist, ArtistSimpleDTO.class))
                .collect(Collectors.toSet());
        responseDTO.setArtists(artistDTOs);

        return responseDTO;
    }

    public void addArtistToAlbum(Long albumId, Artist artist) {
        Optional<Album> optionalAlbum = albumRepository.findById(albumId);
        if (optionalAlbum.isPresent()) {
            Album album = optionalAlbum.get();
            album.addArtist(artist);
            albumRepository.save(album);
        }
    }

    public void removeArtistFromAlbum(Long albumId, Artist artist) {
        Optional<Album> optionalAlbum = albumRepository.findById(albumId);
        if (optionalAlbum.isPresent()) {
            Album album = optionalAlbum.get();
            album.removeArtist(artist);
            albumRepository.save(album);
        }
    }
}
