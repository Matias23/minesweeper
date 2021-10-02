package com.mricotta.minesweeper.service;

import com.mricotta.minesweeper.domain.GameEntity;
import com.mricotta.minesweeper.domain.UserEntity;
import com.mricotta.minesweeper.repository.UserRepository;
import com.mricotta.minesweeper.rest.dto.Game;
import com.mricotta.minesweeper.rest.dto.GameRules;
import com.mricotta.minesweeper.rest.dto.User;
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
        return new ResponseEntity(toUserDTO(userRepository.getById(userId)), HttpStatus.OK);
    }

    public ResponseEntity<Game> initializeGame(long userId, GameRules gameRules) {
        UserEntity userEntity = userRepository.getById(userId);
        GameEntity gameEntity = GameEntity.builder().width(gameRules.getWidth()).height(gameRules.getHeight()).userEntity(userEntity).build();
        gameService.initializeGame(gameEntity, gameRules);
        return gameService.createGame(gameEntity);
    }
}
