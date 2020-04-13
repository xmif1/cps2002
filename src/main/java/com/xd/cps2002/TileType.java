package com.xd.cps2002;

/**
 *  The {@link TileType} enum defines the different types of tiles used in different maps.
 *  It also defines the {@link com.xd.cps2002.PlayerStatus} after stepping on that particular tile.
 *
 */
public enum TileType {
    /**
     * The {@code Grass} tile has a {@link PlayerStatus} of type {@link PlayerStatus#Normal} since nothing
     * happens when the player steps on this tile.
     */
    Grass(PlayerStatus.Normal),
    /**
     * The {@code Water} tile has a {@link PlayerStatus} of type {@link PlayerStatus#Death} since the player
     * should restart the game when they step on this tile.
     */
    Water(PlayerStatus.Death),
    /**
     * The {@code Treasure} tile has a {@link PlayerStatus} of type {@link PlayerStatus#Death} since the player
     * should win the game when they step on this tile.
     */
    Treasure(PlayerStatus.Win);

    /**
     * This value of the {@link PlayerStatus} enum describes what happens to the player as they move onto a particular
     * type of tile.
     */
    public final PlayerStatus statusAfterMove;

    /**
     * This function is used to initialize the {@code statusAfterMove} value of each tile type.
     * @param statusAfterMove {@link PlayerStatus} value to be associated with the new tile type.
     */
    TileType(PlayerStatus statusAfterMove) {
        this.statusAfterMove = statusAfterMove;
    }
}
