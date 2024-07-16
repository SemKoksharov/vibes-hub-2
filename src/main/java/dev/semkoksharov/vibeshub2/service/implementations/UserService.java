package dev.semkoksharov.vibeshub2.service.implementations;

import dev.semkoksharov.vibeshub2.dto.advertisement.AdSimpleDTO;
import dev.semkoksharov.vibeshub2.dto.album.AlbumSimpleDTO;
import dev.semkoksharov.vibeshub2.dto.song.SongSimpleDTO;
import dev.semkoksharov.vibeshub2.dto.user.*;
import dev.semkoksharov.vibeshub2.exceptions.EmptyResultException;
import dev.semkoksharov.vibeshub2.exceptions.EntityUpdaterException;
import dev.semkoksharov.vibeshub2.model.*;
import dev.semkoksharov.vibeshub2.model.base.RoleDetails;
import dev.semkoksharov.vibeshub2.model.enums.UserRoles;
import dev.semkoksharov.vibeshub2.repository.AdvertiserDetailsRepo;
import dev.semkoksharov.vibeshub2.repository.ArtistDetailsRepo;
import dev.semkoksharov.vibeshub2.repository.UserRepo;
import dev.semkoksharov.vibeshub2.service.interfaces.UserServiceInt;
import dev.semkoksharov.vibeshub2.utils.EntityUpdater;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServiceInt {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final ArtistDetailsRepo artistDetailsRepo;
    private final AdvertiserDetailsRepo advertiserDetailsRepo;
    private final EntityUpdater entityUpdater;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, ModelMapper modelMapper, ArtistDetailsRepo artistDetailsRepo, AdvertiserDetailsRepo advertiserDetailsRepo, EntityUpdater entityUpdater) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.artistDetailsRepo = artistDetailsRepo;
        this.advertiserDetailsRepo = advertiserDetailsRepo;
        this.entityUpdater = entityUpdater;
    }

    @Override
    public UserResponseDTO saveUser(UserRegistrationDTO user) {
        UserEntity newUser = modelMapper.map(user, UserEntity.class);
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.saveAndFlush(newUser);
        return modelMapper.map(newUser, UserResponseDTO.class);
    }

    @Override
    public UserEntity updateUser(Long userID, UserRegistrationDTO user) {
        throw new UnsupportedOperationException("Update user functionality not implemented yet");
    }

    @Transactional
    @Override
    public UserResponseDTO createArtistIfUserHasRole(ArtistDTO artistDTO) {
        UserEntity user = userRepo.findById(artistDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("[Create error] User with id " + artistDTO.getId() + " not found"));

        if (user.getUserRoles().contains(UserRoles.ARTIST)) {
            Artist artist = modelMapper.map(artistDTO, Artist.class);
            artist.setUser(user);
            artist.setAlbums(Set.of());
            artist.setSongs(Set.of());
            var saved = artistDetailsRepo.saveAndFlush(artist);
            var dto = modelMapper.map(user, UserWithRoleDetailsDTO.class);
            dto.setRoleDetails(mapToArtistDTO(saved));

            return dto;
        }

        throw new IllegalArgumentException("[Create error] User with id " + artistDTO.getId() + " does not have ARTIST role");
    }

    @Transactional
    @Override
    public UserResponseDTO createAdvertiserIfUserHasRole(AdvertiserDTO advertiserDTO) {
        UserEntity user = userRepo.findById(advertiserDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("[Create error] User with id " + advertiserDTO.getUserId() + " not found"));

        if (user.getUserRoles().contains(UserRoles.ADVERTISER)) {
            Advertiser advertiser = modelMapper.map(advertiserDTO, Advertiser.class);
            advertiser.setUser(user);
            advertiser.setAds(Set.of());
            var saved = advertiserDetailsRepo.saveAndFlush(advertiser);
            var dto = modelMapper.map(user, UserWithRoleDetailsDTO.class);
            dto.setRoleDetails(mapToAdvertiserDTO(advertiser));

            return dto;
        }
        throw new IllegalArgumentException("[Create error] User with id " + advertiserDTO.getUserId() + " does not have ADVERTISER role");
    }

    @Override
    public void deleteUserDeteils(Long userID) {
        UserEntity user = userRepo.findById(userID)
                .orElseThrow(() -> new IllegalArgumentException("[Create error] User with id " + userID + " not found"));

        RoleDetails detailsToDelete = findRoleDetails(user)
                .orElseThrow(() -> new EntityNotFoundException("Role details for user '" + user.getUsername() +
                        "' were not found"));

        if (detailsToDelete instanceof Artist) artistDetailsRepo.delete(((Artist) detailsToDelete));
        if (detailsToDelete instanceof Advertiser) advertiserDetailsRepo.delete(((Advertiser) detailsToDelete));
    }

    @Override
    public void updateRoleDetails(Long userID, ArtistDTO artistDTO) {
        UserEntity user = userRepo.findById(userID)
                .orElseThrow(() -> new IllegalArgumentException("[Update error] User with id " + userID + " is not found"));

        RoleDetails detailsToUpdate = findRoleDetails(user)
                .orElseThrow(() -> new EntityNotFoundException("[Update error] Role details for user '" + user.getUsername() +
                        "' were not found"));

        if (!(detailsToUpdate instanceof Artist))
            throw new EntityUpdaterException("[Update error] The user role details should be 'Artist'");

        try {
            entityUpdater.update(artistDTO, detailsToUpdate);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new EntityUpdaterException("[Update error] Caused by: " + e.getClass().getSimpleName() +
                    " Exception message: " + e.getMessage());
        }
    }
    @Override
    public void updateRoleDetails(Long userID, AdvertiserDTO advertiserDTO) {
        UserEntity user = userRepo.findById(userID)
                .orElseThrow(() -> new IllegalArgumentException("[Update error] User with id " + userID + " is not found"));

        RoleDetails detailsToUpdate = findRoleDetails(user)
                .orElseThrow(() -> new EntityNotFoundException("[Update error] Role details for user '" + user.getUsername() +
                        "' were not found in the database"));

        if (!(detailsToUpdate instanceof Advertiser))
            throw new EntityUpdaterException("[Update error] The user role details should be 'Artist'");

        try {
            entityUpdater.update(advertiserDTO, detailsToUpdate);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new EntityUpdaterException("[Update error] Caused by: " + e.getClass().getSimpleName() +
                    " Exception message: " + e.getMessage());
        }
    }

    @Override
    public List<UserResponseDTO> findAllUsers() {
        List<UserResponseDTO> allUsers = userRepo.findAll().stream().map(this::getUserResponseDTO
        ).toList();
        if (allUsers.isEmpty()) throw new EmptyResultException("[Service message] Users were not found in the database");

        return allUsers;
    }


    @Override
    public UserResponseDTO findUserById(Long userID) {
        UserEntity user = userRepo.findById(userID)
                .orElseThrow(() -> new IllegalArgumentException("[Get error] User with id " + userID + " is not found"));

        return getUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO findUserByUsername(String username) {
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("[Delete error] User with username '" + username + "' is not found"));

        return getUserResponseDTO(user);
    }

    @Override
    public void deleteUserById(Long userID) {
        UserEntity user = userRepo.findById(userID)
                .orElseThrow(() -> new IllegalArgumentException("[Delete error] User with id " + userID + " is not found"));

        RoleDetails roleDetails = findRoleDetails(user).orElse(null);
        if (roleDetails instanceof Artist) artistDetailsRepo.delete((Artist) roleDetails);
        if (roleDetails instanceof Advertiser) advertiserDetailsRepo.delete((Advertiser) roleDetails);

        userRepo.deleteById(userID);
    }

    @Override
    public void deleteAllUsers() {
        userRepo.deleteAll();
    }

    private Optional<RoleDetails> findRoleDetails(UserEntity userEntity) {
        if (userEntity.getUserRoles().contains(UserRoles.ARTIST)) {
            return artistDetailsRepo.findByUser_Id(userEntity.getId());
        } else if (userEntity.getUserRoles().contains(UserRoles.ADVERTISER)) {
            return advertiserDetailsRepo.findByUser_Id(userEntity.getId());
        }
        return Optional.empty();
    }

    private ArtistResponseDTO mapToArtistDTO(Artist artist) {
        ArtistResponseDTO dto = modelMapper.map(artist, ArtistResponseDTO.class);
        Set<AlbumSimpleDTO> albumDTOs = artist.getAlbums().stream()
                .map(album -> modelMapper.map(album, AlbumSimpleDTO.class))
                .collect(Collectors.toSet());
        dto.setAlbums(albumDTOs);

        Set<SongSimpleDTO> songDTOs = artist.getSongs().stream()
                .map(song -> modelMapper.map(song, SongSimpleDTO.class))
                .collect(Collectors.toSet());
        dto.setSongs(songDTOs);

        return dto;
    }

    private AdvertiserResponseDTO mapToAdvertiserDTO(Advertiser advertiser){
        AdvertiserResponseDTO dto = modelMapper.map(advertiser, AdvertiserResponseDTO.class);
        Set<AdSimpleDTO> adDTOs =  advertiser.getAds().stream().map(ad -> modelMapper.map(ad, AdSimpleDTO.class)).collect(Collectors.toSet());
        dto.setAds(adDTOs);

        return dto;
    }

    private UserResponseDTO getUserResponseDTO(UserEntity user) {
        Optional<RoleDetails> optionalRoleDetails = findRoleDetails(user);
        if (optionalRoleDetails.isEmpty()) return modelMapper.map(user, UserResponseDTO.class);

        RoleDetails roleDetails = optionalRoleDetails.get();
        UserWithRoleDetailsDTO dto = modelMapper.map(user, UserWithRoleDetailsDTO.class);
        dto.setRoleDetails(optionalRoleDetails);
        if (roleDetails instanceof Artist) dto.setRoleDetails(mapToArtistDTO((Artist) roleDetails));

        return dto;
    }
}
