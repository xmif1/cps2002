package com.xd.cps2002;

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

    @Before
    public void setupMainGameTest(){
        MainGame.players = new Player[2]; // initialize two players
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
     */
    @Test(expected = InvalidNumberOfPlayersException.class)
    public void belowMin_NoOfPlayers_setupPlayersTest() throws InvalidNumberOfPlayersException{
        int n_players = 1;
        mainGame.setupPlayers(n_players);
    }

    /**
     * Tests that an InvalidNumberOfPlayersException is thrown when the number of players is greater than 8.
     * @throws InvalidNumberOfPlayersException is thrown if the number of players is < 2 or > 8 (expected).
     */
    @Test(expected = InvalidNumberOfPlayersException.class)
    public void aboveMax_NoOfPlayers_setupPlayersTest() throws InvalidNumberOfPlayersException{
        int n_players = 9;
        mainGame.setupPlayers(n_players);
    }

    /**
     * Tests that an InvalidNumberOfPlayersException is thrown when no players have been initialized.
     * @throws InvalidNumberOfPlayersException is thrown if MainGame.players is null (expected).
     * @throws InvalidMapSizeException is thrown if the map_size is invalid (not expected).
     */
    @Test(expected = InvalidNumberOfPlayersException.class)
    public void noPlayersInitialized_setupMapTest() throws InvalidNumberOfPlayersException, InvalidMapSizeException{
        MainGame.players = null;

        mainGame.setupMap(8);
    }

    /**
     * Tests that an InvalidMapSizeException is thrown when the map_size is less than 5.
     * @throws InvalidMapSizeException is thrown if the map_size is invalid (expected).
     * @throws InvalidNumberOfPlayersException is thrown if MainGame.players is null (not expected).
     */
    @Test(expected = InvalidMapSizeException.class)
    public void belowMin_MapSize_setupMapTest() throws InvalidMapSizeException, InvalidNumberOfPlayersException{
        int map_size = 4;
        mainGame.setupMap(map_size);
    }

    /**
     * Tests that an InvalidMapSizeException is thrown when the map_size is greater than 50.
     * @throws InvalidMapSizeException is thrown if the map_size is invalid (expected).
     * @throws InvalidNumberOfPlayersException is thrown if MainGame.players is null (not expected).
     */
    @Test(expected = InvalidMapSizeException.class)
    public void aboveMax_MapSize_setupMapTest() throws InvalidMapSizeException, InvalidNumberOfPlayersException{
        int map_size = 51;
        mainGame.setupMap(map_size);
    }

    /**
     * Tests that an InvalidMapSizeException is thrown when the map_size is greater than 5 but less than 8 for 5 or more
     * initialized players.
     * @throws InvalidMapSizeException is thrown if the map_size is invalid (expected).
     * @throws InvalidNumberOfPlayersException is thrown if MainGame.players is null (not expected).
     */
    @Test(expected = InvalidMapSizeException.class)
    public void smallMap_Min5Players_setupMapTest() throws InvalidMapSizeException, InvalidNumberOfPlayersException{
        MainGame.players = new Player[5];

        int map_size = 5;
        mainGame.setupMap(map_size);
    }

    @After
    public void teardownMainGameTest(){
        MainGame.players = null; //dereference
    }
}
