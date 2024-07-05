package dev.semkoksharov.vibeshub2.users.entity;

import dev.semkoksharov.vibeshub2.models.BaseEntity;
import dev.semkoksharov.vibeshub2.roles.UserRoles;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Id
    private Long id;
    private String name;
    private String surname;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;
    private String password;

    @Column(unique = true)
    private String telNumber;

    private String urlPhoto;

    private String photoMinioName;

    @Enumerated(value = EnumType.STRING)
    @ElementCollection(targetClass = UserRoles.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<UserRoles> userRoles;

    private String country;

    public UserEntity(String name, String surname, String username, String email, String password, String telNumber, Set<UserRoles> userRoles, String country) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.telNumber = telNumber;
        this.userRoles = userRoles;
        this.country = country;
    }

    public UserEntity() {
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

    public String getPhotoMinioName() {
        return photoMinioName;
    }

    public void setPhotoMinioName(String photoMinioName) {
        this.photoMinioName = photoMinioName;
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
}
