package dev.semkoksharov.vibeshub2.dto.user;

import dev.semkoksharov.vibeshub2.model.RoleDetails;
import dev.semkoksharov.vibeshub2.model.UserRoles;

import java.time.LocalDateTime;
import java.util.Set;

public class UserWithRoleDetailsDTO extends UserResponseDTO{

    private RoleDetails roleDetails;

    public UserWithRoleDetailsDTO() {
    }

    public UserWithRoleDetailsDTO(Long id, String name, String surname, String username, String email, String password, String telNumber, String urlPhoto, Set<UserRoles> userRoles, String country, LocalDateTime createdAt, LocalDateTime updatedAt, RoleDetails roleDetails) {
        super(id, name, surname, username, email, password, telNumber, urlPhoto, userRoles, country, createdAt, updatedAt);
        this.roleDetails = roleDetails;
    }

    public RoleDetails getRoleDetails() {
        return roleDetails;
    }

    public void setRoleDetails(RoleDetails roleDetails) {
        this.roleDetails = roleDetails;
    }
}
