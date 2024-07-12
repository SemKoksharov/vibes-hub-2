package dev.semkoksharov.vibeshub2.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvertiserDTO {

    private Long userId;
    private String description;
    private String company;
    private String taxCode;

}
