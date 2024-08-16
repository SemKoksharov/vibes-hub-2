package dev.semkoksharov.vibeshub2.controllers;

import dev.semkoksharov.vibeshub2.dto.forms.BaseResponseForm;
import dev.semkoksharov.vibeshub2.dto.forms.ResponseForm;
import dev.semkoksharov.vibeshub2.dto.user.ArtistDTO;
import dev.semkoksharov.vibeshub2.service.implementations.UserServiceImpl;
import dev.semkoksharov.vibeshub2.dto.user.UserResponseDTO;
import dev.semkoksharov.vibeshub2.service.interfaces.UserService;
import dev.semkoksharov.vibeshub2.utils.DateTimeUtil;
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
    public ResponseEntity<BaseResponseForm> getUserById(@PathVariable Long userID) {
        UserResponseDTO user = userService.findUserById(userID);

        BaseResponseForm userWasFound = new ResponseForm(
                HttpStatus.OK.toString(),
                "The user was found",
                DateTimeUtil.getFormattedTimestamp(),
                user
        );

        return ResponseEntity.ok(userWasFound);
    }

    @GetMapping
    public ResponseEntity<BaseResponseForm> getAllUsers() {
        List<UserResponseDTO> users = userService.findAllUsers();

        BaseResponseForm usersWereFound = new ResponseForm(
                HttpStatus.OK.toString(),
                "The users were found",
                DateTimeUtil.getFormattedTimestamp(),
                users
        );

        return ResponseEntity.ok(usersWereFound);
    }

    @PostMapping("/artistDetails")
    public ResponseEntity<BaseResponseForm> createArtistDetails(@RequestBody ArtistDTO artistDTO) {
        UserResponseDTO user = userService.createArtistIfUserHasRole(artistDTO);

        BaseResponseForm artistDetailsCreated = new ResponseForm(
                HttpStatus.OK.toString(),
                "Artist details were created",
                DateTimeUtil.getFormattedTimestamp(),
                user
        );

        return ResponseEntity.status(HttpStatus.OK).body(artistDetailsCreated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<BaseResponseForm> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);

        BaseResponseForm userWasDeleted = new ResponseForm(
                HttpStatus.OK.toString(),
                "User with id " + id + " has been deleted.",
                DateTimeUtil.getFormattedTimestamp()
        );

        return ResponseEntity.ok(userWasDeleted);
    }

    @DeleteMapping
    public ResponseEntity<BaseResponseForm> deleteAllUsers() {
        userService.deleteAllUsers();

        BaseResponseForm allUsersWereDeleted = new ResponseForm(
                HttpStatus.OK.toString(),
                "All users have been deleted.",
                DateTimeUtil.getFormattedTimestamp()
        );

        return ResponseEntity.ok(allUsersWereDeleted);
    }
}
