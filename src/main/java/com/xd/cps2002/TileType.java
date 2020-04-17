package com.xd.cps2002;

/**
 *  The {@link TileType} enum defines the different types of tiles used in different maps.
 *  It also defines the {@link com.xd.cps2002.PlayerStatus} after stepping on that particular tile.
 *  Each tile is described by its PlayerStatus, a handle (string identifier), and a colour (as three integers [r,g,b]).
 *
 * @author Domenico Agius and Xandru Mifsud
 */
public enum TileType {
    /**
     * The {@code Grass} tile has a {@link PlayerStatus} of type {@link PlayerStatus#Normal} since nothing
     * happens when the player steps on this tile.
     */
    Grass(PlayerStatus.Normal, "grass", 210, 250, 100),
    /**
     * The {@code Water} tile has a {@link PlayerStatus} of type {@link PlayerStatus#Death} since the player
     * should restart the game when they step on this tile.
     */
    Water(PlayerStatus.Death, "water", 140, 210, 240),
    /**
     * The {@code Treasure} tile has a {@link PlayerStatus} of type {@link PlayerStatus#Death} since the player
     * should win the game when they step on this tile.
     */
    Treasure(PlayerStatus.Win, "treasure", 200, 200, 50);

    /**
     * This value of the {@link PlayerStatus} enum describes what happens to the player as they move onto a particular
     * type of tile.
     */
    public final PlayerStatus statusAfterMove;

    /**
     * These two values describe the CSS style of the tile. The first, html_handle, is a simple string identifier used
     * to reference the html_blurb. The blurb is what actually describes the CSS style of the tile.
     */
    public final String html_handle;
    public final String html_blurb;

    /**
     * This function is used to initialize each tile type.
     * @param statusAfterMove {@link PlayerStatus} value to be associated with the new tile type.
     * @param html_handle is the identifier used to reference the html_blurb in the HTML file.
     * @param r is the red channel value for the tile colour.
     * @param g is the green channel value for the tile colour.
     * @param b is the blue channel value for the tile colour.
     */
    TileType(PlayerStatus statusAfterMove, String html_handle, int r, int g, int b) {
        this.statusAfterMove = statusAfterMove;
        this.html_handle = html_handle;
        this.html_blurb = "." + html_handle +
                          " {\nbackground-color: rgba(" + r + ", " + g + ", " + b + ", 0.8);\n" +
                          "border: 1px solid rgba(0, 0, 0, 0.8);\n" +
                          "font-size: 30px;\n" +
                          "text-align: center;}\n\n";
    }
}
