package com.mricotta.minesweeper.rest;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("minesweeperControllerV1")
@RequestMapping( "/minesweeper")
@Validated
public class ListingController {

    @RequestMapping(value = {"/{x:\\d+}/{y:\\d+}"}, method = RequestMethod.GET)
    public String getValueByCoordinates(@PathVariable int x, @PathVariable int y) {
        return String.format("Retrieving value for element in x: %s and y: %s", x, y);
    }

}
