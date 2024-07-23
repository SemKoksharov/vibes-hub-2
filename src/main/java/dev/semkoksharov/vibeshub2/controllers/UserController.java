package dev.semkoksharov.vibeshub2.controllers;

import dev.semkoksharov.vibeshub2.dto.user.ArtistDTO;
import dev.semkoksharov.vibeshub2.service.implementations.UserServiceImpl;
import dev.semkoksharov.vibeshub2.dto.user.UserResponseDTO;
import dev.semkoksharov.vibeshub2.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/{userID}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userID) {
        UserResponseDTO user = userService.findUserById(userID);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.findAllUsers();

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PostMapping("/artistDetails")
    public ResponseEntity<UserResponseDTO> createArtistDetails(@RequestBody ArtistDTO artistDTO) {
        UserResponseDTO user = userService.createArtistIfUserHasRole(artistDTO);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id){
        userService.deleteUserById(id);

        return ResponseEntity.ok("User with id " + id + " has been deleted.");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllUsers(){
        userService.deleteAllUsers();

        return ResponseEntity.ok("All users have been deleted.");
    }

}
