package dev.semkoksharov.vibeshub2.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithRoleDetailsDTO extends UserResponseDTO {
    private Object roleDetails;

}
