package dev.semkoksharov.vibeshub2.users.entity;

import dev.semkoksharov.vibeshub2.advertisement.Ad;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.util.HashSet;
import java.util.Set;

public class Advertiser {

    private Long id;
    private UserEntity user;
    private String companyName;
    private String description;

    @OneToMany(mappedBy = "advertiser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Ad> ads = new HashSet<>();


}
