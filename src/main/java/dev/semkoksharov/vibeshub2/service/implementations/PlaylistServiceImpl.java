package dev.semkoksharov.vibeshub2.service.implementations;

import dev.semkoksharov.vibeshub2.dto.playlist.PlaylistRequestDTO;
import dev.semkoksharov.vibeshub2.dto.playlist.PlaylistResponseDTO;
import dev.semkoksharov.vibeshub2.dto.user.UserSimpleDTO;
import dev.semkoksharov.vibeshub2.exceptions.EmptyResultException;
import dev.semkoksharov.vibeshub2.model.Playlist;
import dev.semkoksharov.vibeshub2.model.Song;
import dev.semkoksharov.vibeshub2.model.UserEntity;
import dev.semkoksharov.vibeshub2.model.enums.UserRoles;
import dev.semkoksharov.vibeshub2.repository.PlaylistRepo;
import dev.semkoksharov.vibeshub2.repository.SongRepo;
import dev.semkoksharov.vibeshub2.repository.UserRepo;
import dev.semkoksharov.vibeshub2.service.interfaces.PlaylistService;
import dev.semkoksharov.vibeshub2.utils.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Lob;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaylistServiceImpl implements PlaylistService {


    private final PlaylistRepo playlistRepo;
    private final ModelMapper modelMapper;
    private final SecurityUtil securityUtil;
    private final UserRepo userRepo;
    private final SongRepo songRepo;

    @Autowired
    public PlaylistServiceImpl(PlaylistRepo playlistRepo, ModelMapper modelMapper, SecurityUtil securityUtil, UserRepo userRepo, SongRepo songRepo) {
        this.playlistRepo = playlistRepo;
        this.modelMapper = modelMapper;
        this.securityUtil = securityUtil;
        this.userRepo = userRepo;
        this.songRepo = songRepo;
    }

    @Override
    public PlaylistResponseDTO getPlaylistById(Long id) {
        Playlist playlist = playlistRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Playlist with id %d is not found in the database".formatted(id))
        );

        return modelMapper.map(playlist, PlaylistResponseDTO.class);
    }

    @Override
    public List<PlaylistResponseDTO> getAllPlaylists() {
        List<PlaylistResponseDTO> allPlaylists = playlistRepo.findAll().stream().map(
                playlist -> modelMapper.map(playlist, PlaylistResponseDTO.class)
        ).toList();

        if (allPlaylists.isEmpty()) throw new EmptyResultException("No playlists found in the database");

        return allPlaylists;
    }

    @Override
    public PlaylistResponseDTO createPlaylist(PlaylistRequestDTO playlistRequestDTO) {

        Long userID = playlistRequestDTO.getUserID();

        List<Song> songsToAdd = songRepo.findAllById(playlistRequestDTO.getSongIDs());

        UserEntity user = userRepo.findById(userID).orElseThrow(
                () -> new EntityNotFoundException("User with ID %d is not found".formatted(userID)));

        if (!securityUtil.isCurrent(user)) {
            throw new AccessDeniedException(
                    "Your account is not authorized for this action, "
            );
        }
        Playlist playlist = modelMapper.map(playlistRequestDTO, Playlist.class);

        playlist.setUser(user);
        user.addPlaylistToUser(playlist);
        playlist.setSongs(new HashSet<>(songsToAdd));

        // todo extract method

        return mapPlaylistToResponseDTO(playlist, user);
    }

    @NotNull
    private PlaylistResponseDTO mapPlaylistToResponseDTO(Playlist playlist, UserEntity user) {
        PlaylistResponseDTO responseDTO = modelMapper.map(playlist, PlaylistResponseDTO.class);
        UserSimpleDTO userSimpleDTO = modelMapper.map(user, UserSimpleDTO.class);

        responseDTO.setUser(userSimpleDTO);
        return responseDTO;
    }

    @Override
    public PlaylistResponseDTO updatePlaylist(PlaylistRequestDTO playlistRequestDTO) {
        return null;
    }

    @Override
    public void deletePlaylistById(Long id) {

    }

    @Override
    public void deleteAllPlaylists() {

    }
}
