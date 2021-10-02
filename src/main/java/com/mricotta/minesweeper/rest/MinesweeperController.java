package com.mricotta.minesweeper.rest;

import com.mricotta.minesweeper.rest.dto.Cell;
import com.mricotta.minesweeper.service.MinesweeperService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController("minesweeperControllerV1")
@RequestMapping( "/minesweeper")
@Validated
@RequiredArgsConstructor
public class MinesweeperController {

    private final MinesweeperService minesweeperService;

    @RequestMapping(value = {"/{x:\\d+}/{y:\\d+}"}, method = RequestMethod.GET)
    public Optional<Cell> getCellByCoordinates(@PathVariable int x, @PathVariable int y) {
        return minesweeperService.getCellByCoordinates(x, y);
    }

}
