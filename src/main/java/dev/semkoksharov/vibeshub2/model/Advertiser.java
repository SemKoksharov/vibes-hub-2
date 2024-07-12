package dev.semkoksharov.vibeshub2.model;

import dev.semkoksharov.vibeshub2.model.base.RoleDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "advertiser_details")
public class Advertiser extends RoleDetails {

    private String company;
    private String taxCode;

    @OneToMany(mappedBy = "advertiser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Ad> ads;

    public void addAdvertisement(Ad adv){
        this.ads.add(adv);
    }
    public void removeAdvertisement(Ad adv){
        this.ads.remove(adv);
    }
}


