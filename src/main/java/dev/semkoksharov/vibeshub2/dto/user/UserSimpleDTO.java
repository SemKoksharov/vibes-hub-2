package dev.semkoksharov.vibeshub2.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSimpleDTO {
    private Long id;
    private String username;
    private String name;
    private String surname;
}
