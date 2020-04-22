package com.xd.cps2002;

import com.xd.cps2002.map.BasicMap;
import com.xd.cps2002.map.TileType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Tester class for the main game loop logic.
 * @author Xandru Mifsud
 */
public class MainGameTest{
    private final MainGame mainGame = MainGame.getMainGame();

    // define the Map by means of a 2D TileType array
    private final TileType[][] tiles = {{TileType.Grass, TileType.Grass, TileType.Grass, TileType.Grass, TileType.Grass},
                                        {TileType.Grass, TileType.Grass, TileType.Water, TileType.Water, TileType.Water},
                                        {TileType.Grass, TileType.Grass, TileType.Treasure, TileType.Water, TileType.Water},
                                        {TileType.Grass, TileType.Grass, TileType.Grass, TileType.Water, TileType.Water},
                                        {TileType.Grass, TileType.Grass, TileType.Grass, TileType.Grass, TileType.Water}};

    @Before
    public void setupMainGameTest(){
        mainGame.players = new Player[2]; // initialize two players
    }

    /**
     * Tests whether the same instance is returned when attempting to create a new instance (i.e. testing the singleton
     * pattern).
     */
    @Test
    public void singletonTest(){
        MainGame new_mainGame = MainGame.getMainGame();
        assertEquals(mainGame, new_mainGame);
    }

    /**
     * Tests that an InvalidNumberOfPlayersException is thrown when the number of players is less than 2.
     * @throws InvalidNumberOfPlayersException is thrown if the number of players is < 2 or > 8 (expected).
     * @throws SetupOperationPrecedenceException is thrown if a Map instances has already been created (not expected).
     */
    @Test(expected = InvalidNumberOfPlayersException.class)
    public void belowMin_NoOfPlayers_setupPlayersTest() throws InvalidNumberOfPlayersException{
        int n_players = 1;
        mainGame.setupPlayers(n_players);
    }

    /**
     * Tests that an InvalidNumberOfPlayersException is thrown when the number of players is greater than 8.
     * @throws InvalidNumberOfPlayersException is thrown if the number of players is < 2 or > 8 (expected).
     * @throws SetupOperationPrecedenceException is thrown if a Map instances has already been created (not expected).
     */
    @Test(expected = InvalidNumberOfPlayersException.class)
    public void aboveMax_NoOfPlayers_setupPlayersTest() throws InvalidNumberOfPlayersException{
        int n_players = 9;
        mainGame.setupPlayers(n_players);
    }

    /**
     * Tests that a SetupOperationPrecedenceException is thrown when a Map has already been initialised before the Players.
     * @throws SetupOperationPrecedenceException is thrown if a Map instances has already been created (expected).
     * @throws InvalidNumberOfPlayersException is thrown if the number of players is < 2 or > 8 (not expected).
     */
    @Test(expected = SetupOperationPrecedenceException.class)
    public void mapInitializedBeforePlayers_setupPlayersTest() throws InvalidNumberOfPlayersException{
        mainGame.map = new BasicMap(tiles);
        mainGame.setupPlayers(5);
    }

    /**
     * Tests that a SetupOperationPrecedenceException is thrown when no players have been initialized before Map init.
     * @throws SetupOperationPrecedenceException is thrown if MainGame.players is null (expected).
     * @throws InvalidMapSizeException is thrown if the map_size is invalid (not expected).
     */
    @Test(expected = SetupOperationPrecedenceException.class)
    public void noPlayersInitialized_setupMapTest() throws InvalidMapSizeException{
        mainGame.players = null;
        mainGame.setupMap(8);
    }

    /**
     * Tests that an InvalidMapSizeException is thrown when the map_size is less than 5.
     * @throws InvalidMapSizeException is thrown if the map_size is invalid (expected).
     * @throws SetupOperationPrecedenceException is thrown if MainGame.players is null (not expected).
     */
    @Test(expected = InvalidMapSizeException.class)
    public void belowMin_MapSize_setupMapTest() throws InvalidMapSizeException{
        int map_size = 4;
        mainGame.setupMap(map_size);
    }

    /**
     * Tests that an InvalidMapSizeException is thrown when the map_size is greater than 50.
     * @throws InvalidMapSizeException is thrown if the map_size is invalid (expected).
     * @throws SetupOperationPrecedenceException is thrown if MainGame.players is null (not expected).
     */
    @Test(expected = InvalidMapSizeException.class)
    public void aboveMax_MapSize_setupMapTest() throws InvalidMapSizeException{
        int map_size = 51;
        mainGame.setupMap(map_size);
    }

    /**
     * Tests that an InvalidMapSizeException is thrown when the map_size is greater than 5 but less than 8 for 5 or more
     * initialized players.
     * @throws InvalidMapSizeException is thrown if the map_size is invalid (expected).
     * @throws SetupOperationPrecedenceException is thrown if MainGame.players is null (not expected).
     */
    @Test(expected = InvalidMapSizeException.class)
    public void smallMap_Min5Players_setupMapTest() throws InvalidMapSizeException{
        mainGame.players = new Player[5];

        int map_size = 5;
        mainGame.setupMap(map_size);
    }

    /**
     * Tests that a SetupOperationPrecedenceException is thrown when no Players have been initialized.
     * @throws SetupOperationPrecedenceException is thrown if no Players or Map has been initialized (expected).
     */
    @Test(expected = SetupOperationPrecedenceException.class)
    public void noPlayersInitialized_setPlayerPositionsTest(){
        mainGame.map = new BasicMap(tiles);

        mainGame.players = null;
        mainGame.setPlayerPositions();
    }

    /**
     * Tests that a SetupOperationPrecedenceException is thrown when no Map instance has been initialized.
     * @throws SetupOperationPrecedenceException is thrown if no Players or Map has been initialized (expected).
     */
    @Test(expected = SetupOperationPrecedenceException.class)
    public void noMapInitialized_setPlayerPositionsTest(){
        mainGame.players = new Player[5];

        mainGame.map = null;
        mainGame.setPlayerPositions();
    }

    /**
     * Tests that a SetupOperationPrecedenceException is thrown when no directory path has been specified.
     * @throws SetupOperationPrecedenceException is thrown if no directory path or Map has been initialized (expected).
     * @throws IOException is thrown when there is a failure in persisting to disk [generally fatal] (not expected).
     */
    @Test(expected = SetupOperationPrecedenceException.class)
    public void noDirPathSpecified_writeHTMLFilesTest() throws IOException{
        mainGame.map = new BasicMap(tiles);
        mainGame.dir = null;

        mainGame.writeHTMLFiles();
    }

    /**
     * Tests that a SetupOperationPrecedenceException is thrown when no Map instance has been initialized.
     * @throws SetupOperationPrecedenceException is thrown if no directory path or Map has been initialized (expected).
     * @throws IOException is thrown when there is a failure in persisting to disk [generally fatal] (not expected).
     */
    @Test(expected = SetupOperationPrecedenceException.class)
    public void noMapInitialized_writeHTMLFilesTest() throws IOException{
        mainGame.map = null;
        mainGame.writeHTMLFiles();
    }

    /**
     * Asserts that the MainGame.players and MainGame.map are set to null.
     */
    @Test
    public void mainGame_resetTest(){
        mainGame.reset();

        assertNull(mainGame.players);
        assertNull(mainGame.map);
    }

    @After
    public void teardownMainGameTest(){
        mainGame.players = null; //dereference
    }
}
