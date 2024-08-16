package dev.semkoksharov.vibeshub2.controllers;

import dev.semkoksharov.vibeshub2.authentication.JWTService;
import dev.semkoksharov.vibeshub2.authentication.UserDetService;
import dev.semkoksharov.vibeshub2.dto.forms.BaseResponseForm;
import dev.semkoksharov.vibeshub2.dto.forms.LoginForm;
import dev.semkoksharov.vibeshub2.dto.forms.ResponseForm;
import dev.semkoksharov.vibeshub2.dto.user.UserRegistrationDTO;
import dev.semkoksharov.vibeshub2.dto.user.UserResponseDTO;
import dev.semkoksharov.vibeshub2.service.implementations.UserServiceImpl;
import dev.semkoksharov.vibeshub2.service.interfaces.UserService;
import dev.semkoksharov.vibeshub2.utils.DateTimeUtil;
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
    private final UserDetService userDetailsService;

    @Autowired
    public AuthAndRegistrationController(PasswordEncoder passwordEncoder, UserServiceImpl userService, AuthenticationManager authenticationManager, JWTService jwtService, UserDetService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/signup")
    public ResponseEntity<BaseResponseForm> createUser(@RequestBody UserRegistrationDTO user) {
        UserResponseDTO newUser = userService.saveUser(user);

        BaseResponseForm userWasCreated = new ResponseForm(
                HttpStatus.OK.toString(),
                "User was created successfully",
                DateTimeUtil.getFormattedTimestamp(),
                newUser
        );

        return ResponseEntity.status(HttpStatus.OK).body(userWasCreated);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponseForm> loginAndGetToken(@RequestBody LoginForm loginForm) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginForm.username(), loginForm.password())
        );

        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(userDetailsService.loadUserByUsername(loginForm.username()));

            BaseResponseForm loginSuccess = new ResponseForm(
                    HttpStatus.OK.toString(),
                    "Login successful",
                    DateTimeUtil.getFormattedTimestamp(),
                    token
            );

            return ResponseEntity.ok(loginSuccess);
        } else {
            throw new UsernameNotFoundException("Invalid credentials");
        }
    }
}
