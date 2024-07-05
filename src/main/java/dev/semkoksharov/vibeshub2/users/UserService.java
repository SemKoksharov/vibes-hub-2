package dev.semkoksharov.vibeshub2.users;

import dev.semkoksharov.vibeshub2.users.dto.UserRegistrationDTO;
import dev.semkoksharov.vibeshub2.users.dto.UserResponseDTO;
import dev.semkoksharov.vibeshub2.users.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserServiceInt{

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO saveUser(UserRegistrationDTO user) {
        return null;
    }

    @Override
    public UserEntity updateUser(Long userID, UserRegistrationDTO user) {
        return null;
    }

    @Override
    public UserResponseDTO findUserById(Long userID) {
        return null;
    }

    @Override
    public UserResponseDTO findUserByUsername(String username) {
        return null;
    }

    @Override
    public boolean deleteUserById(Long userID) {
        return false;
    }

    @Override
    public void deleteAllUsers() {

    }
}
