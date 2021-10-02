package com.mricotta.minesweeper.rest;

import com.mricotta.minesweeper.rest.dto.Cell;
import com.mricotta.minesweeper.rest.dto.GameRules;
import com.mricotta.minesweeper.service.MinesweeperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController("minesweeperControllerV1")
@RequestMapping( "/minesweeper/cell")
@Validated
@RequiredArgsConstructor
public class MinesweeperController {

    private final MinesweeperService minesweeperService;

    @RequestMapping(value = {"/{x:\\d+}/{y:\\d+}"}, method = RequestMethod.GET)
    public Optional<Cell> getCellByCoordinates(@PathVariable int x, @PathVariable int y) {
        return minesweeperService.getCellByCoordinates(x, y);
    }

    @RequestMapping(value = {"/visit/{x:\\d+}/{y:\\d+}"}, method = RequestMethod.PUT)
    public ResponseEntity<Cell> visitCellByCoordinates(@PathVariable int x, @PathVariable int y) {
        return minesweeperService.visitCellByCoordinates(x, y);
    }

    @RequestMapping(value = {"/flag/{x:\\d+}/{y:\\d+}"}, method = RequestMethod.PUT)
    public ResponseEntity<Cell> flagCellByCoordinates(@PathVariable int x, @PathVariable int y) {
        return minesweeperService.flagCellByCoordinates(x, y);
    }

    @RequestMapping(value = {"/newGame"}, method = RequestMethod.POST)
    public ResponseEntity initializeGame(@RequestBody GameRules gameRules) {
        return minesweeperService.initializeGame(gameRules);
    }

    @RequestMapping(value = {"/reset"}, method = RequestMethod.POST)
    public ResponseEntity resetGame() {
        return minesweeperService.resetGame();
    }
}
