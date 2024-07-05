package dev.semkoksharov.vibeshub2.roles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RoleDetailsRepo extends JpaRepository <RoleDetails, Long> {
    List<RoleDetails> findAllByUserId(Long userId);
}
