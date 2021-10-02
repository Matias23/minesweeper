package com.mricotta.minesweeper.rest;

import com.mricotta.minesweeper.rest.dto.Game;
import com.mricotta.minesweeper.rest.dto.GameRules;
import com.mricotta.minesweeper.rest.dto.User;
import com.mricotta.minesweeper.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("userControllerV1")
@RequestMapping( "/user")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@PathVariable String userName) {
        return userService.createUser(userName);
    }

    @RequestMapping(value = {"/{userId:\\d+}"}, method = RequestMethod.GET)
    public ResponseEntity<User> getUserById(@PathVariable long userId) {
        return userService.getUser(userId);
    }

    @RequestMapping(value = {"/{userId:\\d+}/newGame"}, method = RequestMethod.POST)
    public ResponseEntity<Game> initializeGame(@PathVariable long userId, @RequestBody GameRules gameRules) {
        return userService.initializeGame(userId, gameRules);
    }
}
