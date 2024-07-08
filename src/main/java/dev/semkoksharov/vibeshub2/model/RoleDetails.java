package dev.semkoksharov.vibeshub2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@MappedSuperclass
public abstract class RoleDetails extends BaseEntity{

    @MapsId
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id", nullable = false)
    @JsonIgnore
    private UserEntity user;

    private String description;

    public RoleDetails() {
    }

    public RoleDetails(UserEntity user,String description) {
        this.user = user;
        this.description = description;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
