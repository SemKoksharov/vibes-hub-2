package dev.semkoksharov.vibeshub2.repository;

import dev.semkoksharov.vibeshub2.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepo extends JpaRepository<Album, Long> {
}
