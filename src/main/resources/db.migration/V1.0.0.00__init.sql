-- --------------------------------------------------------------------------------
-- User
-- --------------------------------------------------------------------------------
CREATE TABLE user (
    user_id bigint NOT NULL AUTO_INCREMENT COMMENT 'Auto Increment PK',
    name varchar(100) COMMENT 'User name',
    PRIMARY KEY (user_id)
) COMMENT 'A user of the minesweeper game';

-- --------------------------------------------------------------------------------
-- Game
-- --------------------------------------------------------------------------------
CREATE TABLE game (
    game_id  bigint   NOT NULL AUTO_INCREMENT COMMENT 'Auto Increment PK',
    width  int NOT NULL COMMENT 'Board width',
    height int NOT NULL  COMMENT 'Board height',
    mines  int NOT NULL COMMENT 'Board mines',
    user_id bigint NOT NULL COMMENT 'FK to user table',
    PRIMARY KEY (game_id)
) COMMENT 'A minesweeper game';

-- --------------------------------------------------------------------------------
-- Cell
-- --------------------------------------------------------------------------------
CREATE TABLE cell (
    x_pos  int NOT NULL COMMENT 'X position',
    y_pos int NOT NULL  COMMENT 'Y position',
    is_mined tinyint(1) COMMENT 'Boolean indicating whether the cell is mined',
    is_visited tinyint(1) COMMENT 'Boolean indicating whether the cell was visited',
    is_flagged tinyint(1) COMMENT 'Boolean indicating whether the cell was flagged by the user',
    game_id bigint NOT NULL COMMENT 'FK to game table',
    adjacent_mines int NOT NULL  COMMENT 'Adjacent mines to this cell',
    PRIMARY KEY (x_pos, y_pos, game_id)
) COMMENT='Cell of a minesweeper game';