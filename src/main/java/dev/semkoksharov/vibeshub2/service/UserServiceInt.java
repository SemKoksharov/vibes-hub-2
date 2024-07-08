package dev.semkoksharov.vibeshub2.service;

import dev.semkoksharov.vibeshub2.dto.user.AdvertiserDTO;
import dev.semkoksharov.vibeshub2.dto.user.ArtistDTO;
import dev.semkoksharov.vibeshub2.dto.user.UserRegistrationDTO;
import dev.semkoksharov.vibeshub2.dto.user.UserResponseDTO;
import dev.semkoksharov.vibeshub2.model.UserEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserServiceInt {

    UserResponseDTO saveUser(UserRegistrationDTO user);

    UserEntity updateUser(Long userID, UserRegistrationDTO user);

    @Transactional
    UserResponseDTO createArtistIfUserHasRole(ArtistDTO artistDTO);

    @Transactional
    UserResponseDTO createAdvertiserIfUserHasRole(AdvertiserDTO advertiserDTO);

    List<UserResponseDTO> findAllUsers();

    UserResponseDTO findUserById(Long userID);

    UserResponseDTO findUserByUsername(String username);

    void deleteUserById(Long userID);

    void deleteAllUsers();
}
