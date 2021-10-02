package com.mricotta.minesweeper.service;

import com.mricotta.minesweeper.domain.CellEntity;
import com.mricotta.minesweeper.repository.CellRepository;
import com.mricotta.minesweeper.rest.dto.Cell;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@EnableAsync
@Slf4j
@SuppressWarnings({"PMD.TooManyFields", "PMD.ExcessiveClassLength"})
@RequiredArgsConstructor
public class MinesweeperService {

    @Autowired
    private final CellRepository cellRepository;

    public Optional<Cell> getCellByCoordinates(int x, int y) {
        return cellRepository.findOneByCoordinate(x, y).map(this::toCellDTO);
    }

    private Cell toCellDTO(CellEntity entity) {
        return Cell.builder().build();
    }
}
