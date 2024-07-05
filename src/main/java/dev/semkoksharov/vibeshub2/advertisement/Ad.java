package dev.semkoksharov.vibeshub2.advertisement;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dev.semkoksharov.vibeshub2.users.entity.Advertiser;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class Ad {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "advertiser_id")
    private Advertiser advertiser;

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
}
