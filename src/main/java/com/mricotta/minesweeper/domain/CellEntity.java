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
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "xpos")
    private int xpos;

    @NotNull
    @Column(name = "ypos")
    private int ypos;

    @NotNull
    @Column(name = "isMined")
    private boolean isMined;

    @NotNull
    @Column(name = "isVisited")
    private boolean isVisited;

    @NotNull
    @Column(name = "isFlagged")
    private boolean isFlagged;

    @NotNull
    @Column(name = "adjacentMines")
    private int adjacentMines;

    @ManyToOne
    @JoinColumn(name = "gameId")
    private GameEntity gameEntity;
}
