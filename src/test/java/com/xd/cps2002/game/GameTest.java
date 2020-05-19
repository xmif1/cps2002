package com.xd.cps2002.game;

import com.xd.cps2002.game.game_exceptions.*;
import com.xd.cps2002.player.*;
import com.xd.cps2002.map.BasicMap;
import com.xd.cps2002.map.TileType;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tester class for the Game API logic.
 * @author Xandru Mifsud
 */
public class GameTest {
    private Game game;

    // define the Map by means of a 2D TileType array
    private final TileType[][] tiles = {{TileType.Grass, TileType.Grass, TileType.Grass, TileType.Grass, TileType.Grass},
                                        {TileType.Grass, TileType.Grass, TileType.Water, TileType.Water, TileType.Water},
                                        {TileType.Grass, TileType.Grass, TileType.Treasure, TileType.Water, TileType.Water},
                                        {TileType.Grass, TileType.Grass, TileType.Grass, TileType.Water, TileType.Water},
                                        {TileType.Grass, TileType.Grass, TileType.Grass, TileType.Grass, TileType.Water}};

    @Before
    public void setupMainGameTest(){
        game = Game.getMainGame();
    }

    /**
     * Tests whether the same instance is returned when attempting to create a new instance (i.e. testing the singleton
     * pattern).
     */
    @Test
    public void singletonTest(){
        Game new_Game = Game.getMainGame();
        assertEquals(game, new_Game);
    }

    /**
     * Tests that an InvalidNumberOfPlayersException is thrown when the number of players is less than 2.
     * @throws InvalidNumberOfPlayersException is thrown if the number of players is {@literal <} 2 or {@literal >} 8 (expected).
     * @throws SetupOperationPrecedenceException is thrown if a Map instances has already been created (not expected).
     */
    @Test(expected = InvalidNumberOfPlayersException.class)
    public void belowMin_NoOfPlayers_setupPlayersTest() throws InvalidNumberOfPlayersException{
        int n_players = 1;
        game.setPlayers(n_players);
    }

    /**
     * Tests that an InvalidNumberOfPlayersException is thrown when the number of players is greater than 8.
     * @throws InvalidNumberOfPlayersException is thrown if the number of players is {@literal <} 2 or {@literal >} 8 (expected).
     * @throws SetupOperationPrecedenceException is thrown if a Map instances has already been created (not expected).
     */
    @Test(expected = InvalidNumberOfPlayersException.class)
    public void aboveMax_NoOfPlayers_setupPlayersTest() throws InvalidNumberOfPlayersException{
        int n_players = 9;
        game.setPlayers(n_players);
    }

    /**
     * Tests that the players array is initialized with the correct number of Player instances if input is valid.
     * @throws InvalidNumberOfPlayersException is thrown if the number of players is {@literal <} 2 or {@literal >} 8 (not expected).
     * @throws SetupOperationPrecedenceException is thrown if a Map instances has already been created (not expected).
     */
    @Test
    public void inrange_NoOfPlayers_setupPlayersTest() throws InvalidNumberOfPlayersException{
        int n_players = 5;
        game.setPlayers(n_players);

        assertEquals(5, game.getPlayers().length);
        for(int i = 0; i < 5; i++){
            assertNotNull(game.getPlayers()[i]);
        }
    }

    /**
     * Tests that a SetupOperationPrecedenceException is thrown when a Map has already been initialised before the Players.
     * @throws SetupOperationPrecedenceException is thrown if a Map instances has already been created (expected).
     * @throws InvalidNumberOfPlayersException is thrown if the number of players is {@literal <} 2 or {@literal >} 8 (not expected).
     */
    @Test(expected = SetupOperationPrecedenceException.class)
    public void mapInitializedBeforePlayers_setupPlayersTest() throws InvalidNumberOfPlayersException{
        game.map = new BasicMap(tiles);
        game.setPlayers(5);
    }

    /**
     * Tests that a SetupOperationPrecedenceException is thrown when no players have been initialized before Map init.
     * @throws SetupOperationPrecedenceException is thrown if MainGame.players is null (expected).
     * @throws InvalidMapSizeException is thrown if the map_size is invalid (not expected).
     */
    @Test(expected = SetupOperationPrecedenceException.class)
    public void noPlayersInitialized_setupMapTest() throws InvalidMapSizeException{
        game.setMap(8);
    }

    /**
     * Tests that an InvalidMapSizeException is thrown when the map_size is less than 5.
     * @throws InvalidMapSizeException is thrown if the map_size is invalid (expected).
     * @throws SetupOperationPrecedenceException is thrown if MainGame.players is null (not expected).
     */
    @Test(expected = InvalidMapSizeException.class)
    public void belowMin_MapSize_setupMapTest() throws InvalidMapSizeException{
        int map_size = 4;
        game.setMap(map_size);
    }

    /**
     * Tests that an InvalidMapSizeException is thrown when the map_size is greater than 50.
     * @throws InvalidMapSizeException is thrown if the map_size is invalid (expected).
     * @throws SetupOperationPrecedenceException is thrown if MainGame.players is null (not expected).
     */
    @Test(expected = InvalidMapSizeException.class)
    public void aboveMax_MapSize_setupMapTest() throws InvalidMapSizeException{
        int map_size = 51;
        game.setMap(map_size);
    }

    /**
     * Tests that an InvalidMapSizeException is thrown when the map_size is greater than 5 but less than 8 for 5 or more
     * initialized players.
     * @throws InvalidMapSizeException is thrown if the map_size is invalid (expected).
     * @throws SetupOperationPrecedenceException is thrown if MainGame.players is null (not expected).
     */
    @Test(expected = InvalidMapSizeException.class)
    public void smallMap_Min5Players_setupMapTest() throws InvalidMapSizeException{
        game.players = new Player[5];

        int map_size = 5;
        game.setMap(map_size);
    }

    /**
     * Tests that a map of the appropriate size is initialized.
     * @throws InvalidMapSizeException is thrown if the map_size is invalid (not expected).
     * @throws SetupOperationPrecedenceException is thrown if MainGame.players is null (not expected).
     */
    @Test
    public void inrange_MapSize_setupMapTest() throws InvalidMapSizeException{
        int map_size = 5;
        game.setMap(map_size);

        assertNotNull(game.getMap());
        assertEquals(5, game.getMap().getSize());
    }

    /**
     * Tests that a SetupOperationPrecedenceException is thrown when no Players have been initialized.
     * @throws SetupOperationPrecedenceException is thrown if no Players or Map has been initialized (expected).
     */
    @Test(expected = SetupOperationPrecedenceException.class)
    public void noPlayersInitialized_setPlayerPositionsTest(){
        game.map = new BasicMap(tiles);

        game.setPlayerPositions();
    }

    /**
     * Tests that a SetupOperationPrecedenceException is thrown when no Map instance has been initialized.
     * @throws SetupOperationPrecedenceException is thrown if no Players or Map has been initialized (expected).
     */
    @Test(expected = SetupOperationPrecedenceException.class)
    public void noMapInitialized_setPlayerPositionsTest(){
        game.players = new Player[5];

        game.setPlayerPositions();
    }

    /**
     * Tests that all player positions have been initialized correctly.
     * @throws SetupOperationPrecedenceException is thrown if no Players or Map has been initialized (expected).
     */
    @Test
    public void correct_setPlayerPositionsTest(){
        game.players = new Player[5];
        for(int i = 0; i < 5; i++){
            game.getPlayers()[i] = new Player();
        }

        game.map = new BasicMap(tiles);
        game.getMap().isPlayable();

        game.setPlayerPositions();
        for(int i = 0; i < 5; i++){
            assertNotNull(game.getPlayers()[i].getStartPosition());
        }
    }

    /**
     * Tests that a SetupOperationPrecedenceException is thrown when no directory path has been specified.
     * @throws SetupOperationPrecedenceException is thrown if no directory path or Map has been initialized (expected).
     * @throws IOException is thrown when there is a failure in persisting to disk [generally fatal] (not expected).
     */
    @Test(expected = SetupOperationPrecedenceException.class)
    public void noDirPathSpecified_writeHTMLFilesTest() throws IOException{
        game.map = new BasicMap(tiles);
        game.dir = null;

        game.writeHTMLFiles();
    }

    /**
     * Tests that a SetupOperationPrecedenceException is thrown when no Map instance has been initialized.
     * @throws SetupOperationPrecedenceException is thrown if no directory path or Map has been initialized (expected).
     * @throws IOException is thrown when there is a failure in persisting to disk [generally fatal] (not expected).
     */
    @Test(expected = SetupOperationPrecedenceException.class)
    public void noMapInitialized_writeHTMLFilesTest() throws IOException{
        game.writeHTMLFiles();
    }

    @After
    public void teardownMainGameTest(){
        game.reset();
    }
}
