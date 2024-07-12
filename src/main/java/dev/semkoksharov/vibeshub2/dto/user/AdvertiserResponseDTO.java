package dev.semkoksharov.vibeshub2.dto.user;

import dev.semkoksharov.vibeshub2.dto.advertisement.AdSimpleDTO;
import dev.semkoksharov.vibeshub2.model.Ad;
import lombok.Data;

import java.util.Set;

@Data
public class AdvertiserResponseDTO {
// With advertiser simple DTOs (set)
    private Long id;
    private String description;
    private String artistName;
    private String company;
    private String taxCode;
    private Set<AdSimpleDTO> ads;
}
