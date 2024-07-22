package dev.semkoksharov.vibeshub2.service.interfaces;

import dev.semkoksharov.vibeshub2.dto.user.*;
import dev.semkoksharov.vibeshub2.model.UserEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {


    UserResponseDTO saveUser(UserRegistrationDTO user);

    UserEntity updateUser(Long userID, UserRegistrationDTO user);

    @Transactional
    UserResponseDTO createArtistIfUserHasRole(ArtistDTO artistDTO);

    @Transactional
    UserResponseDTO createAdvertiserIfUserHasRole(AdvertiserDTO advertiserDTO);

    void deleteUserDeteils(Long userID);

    void updateRoleDetails(Long userID, ArtistDTO artistDTO);

    void updateRoleDetails(Long userID, AdvertiserDTO advertiserDTO);

    List<UserResponseDTO> findAllUsers();

    UserResponseDTO findUserById(Long userID);

    UserResponseDTO findUserByUsername(String username);

    void deleteUserById(Long userID);

    void deleteAllUsers();
}
