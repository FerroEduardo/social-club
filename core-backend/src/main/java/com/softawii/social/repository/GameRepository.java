package com.softawii.social.repository;

import com.softawii.social.model.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByName(String name);
    Page<Game> findByNameContainingIgnoreCase(String name, Pageable pageable);
}