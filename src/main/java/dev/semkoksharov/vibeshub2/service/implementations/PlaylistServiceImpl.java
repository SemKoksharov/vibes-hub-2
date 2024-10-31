package dev.semkoksharov.vibeshub2.service.implementations;

import dev.semkoksharov.vibeshub2.dto.playlist.PlaylistRequestDTO;
import dev.semkoksharov.vibeshub2.dto.playlist.PlaylistResponseDTO;
import dev.semkoksharov.vibeshub2.dto.playlist.PlaylistUpdateDTO;
import dev.semkoksharov.vibeshub2.dto.user.UserSimpleDTO;
import dev.semkoksharov.vibeshub2.exceptions.EmptyResultException;
import dev.semkoksharov.vibeshub2.model.Playlist;
import dev.semkoksharov.vibeshub2.model.Song;
import dev.semkoksharov.vibeshub2.model.UserEntity;
import dev.semkoksharov.vibeshub2.repository.PlaylistRepo;
import dev.semkoksharov.vibeshub2.repository.SongRepo;
import dev.semkoksharov.vibeshub2.repository.UserRepo;
import dev.semkoksharov.vibeshub2.service.interfaces.PlaylistService;
import dev.semkoksharov.vibeshub2.utils.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

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
        UserEntity user = playlist.getUser();

        return mapPlaylistToResponseDTO(playlist, user);
    }

    @Override
    public List<PlaylistResponseDTO> getAllPlaylists() {
        List<PlaylistResponseDTO> allPlaylists = playlistRepo.findAll().stream().map(
                playlist -> mapPlaylistToResponseDTO(playlist, playlist.getUser())
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

        if (! securityUtil.isCurrent(user)) {
            throw new AccessDeniedException(
                    "Your account is not authorized for this action."
            );
        }

        Playlist playlist = modelMapper.map(playlistRequestDTO, Playlist.class);

        playlist.setUser(user);
        user.addPlaylistToUser(playlist);
        playlist.setSongs(new LinkedList<>(songsToAdd));

        return mapPlaylistToResponseDTO(playlist, user);
    }

    @NotNull
    private PlaylistResponseDTO mapPlaylistToResponseDTO(Playlist playlist, UserEntity user) {
        PlaylistResponseDTO responseDTO = modelMapper.map(playlist, PlaylistResponseDTO.class);
        UserSimpleDTO userSimpleDTO = modelMapper.map(user, UserSimpleDTO.class);

        responseDTO.setUser(userSimpleDTO);
        return responseDTO;
    }


    // todo TEST THIS METHOD
    @Override
    public PlaylistResponseDTO updatePlaylist(PlaylistUpdateDTO playlistUpdateDTO, Long currentPlaylistId) {

        Playlist toUpdate = playlistRepo.findById(currentPlaylistId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Playlist with ID %d is not found in the database".formatted(currentPlaylistId))
        );

        UserEntity user = toUpdate.getUser();

        if (!securityUtil.isCurrent(user)) {
            throw new AccessDeniedException(
                    "Your account is not authorized for this action"
            );
        }

        String newTitle = playlistUpdateDTO.getTitle();
        List<Song> newSongs = songRepo.findAllById(playlistUpdateDTO.getSongIDs());

        toUpdate.setTitle(newTitle);
        toUpdate.getSongs().clear();
        toUpdate.getSongs().addAll(newSongs);

        playlistRepo.saveAndFlush(toUpdate);

        return mapPlaylistToResponseDTO(toUpdate, user);
    }

    @Override
    public void deletePlaylistById(Long id) {

    }

    @Override
    public void deleteAllPlaylists() {

    }
}
