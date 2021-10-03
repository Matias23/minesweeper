package com.minesweeper.rest;

import com.minesweeper.rest.dto.Game;
import com.minesweeper.rest.dto.User;
import com.minesweeper.service.UserService;
import com.minesweeper.rest.dto.GameRules;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return userService.createUser(user.getName());
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
