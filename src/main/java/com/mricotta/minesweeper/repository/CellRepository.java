package com.mricotta.minesweeper.repository;

import com.mricotta.minesweeper.domain.CellEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CellRepository extends JpaRepository<CellEntity, Long> {

    @Query("SELECT c FROM CellEntity c WHERE c.xpos = (:x) AND c.ypos = (:y) ")
    Optional<CellEntity> findOneByCoordinate(int x, int y);
}
