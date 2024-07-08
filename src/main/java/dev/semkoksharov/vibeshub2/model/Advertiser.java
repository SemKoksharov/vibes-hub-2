package dev.semkoksharov.vibeshub2.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "advertiser_details")
public class Advertiser extends RoleDetails {

    private String company;
    private String taxCode;

    @OneToMany(mappedBy = "advertiser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Ad> ads;


    public Advertiser(UserEntity user, String description, String company, String taxCode, Set<Ad> ads) {
        super(user, description);
        this.company = company;
        this.taxCode = taxCode;
        this.ads = ads;
    }

    public Advertiser() {
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTaxCode() {
        return taxCode;
    }


    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public Set<Ad> getAds() {
        return ads;
    }

    public void setAds(Set<Ad> ads) {
        this.ads = ads;
    }
}


