package com.xd.cps2002.game;

import com.xd.cps2002.game.game_exceptions.InvalidMapSizeException;
import com.xd.cps2002.game.game_exceptions.InvalidNumberOfPlayersException;
import com.xd.cps2002.game.game_exceptions.SetupOperationPrecedenceException;
import com.xd.cps2002.map.BasicMap;
import com.xd.cps2002.map.TileType;
import com.xd.cps2002.player.Player;
import com.xd.cps2002.player.Position;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
     * @throws InvalidNumberOfPlayersException is thrown if the number of players is {@literal <} 2 or {@literal >} 8 (expected).
     * @throws SetupOperationPrecedenceException is thrown if a Map instances has already been created (not expected).
     */
    @Test(expected = InvalidNumberOfPlayersException.class)
    public void belowMin_NoOfPlayers_setupPlayersTest() throws InvalidNumberOfPlayersException{
        int n_players = 1;
        mainGame.setupPlayers(n_players);
    }

    /**
     * Tests that an InvalidNumberOfPlayersException is thrown when the number of players is greater than 8.
     * @throws InvalidNumberOfPlayersException is thrown if the number of players is {@literal <} 2 or {@literal >} 8 (expected).
     * @throws SetupOperationPrecedenceException is thrown if a Map instances has already been created (not expected).
     */
    @Test(expected = InvalidNumberOfPlayersException.class)
    public void aboveMax_NoOfPlayers_setupPlayersTest() throws InvalidNumberOfPlayersException{
        int n_players = 9;
        mainGame.setupPlayers(n_players);
    }

    /**
     * Tests that the players array is initialized with the correct number of Player instances if input is valid.
     * @throws InvalidNumberOfPlayersException is thrown if the number of players is {@literal <} 2 or {@literal >} 8 (not expected).
     * @throws SetupOperationPrecedenceException is thrown if a Map instances has already been created (not expected).
     */
    @Test
    public void inrange_NoOfPlayers_setupPlayersTest() throws InvalidNumberOfPlayersException{
        int n_players = 5;
        mainGame.setupPlayers(n_players);

        assertEquals(5, mainGame.players.length);
        for(int i = 0; i < 5; i++){
            assertNotNull(mainGame.players[i]);
        }
    }

    /**
     * Tests that a SetupOperationPrecedenceException is thrown when a Map has already been initialised before the Players.
     * @throws SetupOperationPrecedenceException is thrown if a Map instances has already been created (expected).
     * @throws InvalidNumberOfPlayersException is thrown if the number of players is {@literal <} 2 or {@literal >} 8 (not expected).
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
     * Tests that a map of the appropriate size is initialized.
     * @throws InvalidMapSizeException is thrown if the map_size is invalid (not expected).
     * @throws SetupOperationPrecedenceException is thrown if MainGame.players is null (not expected).
     */
    @Test
    public void inrange_MapSize_setupMapTest() throws InvalidMapSizeException{
        int map_size = 5;
        mainGame.setupMap(map_size);

        assertNotNull(mainGame.map);
        assertEquals(5, mainGame.map.getSize());
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
     * Tests that all player positions have been initialized correctly.
     * @throws SetupOperationPrecedenceException is thrown if no Players or Map has been initialized (expected).
     */
    @Test
    public void correct_setPlayerPositionsTest(){
        mainGame.players = new Player[5];
        for(int i = 0; i < 5; i++){
            mainGame.players[i] = new Player();
        }

        mainGame.map = new BasicMap(tiles);
        mainGame.map.isPlayable();

        mainGame.setPlayerPositions();
        for(int i = 0; i < 5; i++){
            assertNotNull(mainGame.players[i].getStartPosition());
        }
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
     * Tests that a SetupOperationPrecedenceException is thrown when getMoves() is called and MainGame.initialized is
     * false.
     * @throws SetupOperationPrecedenceException is thrown when there is an attempted call to getMoves() before a call
     * to initializeGame() (expected).
     */
    @Test(expected = SetupOperationPrecedenceException.class)
    public void notInitialized_getMovesTest(){
        mainGame.initialized = false;
        mainGame.getMoves();
    }

    /**
     * Tests that a SetupOperationPrecedenceException is thrown when updateGameState() is called and MainGame.initialized
     * is false.
     * @throws SetupOperationPrecedenceException is thrown when there is an attempted call to updateGameState() before a
     * call to initializeGame() (expected).
     */
    @Test(expected = SetupOperationPrecedenceException.class)
    public void notInitialized_updateGameStateTest(){
        mainGame.initialized = false;
        mainGame.updateGameState();
    }

    /**
     * Tests that if multiple players are currently at the treasure tile, then they are all returned.
     * @throws SetupOperationPrecedenceException is thrown when there is an attempted call to updateGameState() before a
     * call to initializeGame() (not expected).
     */
    @Test
    public void multipleWinners_updateGameStateTest(){
        // initialize a number of players
        mainGame.players = new Player[3];
        mainGame.players[0] = new Player();
        mainGame.players[0].setStartPosition(new Position(2, 2)); // at treasure tile
        mainGame.players[1] = new Player();
        mainGame.players[1].setStartPosition(new Position(0, 0)); // not at treasure tile
        mainGame.players[2] = new Player();
        mainGame.players[2].setStartPosition(new Position(2, 2)); // at treasure tile

        mainGame.map = new BasicMap(tiles);
        mainGame.map.isPlayable();

        mainGame.initialized = true;
        ArrayList<Integer> winners = mainGame.updateGameState();

        assertEquals(2, winners.size());
        assertEquals(mainGame.players[0].get_pID(), (int) winners.get(0));
        assertEquals(mainGame.players[2].get_pID(), (int) winners.get(1));
    }

    /**
     * Tests that a player is reset if they land on a water tile.
     * @throws SetupOperationPrecedenceException is thrown when there is an attempted call to updateGameState() before a
     * call to initializeGame() (not expected).
     */
    @Test
    public void deadPlayer_updateGameStateTest(){
        // initialize a number of players
        mainGame.players = new Player[1];
        mainGame.players[0] = new Player();
        mainGame.players[0].setStartPosition(new Position(0, 2)); // at grass tile
        mainGame.players[0].setPosition(new Position(1, 2)); // at water tile

        mainGame.map = new BasicMap(tiles);
        mainGame.map.isPlayable();

        mainGame.initialized = true;
        mainGame.updateGameState();

        assertTrue(mainGame.players[0].getPosition().x == 0 && mainGame.players[0].getPosition().y == 2);
    }

    /**
     * Tests that a SetupOperationPrecedenceException is thrown when startGame() is called and MainGame.initialized is
     * false.
     * @throws SetupOperationPrecedenceException is thrown when there is an attempted call to startGame() before a call
     * to initializeGame() (expected).
     */
    @Test(expected = SetupOperationPrecedenceException.class)
    public void notInitialized_startGameTest(){
        mainGame.initialized = false;
        mainGame.startGame();
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
        assertNull(mainGame.dir);
        assertFalse(mainGame.initialized);
    }

    @After
    public void teardownMainGameTest(){
        mainGame.players = null; //dereference
        mainGame.map = null;
        mainGame.dir = null;
        mainGame.initialized = false;
    }
}
