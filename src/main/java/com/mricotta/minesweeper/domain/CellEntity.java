package com.mricotta.minesweeper.domain;

import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@javax.persistence.Entity
@Table(name = "cell")
public class CellEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cell_id")
    private Long id;

    @NotNull
    @Column(name = "x_pos")
    private int xpos;

    @NotNull
    @Column(name = "y_pos")
    private int ypos;

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
    @JoinColumn(name = "game_id")
    private GameEntity gameEntity;
}
