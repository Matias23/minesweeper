package com.minesweeper.repository;

import com.minesweeper.domain.CellEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CellRepository extends CrudRepository<CellEntity, Long> {

    @Query("SELECT c FROM CellEntity c WHERE c.cellId.xpos = (:x) AND c.cellId.ypos = (:y) AND c.cellId.gameId = (:gameId)")
    Optional<CellEntity> findOneByGameIdAndCoordinates(long gameId, int x, int y);

    @Query("SELECT DISTINCT c FROM CellEntity c WHERE c.cellId.gameId = (:gameId)")
    List<CellEntity> findAllByGameId(long gameId);

    @Query("SELECT COUNT(c.isVisited) FROM CellEntity c WHERE c.cellId.gameId = (:gameId) AND c.isVisited = 1")
    int countVisitedCellsByGameId(long gameId);
}
