package dev.semkoksharov.vibeshub2.utils;

import dev.semkoksharov.vibeshub2.model.UserEntity;
import dev.semkoksharov.vibeshub2.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    private final UserRepo userRepository;

    @Autowired
    public SecurityUtil(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        return userRepository.findByUsername(name).orElseThrow(() -> new UsernameNotFoundException("[Error] Current user not found"));
    }
}

