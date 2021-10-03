package com.minesweeper.service;

import com.minesweeper.domain.GameEntity;
import com.minesweeper.domain.UserEntity;
import com.minesweeper.repository.UserRepository;
import com.minesweeper.rest.dto.Game;
import com.minesweeper.rest.dto.GameRules;
import com.minesweeper.rest.dto.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final GameService gameService;

    public ResponseEntity<User> createUser(String userName) {
        UserEntity userEntity = UserEntity.builder().name(userName).build();
        return new ResponseEntity(toUserDTO(userRepository.save(userEntity)), HttpStatus.CREATED);
    }

    private User toUserDTO(UserEntity entity) {
        return User.builder().userId(entity.getUserId()).name(entity.getName()).build();
    }

    public ResponseEntity<User> getUser(long userId) {
        return new ResponseEntity(toUserDTO(userRepository.findById(userId).orElse(null)), HttpStatus.OK);
    }

    public ResponseEntity<Game> initializeGame(long userId, GameRules gameRules) {
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        GameEntity gameEntity = GameEntity.builder()
            .width(gameRules.getWidth())
            .height(gameRules.getHeight())
            .mines(gameRules.getMines())
            .userEntity(userEntity).build();
        Game game = gameService.createGame(gameEntity);
        gameService.initializeGame(gameEntity, gameRules);
        return new ResponseEntity(game, HttpStatus.CREATED);
    }
}
