package dev.semkoksharov.vibeshub2.controllers;

import dev.semkoksharov.vibeshub2.service.UserService;
import dev.semkoksharov.vibeshub2.dto.user.ArtistDTO;
import dev.semkoksharov.vibeshub2.dto.user.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userID}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userID){
        UserResponseDTO user = userService.findUserById(userID);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/details")
    public ResponseEntity<UserResponseDTO> createDetsils(@RequestBody ArtistDTO artistDTO){

        UserResponseDTO user = userService.createArtistIfUserHasRole(artistDTO);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
