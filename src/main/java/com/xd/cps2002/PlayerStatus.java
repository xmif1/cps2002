package com.xd.cps2002;

import com.xd.cps2002.map.TileType;

/**
 * The {@link PlayerStatus} enum defines the different states/outcome that can happen as a result of the player
 * moving to a specific tile. This is used in {@link TileType} to tie each type of tile with a specific outcome.
 *
 * @author Domenico Agius
 */
public enum PlayerStatus {
    /**
     * The {@code Normal} status shows that nothing happened after the user moved.
     */
    Normal,
    /**
     * The {@code Death} status shows that the player need to restart the game.
     */
    Death,
    /**
     * The {@code Win} status shows that the player has won the game after moving to a tile.
     */
    Win
}
