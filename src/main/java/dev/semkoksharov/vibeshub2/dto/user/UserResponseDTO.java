package dev.semkoksharov.vibeshub2.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.semkoksharov.vibeshub2.model.enums.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

}