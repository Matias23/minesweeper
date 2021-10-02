package com.mricotta.minesweeper.service;

import com.mricotta.minesweeper.domain.CellEntity;
import com.mricotta.minesweeper.repository.CellRepository;
import com.mricotta.minesweeper.rest.dto.Cell;
import com.mricotta.minesweeper.rest.dto.GameRules;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@EnableAsync
@Slf4j
@SuppressWarnings({"PMD.TooManyFields", "PMD.ExcessiveClassLength"})
@RequiredArgsConstructor
public class MinesweeperService {

    @Autowired
    private final CellRepository cellRepository;

    public Optional<Cell> getCellByCoordinates(int x, int y) {
        return cellRepository.findOneByCoordinate(x, y).map(this::toCellDTO);
    }

    public Cell visitCellByCoordinates(int x, int y) {
        CellEntity cellEntity = cellRepository.findOneByCoordinate(x, y).orElse(null);
        if (cellEntity == null) {
            //TODO cell is out of limits, print some error;
        }
        if (cellEntity.isVisited()) {
            //TODO user has already visited it, should do nothing
        }
        if (cellEntity.isMined()) {
            //TODO endgame
        }
        cellEntity.setVisited(true);
        return toCellDTO(cellRepository.save(cellEntity));
    }

    public Cell flagCellByCoordinates(int x, int y) {
        CellEntity cellEntity = cellRepository.findOneByCoordinate(x, y).orElse(null);
        if (cellEntity == null) {
            //TODO cell is out of limits, print some error;
        }
        cellEntity.setFlagged(true);
        return toCellDTO(cellRepository.save(cellEntity));
    }

    private Cell toCellDTO(CellEntity entity) {
        return Cell.builder().build();
    }

    public void initializeGame(GameRules gameRules) {
        ////Creating all the cell entities
        createCells(gameRules);

        //Setting the bombs
        putBombs(gameRules);

        //Setting adjacent bombs number
        settingAdjacentNumber(gameRules);
    }

    private void createCells(GameRules gameRules) {
        CellEntity cellEntity;
        for (int row = 0; row < gameRules.getSize(); row++) {
            for (int column = 0; column < gameRules.getSize(); column++) {
                cellEntity = CellEntity.builder()
                        .isVisited(false)
                        .isFlagged(false)
                        .xpos(column)
                        .ypos(row)
                        .build();
                cellRepository.save(cellEntity);
            }
        }
    }

    private void putBombs(GameRules gameRules) {
        int mines = gameRules.getMines();
        while (mines > 0) {
            //TODO get new bomb coordinates in a random way
            int x = -1;
            int y = -1;
            CellEntity minedCell = cellRepository.findOneByCoordinate(x, y).orElse(null);
            minedCell.setMined(true);
            cellRepository.save(minedCell);
        }
    }

    private void settingAdjacentNumber(GameRules gameRules) {
        for (int row = 0; row < gameRules.getSize(); row++) {
            for (int column = 0; column < gameRules.getSize(); column++) {
                CellEntity minedCell = cellRepository.findOneByCoordinate(row, column).orElse(null);
                //TODO think a way of calculating this value
                int adjacentMines = -1;
                minedCell.setAdjacentMines(adjacentMines);
                cellRepository.save(minedCell);
            }
        }
    }
}
