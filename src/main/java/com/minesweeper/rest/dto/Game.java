package com.minesweeper.rest.dto;

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
public class Game {

    private Long gameId;
    private int mines;
    private int width;
    private int height;
}
