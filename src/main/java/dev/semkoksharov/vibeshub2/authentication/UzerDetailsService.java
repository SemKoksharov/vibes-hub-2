package dev.semkoksharov.vibeshub2.authentication;

import dev.semkoksharov.vibeshub2.repository.UserRepo;
import dev.semkoksharov.vibeshub2.model.UserEntity;
import dev.semkoksharov.vibeshub2.model.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UzerDetailsService implements UserDetailsService {

    private final UserRepo uzerRepo;

    @Autowired
    public UzerDetailsService(UserRepo uzerRepo) {
        this.uzerRepo = uzerRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userOptional = uzerRepo.findByUsername(username);
        if (userOptional.isPresent()) {
            var uzer = userOptional.get();
            return User.builder()
                    .username(uzer.getUsername())
                    .password(uzer.getPassword())
                    .roles(getRoles(uzer))
                    .build();
        } else {
            throw new UsernameNotFoundException("User " +
                    username + " not found");
        }
    }
    private String getRoles(UserEntity user){
        if (user.getUserRoles() == null){
            return UserRoles.USER.toString();
        }
        return user.getUserRoles().stream().map(Enum::toString).collect(Collectors.joining(","));
    }



}
