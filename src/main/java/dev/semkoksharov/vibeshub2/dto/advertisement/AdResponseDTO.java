package dev.semkoksharov.vibeshub2.dto.advertisement;

import dev.semkoksharov.vibeshub2.dto.user.AdvertiserSimpleDTO;
import dev.semkoksharov.vibeshub2.model.Advertiser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
// with advertiser simple DTO for AD request
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdResponseDTO {

    private Long id;
    private AdvertiserSimpleDTO advertiser;
    private String advText;
    private String audioLink;
    private String imageLink;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer orderedViews;
    private Float costPerDay;
    private Float costPerView;
    private Float finalCost;
    private int actualViews;
    private Integer actualDuration;
    private Integer totalDuration;
    private Boolean active;



}
