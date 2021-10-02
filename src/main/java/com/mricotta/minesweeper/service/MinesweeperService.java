package com.mricotta.minesweeper.service;

import com.mricotta.minesweeper.domain.CellEntity;
import com.mricotta.minesweeper.repository.CellRepository;
import com.mricotta.minesweeper.rest.dto.Cell;
import com.mricotta.minesweeper.rest.dto.GameRules;
import com.sun.xml.internal.bind.v2.TODO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@EnableAsync
@Slf4j
@SuppressWarnings({"PMD.TooManyFields", "PMD.ExcessiveClassLength"})
@RequiredArgsConstructor
public class MinesweeperService {

    @Autowired
    private final CellRepository cellRepository;

    public Optional<Cell> getCellByCoordinates(int x, int y) {
        return getCellEntityByCoordinates(x,y).map(this::toCellDTO);
    }

    public ResponseEntity visitCellByCoordinates(int x, int y) {
        CellEntity cellEntity = cellRepository.findOneByCoordinate(x, y).orElse(null);
        if (cellEntity == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if (cellEntity.isVisited()) {
            return new ResponseEntity(HttpStatus.OK);
        }
        if (cellEntity.isMined()) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        cellEntity.setVisited(true);
        return new ResponseEntity(toCellDTO(cellRepository.save(cellEntity)), HttpStatus.OK);
    }

    public ResponseEntity<Cell> flagCellByCoordinates(int x, int y) {
        CellEntity cellEntity = cellRepository.findOneByCoordinate(x, y).orElse(null);
        if (cellEntity == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        cellEntity.setFlagged(true);
        return new ResponseEntity(toCellDTO(cellRepository.save(cellEntity)), HttpStatus.OK);
    }

    private Cell toCellDTO(CellEntity entity) {
        return Cell.builder().build();
    }

    public ResponseEntity initializeGame(GameRules gameRules) {
        int numberOfCells = gameRules.getHeight() * gameRules.getWidth() - 1;

        //The game cannot be player if all cells are mined
        if (gameRules.getMines() >= numberOfCells) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        ////Creating all the cell entities
        createCells(gameRules);

        //Setting the bombs
        putBombs(gameRules, numberOfCells);

        //Setting adjacent bombs number
        settingAdjacentNumber(gameRules);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    private void createCells(GameRules gameRules) {
        CellEntity cellEntity;
        for (int row = 0; row < gameRules.getHeight(); row++) {
            for (int column = 0; column < gameRules.getWidth(); column++) {
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

    private void putBombs(GameRules gameRules, int numberOfCells) {
        int mines = gameRules.getMines();
        while (mines > 0) {
            Random r = new Random();
            int randomNumber = r.nextInt(numberOfCells);
            int randomRow = (int)Math.floor(randomNumber / gameRules.getWidth());
            int randomColumn = randomNumber % gameRules.getWidth();
            CellEntity minedCell = cellRepository.findOneByCoordinate(randomRow, randomColumn).orElse(null);
            if (minedCell.isMined()) {
                continue;
            }
            minedCell.setMined(true);
            cellRepository.save(minedCell);
            mines--;
        }
    }

    private void settingAdjacentNumber(GameRules gameRules) {
        for (int row = 0; row < gameRules.getHeight(); row++) {
            for (int column = 0; column < gameRules.getWidth(); column++) {
                CellEntity cell = cellRepository.findOneByCoordinate(row, column).orElse(null);
                /* TODO think a better way of calculating this value, since cell at the corners and borders
                will only have 3/5 adjacent cells*/
                int adjacentMines = 0;
                adjacentMines += searchMine(row - 1, column, gameRules.getWidth(), gameRules.getHeight());
                adjacentMines += searchMine(row - 1, column - 1, gameRules.getWidth(), gameRules.getHeight());
                adjacentMines += searchMine(row - 1, column + 1, gameRules.getWidth(), gameRules.getHeight());
                adjacentMines += searchMine(row + 1, column, gameRules.getWidth(), gameRules.getHeight());
                adjacentMines += searchMine(row + 1, column - 1, gameRules.getWidth(), gameRules.getHeight());
                adjacentMines += searchMine(row + 1, column + 1, gameRules.getWidth(), gameRules.getHeight());
                adjacentMines += searchMine(row, column + 1, gameRules.getWidth(), gameRules.getHeight());
                adjacentMines += searchMine(row, column - 1, gameRules.getWidth(), gameRules.getHeight());
                cell.setAdjacentMines(adjacentMines);
                cellRepository.save(cell);
            }
        }
    }

    private int searchMine(int xpos, int ypos, int maxWidth, int maxHeight) {
        if (xpos < 0 || ypos < 0 || xpos >= maxWidth || ypos >= maxHeight) {
            return 0;
        }
        CellEntity cellEntity = getCellEntityByCoordinates(xpos, ypos).orElse(null);
        return cellEntity.isMined() ? 1 : 0;
    }

    private Optional<CellEntity> getCellEntityByCoordinates(int x, int y) {
        return cellRepository.findOneByCoordinate(x, y);
    }

    public ResponseEntity resetGame() {
        cellRepository.deleteAll();
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
