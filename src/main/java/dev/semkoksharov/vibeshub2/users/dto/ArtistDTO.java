package dev.semkoksharov.vibeshub2.users.dto;

import dev.semkoksharov.vibeshub2.roles.UserRoles;

import java.util.Set;

public class ArtistDTO {
    private Long id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;
    private String telNumber;
    private String urlPhoto;
    private Set<UserRoles> userRoles;
    private String country;

    private String artistName;
    private String description;
}
