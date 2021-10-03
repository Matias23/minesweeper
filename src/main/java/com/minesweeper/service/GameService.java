package com.minesweeper.service;

import com.minesweeper.domain.CellEntity;
import com.minesweeper.domain.CellId;
import com.minesweeper.domain.GameEntity;
import com.minesweeper.repository.CellRepository;
import com.minesweeper.repository.GameRepository;
import com.minesweeper.rest.dto.Cell;
import com.minesweeper.rest.dto.Game;
import com.minesweeper.rest.dto.GameRules;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@EnableAsync
@Slf4j
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final CellRepository cellRepository;

    public Game createGame(GameEntity entity) {
        return toGameDTO(gameRepository.save(entity));
    }

    private Game toGameDTO(GameEntity entity) {
        return Game.builder()
            .gameId(entity.getGameId())
            .height(entity.getHeight())
            .width(entity.getWidth())
            .mines(entity.getMines())
            .build();
    }

    private Cell toCellDTO(CellEntity entity) {
        return Cell.builder()
            .xpos(entity.getCellId().getXpos())
            .ypos(entity.getCellId().getYpos())
            .adjacentMines(entity.getAdjacentMines())
            .isFlagged(entity.isFlagged())
            .isMined(entity.isMined())
            .isVisited(entity.isVisited())
            .build();
    }

    public ResponseEntity initializeGame(GameEntity gameEntity, GameRules gameRules) {
        int numberOfCells = gameRules.getHeight() * gameRules.getWidth() - 1;

        //The game cannot be player if all cells are mined
        if (gameRules.getMines() >= numberOfCells) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        ////Creating all the cell entities
        createCells(gameEntity, gameRules);

        //Setting the bombs
        putBombs(gameEntity, gameRules, numberOfCells);

        //Setting adjacent bombs number
        settingAdjacentNumber(gameEntity, gameRules);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    private void createCells(GameEntity gameEntity, GameRules gameRules) {
        CellEntity cellEntity;
        for (int row = 0; row < gameRules.getHeight(); row++) {
            for (int column = 0; column < gameRules.getWidth(); column++) {
                cellEntity = CellEntity.builder()
                        .isVisited(false)
                        .isFlagged(false)
                        .cellId(CellId.builder()
                            .gameId(gameEntity.getGameId())
                            .xpos(column)
                            .ypos(row)
                            .build())
                        .gameEntity(gameEntity)
                        .build();
                cellRepository.save(cellEntity);
            }
        }
    }

    private void putBombs(GameEntity gameEntity, GameRules gameRules, int numberOfCells) {
        int mines = gameRules.getMines();
        while (mines > 0) {
            Random r = new Random();
            int randomNumber = r.nextInt(numberOfCells);
            int randomRow = (int)Math.floor(randomNumber / gameRules.getWidth());
            int randomColumn = randomNumber % gameRules.getWidth();
            CellEntity minedCell = cellRepository.findOneByGameIdAndCoordinates(gameEntity.getGameId(), randomRow, randomColumn).orElse(null);
            if (minedCell.isMined()) {
                continue;
            }
            minedCell.setMined(true);
            cellRepository.save(minedCell);
            mines--;
        }
    }

    private void settingAdjacentNumber(GameEntity gameEntity, GameRules gameRules) {
        for (int row = 0; row < gameRules.getHeight(); row++) {
            for (int column = 0; column < gameRules.getWidth(); column++) {
                CellEntity cell = cellRepository.findOneByGameIdAndCoordinates(gameEntity.getGameId(), row, column).orElse(null);
                List<Cell> adjacents = getAdjacents(toCellDTO(cell), gameRules.getWidth(), gameRules.getHeight());
                cell.setAdjacentMines(searchMines(gameEntity.getGameId(), adjacents, gameRules.getWidth(), gameRules.getHeight()));
                cellRepository.save(cell);
            }
        }
    }

    private List<Cell> getAdjacents(Cell cell, int width, int height) {
        List<Cell> adjacents = new ArrayList<>();
        if (cell.getYpos() == 0) {
            //Cell is at top level
            if (cell.getXpos() == 0) {
                //Cell is the top left corner
                adjacents.add(Cell.builder().ypos(cell.getYpos() + 1).xpos(cell.getXpos()).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos() + 1).xpos(cell.getXpos() + 1).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos()).xpos(cell.getXpos() + 1).build());
            } else if (cell.getXpos() == width - 1) {
                //Cell is at top right corner
                adjacents.add(Cell.builder().ypos(cell.getYpos()).xpos(cell.getXpos() - 1).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos() + 1).xpos(cell.getXpos() - 1).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos() +1).xpos(cell.getXpos()).build());
            } else {
                //Cell is in any middle position in the top
                adjacents.add(Cell.builder().ypos(cell.getYpos()).xpos(cell.getXpos() - 1).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos() + 1).xpos(cell.getXpos() - 1).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos() + 1).xpos(cell.getXpos()).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos() + 1).xpos(cell.getXpos() + 1).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos()).xpos(cell.getXpos() + 1).build());
            }
        } else if (cell.getYpos() == height - 1) {
            //Cell is at bottom level
            if (cell.getXpos() == 0) {
                //Cell is the bottom left corner
                adjacents.add(Cell.builder().ypos(cell.getYpos() - 1).xpos(cell.getXpos()).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos() - 1).xpos(cell.getXpos() + 1).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos()).xpos(cell.getXpos() + 1).build());
            } else if (cell.getXpos() == width - 1) {
                //Cell is at bottom right corner
                adjacents.add(Cell.builder().ypos(cell.getYpos()).xpos(cell.getXpos() - 1).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos() - 1).xpos(cell.getXpos() - 1).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos() - 1).xpos(cell.getXpos()).build());
            } else {
                //Cell is in any middle position in the bottom
                adjacents.add(Cell.builder().ypos(cell.getYpos()).xpos(cell.getXpos() - 1).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos() - 1).xpos(cell.getXpos() - 1).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos() - 1).xpos(cell.getXpos()).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos() - 1).xpos(cell.getXpos() + 1).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos()).xpos(cell.getXpos() + 1).build());
            }
        } else {
            //Cell is in the middle
            if (cell.getXpos() == 0) {
                //Cell is in any middle position in the first column
                adjacents.add(Cell.builder().ypos(cell.getYpos() - 1).xpos(cell.getXpos()).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos() - 1).xpos(cell.getXpos() + 1).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos() + 1).xpos(cell.getXpos()).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos() + 1).xpos(cell.getXpos() + 1).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos()).xpos(cell.getXpos() + 1).build());
            } else if (cell.getXpos() == width - 1) {
                //Cell is in any middle position in the last column
                adjacents.add(Cell.builder().ypos(cell.getYpos() - 1).xpos(cell.getXpos()).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos() - 1).xpos(cell.getXpos() - 1).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos() + 1).xpos(cell.getXpos()).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos() + 1).xpos(cell.getXpos() - 1).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos()).xpos(cell.getXpos() - 1).build());
            } else {
                //Cell is in the middle of the board
                adjacents.add(Cell.builder().ypos(cell.getYpos()- 1).xpos(cell.getXpos()).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos()- 1).xpos(cell.getXpos() - 1).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos()- 1).xpos(cell.getXpos() + 1).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos()+ 1).xpos(cell.getXpos()).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos()+ 1).xpos(cell.getXpos() - 1).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos()+ 1).xpos(cell.getXpos() + 1).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos()).xpos(cell.getXpos() + 1).build());
                adjacents.add(Cell.builder().ypos(cell.getYpos()).xpos(cell.getXpos() - 1).build());
            }
        }
        return adjacents;
    }

    private int searchMines(long gameId, List<Cell> cells, int maxWidth, int maxHeight) {
        int mines = 0;
        for (Cell c : cells) {
            if (c.getXpos() < 0 || c.getYpos() < 0 || c.getXpos() >= maxWidth || c.getYpos() >= maxHeight) {
                continue;
            }
            CellEntity cellEntity = getCellEntityByCoordinates(gameId, c.getXpos(), c.getYpos()).orElse(null);
            mines += cellEntity.isMined() ? 1 : 0;
        }
        return mines;
    }

    private Optional<CellEntity> getCellEntityByCoordinates(long gameId, int x, int y) {
        return cellRepository.findOneByGameIdAndCoordinates(gameId, x, y);
    }

    public ResponseEntity<List<Cell>> getBoard(long gameId) {
        return new ResponseEntity(cellRepository.findAllByGameId(gameId).stream().map(cell -> toCellDTO(cell)).collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<Cell> visitCellByCoordinates(long gameId, int x, int y) {

        CellEntity cellEntity = cellRepository.findOneByGameIdAndCoordinates(gameId, x, y).orElse(null);
        if (cellEntity == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if (cellEntity.isMined()) {
            //TODO user lost
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

        CellEntity visited = visitCellEntity(gameId, cellEntity);

        if (isGameEnded(gameId)) {
            //TODO user won the game
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(toCellDTO(visited), HttpStatus.OK);
    }

    private boolean isGameEnded(long gameId) {
        GameEntity gameEntity = gameRepository.findById(gameId).orElse(null);
        int totalCells = gameEntity.getHeight() * gameEntity.getWidth();
        int bombs = gameEntity.getMines();
        int visitedCells = cellRepository.countVisitedCellsByGameId(gameId);
        return visitedCells + bombs == totalCells;
    }

    private CellEntity visitCellEntity(long gameId, CellEntity cellEntity) {
        if (cellEntity.isVisited()) {
            return cellEntity;
        }
        cellEntity.setVisited(true);
        //Check if adjacent has mines, if not then visit them
        GameEntity gameEntity = gameRepository.findById(gameId).orElse(null);
        List<Cell> adjacents = getAdjacents(toCellDTO(cellEntity), gameEntity.getWidth(), gameEntity.getHeight());
        if (searchMines(gameId, adjacents, gameEntity.getWidth(), gameEntity.getHeight()) == 0) {
            CellEntity adjacentEntity;
            for (Cell adjacent : adjacents) {
                adjacentEntity = getCellEntityByCoordinates(gameId, adjacent.getXpos(), adjacent.getXpos()).orElse(null);
                visitCellEntity(gameId, adjacentEntity);
            }
        }
        return cellRepository.save(cellEntity);
    }

    public ResponseEntity<Cell> flagCellByCoordinates(long gameId, int x, int y) {
        CellEntity cellEntity = cellRepository.findOneByGameIdAndCoordinates(gameId, x, y).orElse(null);
        if (cellEntity == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        cellEntity.setFlagged(true);
        return new ResponseEntity(toCellDTO(cellRepository.save(cellEntity)), HttpStatus.OK);
    }

}
