package dev.semkoksharov.vibeshub2.repository;

import dev.semkoksharov.vibeshub2.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepo extends JpaRepository<Genre, Long> {
}
