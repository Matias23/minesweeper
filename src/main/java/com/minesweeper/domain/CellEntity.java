package com.minesweeper.domain;

import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@javax.persistence.Entity
@Table(name = "cell")
public class CellEntity {

    @EmbeddedId
    private CellId cellId;

    @NotNull
    @Column(name = "is_mined")
    private boolean isMined;

    @NotNull
    @Column(name = "is_visited")
    private boolean isVisited;

    @NotNull
    @Column(name = "is_flagged")
    private boolean isFlagged;

    @NotNull
    @Column(name = "adjacent_mines")
    private int adjacentMines;

    @ManyToOne
    @MapsId("gameId")
    private GameEntity gameEntity;
}
