package com.xd.cps2002;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tester class for the HTMLGenerator class.
 * @author Xandru Mifsud
 */
public class HTMLGeneratorTest{
    private Player player;

    // define the Map by means of a 2D TileType array
    private final TileType[][] tiles = {{TileType.Grass, TileType.Grass, TileType.Grass, TileType.Grass, TileType.Grass},
                                        {TileType.Grass, TileType.Grass, TileType.Water, TileType.Water, TileType.Water},
                                        {TileType.Grass, TileType.Grass, TileType.Treasure, TileType.Water, TileType.Water},
                                        {TileType.Grass, TileType.Grass, TileType.Grass, TileType.Water, TileType.Water},
                                        {TileType.Grass, TileType.Grass, TileType.Grass, TileType.Grass, TileType.Water}};
    private final Map map = new BasicMap(tiles); // initialize a BasicMap based on the tiles above
    private final HTMLGenerator htmlGenerator = HTMLGenerator.getHTMLGenerator(); // get HTMLGenerator singleton instance

    /**
     * Initialises a Player and defines a path on the map, before every unit test.
     * @throws NullPositionException is thrown whenever the start_position is not set (unexpected).
     */
    @Before
    public void setupHTMLGeneratorTest() throws NullPositionException{
        player = new Player(); // initialising new player
        player.setStartPosition(new Position(0,0));// setting to origin

        // defining simple path traversed by player on the map, to setup history of moves
        player.setPosition(new Position(1, 0));
        player.setPosition(new Position(1,1));
        player.setPosition(new Position(2,1));
        player.setPosition(new Position(2,2));
    }

    /**
     * Tests whether the same instance is returned when attempting to create a new instance (i.e. testing the singleton
     * pattern).
     */
    @Test
    public void singletonTest(){
        HTMLGenerator new_htmlGenerator = HTMLGenerator.getHTMLGenerator();
        assertEquals(htmlGenerator, new_htmlGenerator);
    }

    /* ----- This section tests the overall structure of the HTML file -----
     * This is done as agnostically as possible, to ensure that the file is generated as expected. It is assumed that
     * the returned array by htmlGenerator.genPlayerMap(player, map)  follows the structure outlined in the class
     * documentation.
     */

    /**
     * Testing whether the generated HTML map is coloured correctly, based on the TileType definition for each visited tile.
     */
    @Test
    public void visited_tilesTest(){
        ArrayList<String> html = htmlGenerator.genPlayerMap(player, map);

        int idx;
        for(Position position : player.getPositionHistory()){ // for each visited position by the player
            idx = 3 + 5*position.y + position.x; // calculate linearised 1D index corresponding to 2D coordinates
            // check if map is correctly coloured
            assertEquals("<div class=\"" + map.getTileType(position.x, position.y).html_handle + "\"></div>\n",
                    html.get(idx));
        }
    }

    /**
     * Testing whether the generated HTML map is left uncovered for the unvisited tiles.
     */
    @Test
    public void unvisited_tilesTest(){
        ArrayList<String> html = htmlGenerator.genPlayerMap(player, map);

        // the unvisited positions on the map
        int[][] unvisited_pos = {{0, 1}, {0, 1}, {0, 3}, {0, 4},
                                 {1, 2}, {1, 3}, {1, 4},
                                 {2, 0}, {2, 3}, {2, 4},
                                 {3, 0}, {3, 1}, {3, 2}, {3, 3}, {3, 4},
                                 {4, 0}, {4, 1}, {4, 2}, {4, 3}, {4, 4}};

        int idx;
        for(int[] position : unvisited_pos){ // for each unvisited position by the player
            idx = 3 + 5*position[1] + position[0]; // calculate linearised 1D index corresponding to 2D coordinates
            // check if map is correctly coloured
            assertEquals("<div class=\"uncovered\"></div>\n", html.get(idx));
        }
    }

    /**
     * Testing whether the starting tile is marked with the correct symbol (a Torii Gate).
     */
    @Test
    public void start_tileTest(){
        ArrayList<String> html = htmlGenerator.genPlayerMap(player, map);
        Position start_position = player.getStartPosition();

        assertEquals("<div class=\"" + map.getTileType(start_position.x, start_position.y).html_handle +
                        "\">&#26E9</div>\n", html.get(4));
    }

    /**
     * Testing whether the current tile is marked with the correct symbol (a gleaming Sun).
     */
    @Test
    public void current_tileTest(){
        ArrayList<String> html = htmlGenerator.genPlayerMap(player, map);
        Position curr_position = player.getPosition();

        assertEquals("<div class=\"" + map.getTileType(curr_position.x, curr_position.y).html_handle +
                        "\">&#1F31E</div>\n", html.get(8));
    }

    @After
    public void teardownHTMLGeneratorTest(){
        player = null; // dereference
    }
}
