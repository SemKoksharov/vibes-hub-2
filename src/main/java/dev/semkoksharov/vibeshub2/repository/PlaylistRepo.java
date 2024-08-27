package dev.semkoksharov.vibeshub2.repository;

import dev.semkoksharov.vibeshub2.dto.playlist.PlaylistResponseDTO;
import dev.semkoksharov.vibeshub2.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepo extends JpaRepository<Playlist, Long> {
  List<Playlist> findAllByTitle(String title);
}
