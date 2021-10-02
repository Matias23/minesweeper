package com.mricotta.minesweeper.repository;

import com.mricotta.minesweeper.domain.CellEntity;
import com.mricotta.minesweeper.domain.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Long> {
}
