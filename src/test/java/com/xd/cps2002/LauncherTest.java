package com.xd.cps2002;

import com.xd.cps2002.game.Game;
import com.xd.cps2002.game.game_exceptions.*;
import com.xd.cps2002.map.Map;
import com.xd.cps2002.map.MapCreator;
import com.xd.cps2002.player.*;
import com.xd.cps2002.map.BasicMap;
import com.xd.cps2002.map.TileType;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tester class for the Launcher game loop logic.
 * @author Xandru Mifsud
 */
public class LauncherTest{
    private Game game;

    // define the Map by means of a 2D TileType array
    private final TileType[][] tiles = {{TileType.Grass, TileType.Grass, TileType.Grass, TileType.Grass, TileType.Grass},
                                        {TileType.Grass, TileType.Grass, TileType.Water, TileType.Water, TileType.Water},
                                        {TileType.Grass, TileType.Grass, TileType.Treasure, TileType.Water, TileType.Water},
                                        {TileType.Grass, TileType.Grass, TileType.Grass, TileType.Water, TileType.Water},
                                        {TileType.Grass, TileType.Grass, TileType.Grass, TileType.Grass, TileType.Water}};

    @Before
    public void setupLauncherTest(){
        game = Game.getGame();
    }

    /**
     * Tests that a SetupOperationPrecedenceException is thrown when getMoves() is called and MainGame.initialized is
     * false.
     * @throws SetupOperationPrecedenceException is thrown when there is an attempted call to getMoves() before a call
     * to initializeGame() (expected).
     */
    @Test(expected = SetupOperationPrecedenceException.class)
    public void notInitialized_getMovesTest(){
        Launcher.getMoves(game);
    }

    /**
     * Tests that a SetupOperationPrecedenceException is thrown when updateGameState() is called and MainGame.initialized
     * is false.
     * @throws SetupOperationPrecedenceException is thrown when there is an attempted call to updateGameState() before a
     * call to initializeGame() (expected).
     */
    @Test(expected = SetupOperationPrecedenceException.class)
    public void notInitialized_updateGameStateTest(){
        Launcher.updateGameState(game, new Player());
    }

    /**
     * Tests that if multiple players are currently at the treasure tile, then they are all returned.
     * @throws SetupOperationPrecedenceException is thrown when there is an attempted call to updateGameState() before a
     * call to initializeGame() (not expected).
     */
    @Test
    public void multipleWinners_updateGameStateTest(){
        // initialize a number of players
        Player[] players = new Player[3];
        players[0] = new Player();
        players[0].setStartPosition(new Position(2, 2)); // at treasure tile
        players[1] = new Player();
        players[1].setStartPosition(new Position(0, 0)); // not at treasure tile
        players[2] = new Player();
        players[2].setStartPosition(new Position(2, 2)); // at treasure tile

        // initialise a map instance
        Map map = MapCreator.createMap("basic", tiles);
        map.isPlayable();

        // set the necessary state variables in Game instance
        game.setPlayers(players);
        game.setMap(map);

        // explicitly set isInitialised to true for testing
        Launcher.isInitialised = true;

        ArrayList<Integer> winners = new ArrayList<Integer>();
        // update game state for each player
        Launcher.updateGameState(game, players[0]);
        Launcher.updateGameState(game, players[1]);
        winners = Launcher.updateGameState(game, players[2]);

        assertEquals(2, winners.size());
        assertEquals(game.getPlayers()[0].get_pID(), (int) winners.get(0));
        assertEquals(game.getPlayers()[2].get_pID(), (int) winners.get(1));
    }

    /**
     * Tests that a player is reset if they land on a water tile.
     * @throws SetupOperationPrecedenceException is thrown when there is an attempted call to updateGameState() before a
     * call to initializeGame() (not expected).
     */
    @Test
    public void deadPlayer_updateGameStateTest(){
        // initialise a number of players
        Player[] players = new Player[1];
        players[0] = new Player();
        players[0].setStartPosition(new Position(0, 2)); // at grass tile
        players[0].setTeam(new Team());
        players[0].setPosition(new Position(1, 2)); // at water tile

        // initialise a map instance
        Map map = MapCreator.createMap("basic", tiles);;
        map.isPlayable();

        // set the necessary state variables in Game instance
        game.setPlayers(players);
        game.setMap(map);

        // explicitly set isInitialised to true for testing
        Launcher.isInitialised = true;

        Launcher.updateGameState(game, players[0]);

        assertEquals(0, game.getPlayers()[0].getPosition().x);
        assertEquals(2, game.getPlayers()[0].getPosition().y);
    }

    /**
     * Tests that a SetupOperationPrecedenceException is thrown when startGame() is called and MainGame.initialized is
     * false.
     * @throws SetupOperationPrecedenceException is thrown when there is an attempted call to startGame() before a call
     * to initializeGame() (expected).
     */
    @Test(expected = SetupOperationPrecedenceException.class)
    public void notInitialized_startGameTest(){
        Launcher.startGame(game);
    }

    @After
    public void teardownLauncherTest(){
        Launcher.isInitialised = false;
        game.reset();
    }
}
