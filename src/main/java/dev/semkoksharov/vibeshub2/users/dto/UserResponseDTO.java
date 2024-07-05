package dev.semkoksharov.vibeshub2.users.dto;

import dev.semkoksharov.vibeshub2.roles.UserRoles;

import java.util.Set;

public class UserResponseDTO {

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

    public UserResponseDTO() {
    }

    public UserResponseDTO(Long id, String name, String surname, String username, String email, String password, String telNumber, String urlPhoto, Set<UserRoles> userRoles, String country) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.telNumber = telNumber;
        this.urlPhoto = urlPhoto;
        this.userRoles = userRoles;
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public Set<UserRoles> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRoles> userRoles) {
        this.userRoles = userRoles;
    }
}
