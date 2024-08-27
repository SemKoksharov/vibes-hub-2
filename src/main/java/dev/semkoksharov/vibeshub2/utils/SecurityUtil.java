package dev.semkoksharov.vibeshub2.utils;

import dev.semkoksharov.vibeshub2.model.UserEntity;
import dev.semkoksharov.vibeshub2.model.enums.UserRoles;
import dev.semkoksharov.vibeshub2.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
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
        String username = authentication.getName();

        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                "[Error] To perform this action you must be authenticated"
        ));
    }

    // methods for check current user///////////////////////////////////////////////////////////////////////

    public boolean isAuthenticated() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.isAuthenticated();
    }

    public boolean isAdmin() {
        return this.getCurrentUser().getUserRoles().contains(UserRoles.ADMIN);
    }

    public boolean isModerator() {
        return this.getCurrentUser().getUserRoles().contains(UserRoles.MODERATOR);
    }

    public boolean isArtist() {
        return this.getCurrentUser().getUserRoles().contains(UserRoles.ARTIST);
    }

    public boolean isAdvertiser() {
        return this.getCurrentUser().getUserRoles().contains(UserRoles.ADVERTISER);
    }

    // overloaded methods for check any user////////////////////////////////////////////////////////////////

    public boolean isCurrent(UserEntity user) {
        return user.equals(this.getCurrentUser());
    }

    public boolean isAdmin(UserEntity user) {
        return user.getUserRoles().contains(UserRoles.ADMIN);
    }

    public boolean isModerator(UserEntity user) {
        return user.getUserRoles().contains(UserRoles.MODERATOR);
    }

    public boolean isArtist(UserEntity user) {
        return user.getUserRoles().contains(UserRoles.ARTIST);
    }

    public boolean isAdvertiser(UserEntity user) {
        return user.getUserRoles().contains(UserRoles.ADVERTISER);
    }

}

