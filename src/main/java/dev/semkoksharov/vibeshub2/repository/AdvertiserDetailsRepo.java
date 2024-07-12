package dev.semkoksharov.vibeshub2.repository;

import dev.semkoksharov.vibeshub2.model.Advertiser;
import dev.semkoksharov.vibeshub2.model.base.RoleDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdvertiserDetailsRepo extends JpaRepository<Advertiser, Long> {
    List<RoleDetails> findAllByUserId(Long userId);

    Optional<RoleDetails> findByUser_Id(Long id);
}
