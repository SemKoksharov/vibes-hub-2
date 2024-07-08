package dev.semkoksharov.vibeshub2.service;

import dev.semkoksharov.vibeshub2.dto.user.*;
import dev.semkoksharov.vibeshub2.model.*;
import dev.semkoksharov.vibeshub2.repository.AdvertiserDetailsRepo;
import dev.semkoksharov.vibeshub2.repository.ArtistDetailsRepo;
import dev.semkoksharov.vibeshub2.repository.UserRepo;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserServiceInt {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final ArtistDetailsRepo artistDetailsRepo;
    private final AdvertiserDetailsRepo advertiserDetailsRepo;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, ModelMapper modelMapper, ArtistDetailsRepo artistDetailsRepo, AdvertiserDetailsRepo advertiserDetailsRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.artistDetailsRepo = artistDetailsRepo;
        this.advertiserDetailsRepo = advertiserDetailsRepo;
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
        return null;
    }

    @Transactional
    @Override
    public UserResponseDTO createArtistIfUserHasRole(ArtistDTO artistDTO) {
        UserEntity user = userRepo.findById(artistDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getUserRoles().contains(UserRoles.ARTIST)) {
            Artist artist = new Artist(user, artistDTO.getDescription(), artistDTO.getArtistName(), Set.of(), Set.of());
            var saved = artistDetailsRepo.save(artist);
            var dto = modelMapper.map(user, UserWithRoleDetailsDTO.class);
            dto.setRoleDetails(saved);

            return dto;
        }

        throw new IllegalArgumentException("User does not have ARTIST role");
    }

    @Transactional
    @Override
    public UserResponseDTO createAdvertiserIfUserHasRole(AdvertiserDTO advertiserDTO) {
        UserEntity user = userRepo.findById(advertiserDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getUserRoles().contains(UserRoles.ADVERTISER)) {
            Advertiser advertiser = new Advertiser(user, advertiserDTO.getDescription(), advertiserDTO.getCompany(), advertiserDTO.getTaxCode(), Set.of());
            var saved  = advertiserDetailsRepo.save(advertiser);
            var dto = modelMapper.map(user, UserWithRoleDetailsDTO.class);
            dto.setRoleDetails(saved);

            return dto;
        }

        throw new IllegalArgumentException("User does not have ADVERTISER role");
    }

    @Override
    public List<UserResponseDTO> findAllUsers() {
        return userRepo.findAll().stream().map(user -> {
            RoleDetails roleDetails = findRoleDetails(user);

            if (roleDetails == null) {
                return modelMapper.map(user, UserResponseDTO.class);
            }
            UserWithRoleDetailsDTO dto = modelMapper.map(user, UserWithRoleDetailsDTO.class);
            dto.setRoleDetails(roleDetails);
            return dto;

        }).toList();
    }


    @Override
    public UserResponseDTO findUserById(Long userID) {
        UserEntity user = userRepo.findById(userID).orElseThrow(() -> new RuntimeException("User not found"));
        RoleDetails roleDetails = findRoleDetails(user);

        if (roleDetails == null) {
            return modelMapper.map(user, UserResponseDTO.class);
        }
        UserWithRoleDetailsDTO dto = modelMapper.map(user, UserWithRoleDetailsDTO.class);
        dto.setRoleDetails(roleDetails);
        return dto;
    }

    @Override
    public UserResponseDTO findUserByUsername(String username) {

        return null;
    }

    @Override
    public void deleteUserById(Long userID) {
        UserEntity user = userRepo.findById(userID).orElseThrow(() -> new RuntimeException("User not found"));

        RoleDetails roleDetails = findRoleDetails(user);
        if (roleDetails instanceof Artist) {
            artistDetailsRepo.delete((Artist) roleDetails);
        }
        if (roleDetails instanceof Advertiser) {
            advertiserDetailsRepo.delete((Advertiser) roleDetails);
        }
        userRepo.deleteById(userID);
    }

    @Override
    public void deleteAllUsers() {
        userRepo.deleteAll();
    }



    private RoleDetails findRoleDetails(UserEntity userEntity) {

        if (userEntity.getUserRoles().contains(UserRoles.ARTIST)) {
            return artistDetailsRepo.findByUser_Id(userEntity.getId()).orElse(null);

        } else if (userEntity.getUserRoles().contains(UserRoles.ADVERTISER)) {
            return advertiserDetailsRepo.findById(userEntity.getId()).orElse(null);
        } else return null;
    }

}
