package dev.semkoksharov.vibeshub2.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.semkoksharov.vibeshub2.model.UserRoles;

import java.time.LocalDateTime;
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
    @JsonFormat(pattern = "dd-MMM-yyyy HH:mm")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "dd-MMM-yyyy HH:mm")
    private LocalDateTime updatedAt;

    public UserResponseDTO() {
    }

    public UserResponseDTO(Long id, String name, String surname, String username, String email, String password, String telNumber, String urlPhoto, Set<UserRoles> userRoles, String country, LocalDateTime createdAt, LocalDateTime updatedAt) {
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
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}