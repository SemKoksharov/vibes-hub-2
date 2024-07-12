package dev.semkoksharov.vibeshub2.dto.advertisement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
//for AD creation
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdDTO {

    private Long advertiserUserId;
    private String advText;
    private String audioLink;
    private String imageLink;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer orderedViews;

}
