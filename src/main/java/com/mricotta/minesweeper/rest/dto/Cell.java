package com.mricotta.minesweeper.rest.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Builder
@SuppressWarnings({"PMD.TooManyFields", "PMD.ExcessivePublicCount", "PMD.SingularField"})
public class Cell {

    private Long id;
    private int xpos;
    private int ypos;
    private boolean isMined;
    private boolean isVisited;
    private boolean isFlagged;
    // TODO verify if this should be stored or calculated private int adjacentMines;
}
