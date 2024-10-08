package dev.semkoksharov.vibeshub2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.semkoksharov.vibeshub2.interfaces.Uploadable;
import dev.semkoksharov.vibeshub2.model.base.BaseEntity;
import dev.semkoksharov.vibeshub2.model.base.RoleDetails;
import dev.semkoksharov.vibeshub2.model.enums.UserRoles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity implements UserDetails, Uploadable {

    private String name;
    private String surname;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    @Column(unique = true)
    private String telNumber;

    @JsonIgnore
    private String minioPath;
    private String directUrl;

    @Enumerated(value = EnumType.STRING)
    @ElementCollection(targetClass = UserRoles.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<UserRoles> userRoles;

    private String country;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private Set<Playlist> playlists;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public void addPlaylistToUser(Playlist playlist){
        this.playlists.add(playlist);
        playlist.setUser(this);
    }

    public void deletePlaylistFromUser(Playlist playlist){
        this.playlists.remove(playlist);
        playlist.setUser(null);
    }

}
