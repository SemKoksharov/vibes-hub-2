package dev.semkoksharov.vibeshub2.repository;

import dev.semkoksharov.vibeshub2.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepo extends JpaRepository<Song, Long> {
}
