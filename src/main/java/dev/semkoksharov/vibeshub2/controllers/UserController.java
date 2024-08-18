package dev.semkoksharov.vibeshub2.controllers;

import dev.semkoksharov.vibeshub2.dto.forms.BaseResponseForm;
import dev.semkoksharov.vibeshub2.dto.forms.ResponseForm;
import dev.semkoksharov.vibeshub2.dto.user.ArtistDTO;
import dev.semkoksharov.vibeshub2.service.interfaces.UserService;
import dev.semkoksharov.vibeshub2.dto.user.UserResponseDTO;
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
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //todo update user
    // update role details

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseForm> getUserById(@PathVariable Long id) {
        UserResponseDTO user = userService.findUserById(id);

        BaseResponseForm response = new ResponseForm(
                HttpStatus.OK.toString(),
                "User found successfully.",
                DateTimeUtil.getFormattedTimestamp(),
                user
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<BaseResponseForm> getAllUsers() {
        List<UserResponseDTO> users = userService.findAllUsers();

        BaseResponseForm response = new ResponseForm(
                HttpStatus.OK.toString(),
                users.size() + " user(s) found successfully.",
                DateTimeUtil.getFormattedTimestamp(),
                users
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/artistDetails")
    public ResponseEntity<BaseResponseForm> createArtistDetails(@RequestBody ArtistDTO artistDTO) {
        UserResponseDTO user = userService.createArtistIfUserHasRole(artistDTO);

        BaseResponseForm response = new ResponseForm(
                HttpStatus.OK.toString(),
                "Artist details created successfully.",
                DateTimeUtil.getFormattedTimestamp(),
                user
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponseForm> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);

        BaseResponseForm response = new ResponseForm(
                HttpStatus.OK.toString(),
                "User with ID " + id + " has been deleted successfully.",
                DateTimeUtil.getFormattedTimestamp()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<BaseResponseForm> deleteAllUsers() {
        userService.deleteAllUsers();

        BaseResponseForm response = new ResponseForm(
                HttpStatus.OK.toString(),
                "All users have been deleted successfully.",
                DateTimeUtil.getFormattedTimestamp()
        );

        return ResponseEntity.ok(response);
    }
}
