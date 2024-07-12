package dev.semkoksharov.vibeshub2.repository;

import dev.semkoksharov.vibeshub2.model.Artist;
import dev.semkoksharov.vibeshub2.model.base.RoleDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistDetailsRepo extends JpaRepository<Artist,Long> {

    Optional<RoleDetails> findByUser_Id(Long userID);
}
