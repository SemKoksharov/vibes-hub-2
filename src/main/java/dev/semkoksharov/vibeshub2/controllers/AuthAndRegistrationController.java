package dev.semkoksharov.vibeshub2.controllers;


import dev.semkoksharov.vibeshub2.authentication.JWTService;
import dev.semkoksharov.vibeshub2.authentication.UzerDetailsService;
import dev.semkoksharov.vibeshub2.dto.LoginForm;
import dev.semkoksharov.vibeshub2.service.UserService;
import dev.semkoksharov.vibeshub2.dto.user.UserRegistrationDTO;
import dev.semkoksharov.vibeshub2.dto.user.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthAndRegistrationController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UzerDetailsService userDetailsService;
    @Autowired
    public AuthAndRegistrationController(PasswordEncoder passwordEncoder, UserService userService, AuthenticationManager authenticationManager, JWTService jwtService, UzerDetailsService uzerDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = uzerDetailsService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRegistrationDTO user){
        UserResponseDTO newUser =  userService.saveUser(user);

        return ResponseEntity.status(HttpStatus.OK).body(newUser);
    }

    @PostMapping("/login")
    public String loginAndGetToken(@RequestBody LoginForm loginForm){
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginForm.username(), loginForm.password())
        );
        if (authentication.isAuthenticated()){
            return jwtService.generateToken(userDetailsService.loadUserByUsername(loginForm.username()));

        } else throw new UsernameNotFoundException("Invalid credentials");
    }
}
