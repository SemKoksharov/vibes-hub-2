package dev.semkoksharov.vibeshub2.users;

import dev.semkoksharov.vibeshub2.users.dto.UserRegistrationDTO;
import dev.semkoksharov.vibeshub2.users.dto.UserResponseDTO;
import dev.semkoksharov.vibeshub2.users.entity.UserEntity;

public interface UserServiceInt {

    UserResponseDTO saveUser(UserRegistrationDTO user);

    UserEntity updateUser(Long userID, UserRegistrationDTO user);

    UserResponseDTO findUserById(Long userID);

    UserResponseDTO findUserByUsername(String username);

    boolean deleteUserById(Long userID);

    void deleteAllUsers();
}
