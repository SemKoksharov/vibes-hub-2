package dev.semkoksharov.vibeshub2.dto.user;

import dev.semkoksharov.vibeshub2.model.enums.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDTO {

    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;
    private String telNumber;
    private Set<UserRoles> userRoles;
    private String country;

}
