package com.xd.cps2002.game;

import java.util.ArrayList;

import com.xd.cps2002.map.BasicMap;
import com.xd.cps2002.map.Map;
import com.xd.cps2002.map.MapCreator;
import com.xd.cps2002.map.TileType;
import com.xd.cps2002.player.Team;
import com.xd.cps2002.player.player_exceptions.NullPositionException;
import com.xd.cps2002.player.player_exceptions.NullTeamException;
import com.xd.cps2002.player.Player;
import com.xd.cps2002.player.Position;

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
    private final Map map = MapCreator.createMap("basic", tiles); // initialize a BasicMap based on the tiles above
    private final HTMLGenerator htmlGenerator = HTMLGenerator.getHTMLGenerator(); // get HTMLGenerator singleton instance

    /**
     * Initialises a Player and defines a path on the map, before every unit test.
     * @throws NullPositionException is thrown whenever the start_position is not set (unexpected).
     * @throws NullTeamException is thrown when Team team is null, i.e. when it has not been set.
     */
    @Before
    public void setupHTMLGeneratorTest(){
        player = new Player(); // initialising new player
        player.setStartPosition(new Position(0,0));// setting to origin
        player.setTeam(new Team()); // initialise with a Team instance

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
     * Testing whether the generated HTML map is coloured correctly, based on the TileType definition for each visited
     * tile, except the starting tile and the current tile.
     * @throws IllegalArgumentException whenever the Player or Map instances passed are null (not expected).
     * @throws NullTeamException is thrown when Team team is null, i.e. when it has not been set (not expected).
     */
    @Test
    public void visited_tilesTest(){
        ArrayList<String> html = htmlGenerator.genPlayerMap(player, map);
        int[][] visited_pos = {{1, 0}, {1, 1}, {2, 1}};

        int idx;
        for(int[] position : visited_pos){ // for each visited position by the player
            idx = 3 + 5*position[1] + position[0]; // calculate linearised 1D index corresponding to 2D coordinates
            // check if map is correctly coloured
            assertEquals("<div class=\"" + tiles[position[0]][position[1]].html_handle + "\"></div>\n",
                    html.get(idx));
        }
    }

    /**
     * Testing whether the generated HTML map is left uncovered for the unvisited tiles.
     * @throws IllegalArgumentException whenever the Player or Map instances passed are null (not expected).
     * @throws NullTeamException is thrown when Team team is null, i.e. when it has not been set (not expected).
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
     * @throws IllegalArgumentException whenever the Player or Map instances passed are null (not expected).
     * @throws NullTeamException is thrown when Team team is null, i.e. when it has not been set (not expected).
     */
    @Test
    public void start_tileTest(){
        ArrayList<String> html = htmlGenerator.genPlayerMap(player, map);
        assertEquals("<div class=\"" + tiles[0][0].html_handle + "\">&#x26E9;</div>\n", html.get(3));
    }

    /**
     * Testing whether the current tile is marked with the correct symbol (a gleaming Sun).
     * @throws IllegalArgumentException whenever the Player or Map instances passed are null (not expected).
     * @throws NullTeamException is thrown when Team team is null, i.e. when it has not been set (not expected).
     */
    @Test
    public void current_tileTest(){
        ArrayList<String> html = htmlGenerator.genPlayerMap(player, map);
        assertEquals("<div class=\"" + tiles[2][2].html_handle + "\">&#x1F31E;</div>\n", html.get(15));
    }

    /**
     * Testing whether an IllegalArgumentException is raised when genPlayerMap is called with a null Player instance.
     * @throws IllegalArgumentException whenever the Player or Map instances passed are null (expected).
     * @throws NullTeamException is thrown when Team team is null, i.e. when it has not been set (not expected).
     */
    @Test(expected = IllegalArgumentException.class)
    public void nullPlayer_genPlayerMapTest(){
        htmlGenerator.genPlayerMap(null, map);
    }

    /**
     * Testing whether an IllegalArgumentException is raised when genPlayerMap is called with a null Map instance.
     * @throws IllegalArgumentException whenever the Player or Map instances passed are null (expected).
     * @throws NullTeamException is thrown when Team team is null, i.e. when it has not been set (not expected).
     */
    @Test(expected = IllegalArgumentException.class)
    public void nullMap_genPlayerMapTest(){
        htmlGenerator.genPlayerMap(player, null);
    }

    /**
     * Testing whether a NullTeamException is raised when genPlayerMap is called with a Player having no Team instance.
     * @throws IllegalArgumentException whenever the Player or Map instances passed are null (not expected).
     * @throws NullTeamException is thrown when Team team is null, i.e. when it has not been set (expected).
     */
    @Test(expected = NullTeamException.class)
    public void playerNullTeam_genPlayerMapTest(){
        Player player2 = new Player();
        player2.setStartPosition(new Position(0, 0));

        htmlGenerator.genPlayerMap(player2, map);
    }

    @After
    public void teardownHTMLGeneratorTest(){
        player = null; // dereference
    }
}
