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
public class GameRules {

    private int size;
    private int mines;
}
