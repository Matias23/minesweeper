package com.mricotta.minesweeper.rest;

import com.mricotta.minesweeper.rest.dto.Cell;
import com.mricotta.minesweeper.service.GameService;
import com.mricotta.minesweeper.service.MinesweeperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("gameControllerV1")
@RequestMapping( "/game")
@Validated
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @RequestMapping(value = {"/{gameId:\\d+}"}, method = RequestMethod.GET)
    public ResponseEntity<List<Cell>> getBoard(@PathVariable long gameId) {
        return gameService.getBoard(gameId);
    }

    @RequestMapping(value = {"/{gameId:\\d+}/visitCell/{x:\\d+}/{y:\\d+}"}, method = RequestMethod.PUT)
    public ResponseEntity<Cell> visitCellByCoordinates(@PathVariable long gameId, @PathVariable int x, @PathVariable int y) {
        return gameService.visitCellByCoordinates(gameId, x, y);
    }

    @RequestMapping(value = {"/{gameId:\\d+}/flagCell/{x:\\d+}/{y:\\d+}"}, method = RequestMethod.PUT)
    public ResponseEntity<Cell> flagCellByCoordinates(@PathVariable long gameId, @PathVariable int x, @PathVariable int y) {
        return gameService.flagCellByCoordinates(gameId, x, y);
    }
}
