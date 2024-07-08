package dev.semkoksharov.vibeshub2.dto.user;

public class UserWithRoleDetailsDTO extends UserResponseDTO {
    private Object roleDetails;

    public Object getRoleDetails() {
        return roleDetails;
    }

    public void setRoleDetails(Object roleDetails) {
        this.roleDetails = roleDetails;
    }
}
