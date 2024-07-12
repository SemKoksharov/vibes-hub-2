package dev.semkoksharov.vibeshub2.dto.advertisement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
// No advertiser
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdSimpleDTO {

    private Long id;
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
