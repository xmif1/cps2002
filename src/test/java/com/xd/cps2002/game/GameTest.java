package com.xd.cps2002.game;

import com.xd.cps2002.game.game_exceptions.*;
import com.xd.cps2002.map.Map;
import com.xd.cps2002.map.MapCreator;
import com.xd.cps2002.player.*;
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
        game = Game.getGame();
    }

    /**
     * Tests whether the same instance is returned when attempting to create a new instance (i.e. testing the singleton
     * pattern).
     */
    @Test
    public void singletonTest(){
        Game new_Game = Game.getGame();
        assertEquals(game, new_Game);
    }

    //----- TESTING SETTERS -----

    /**
     * Tests that if players array is null, then setPlayers sets players accordingly.
     * @throws SetupOperationPrecedenceException is thrown when setPlayer() or initialise() was called prior i.e. players
     * array is not null (not expected).
     */
    @Test
    public void playersSet_setPlayersTest(){
        game.setPlayers(new Player[2]);
        assertNotNull(game.getPlayers());
    }

    /**
     * Tests that if players array is not null, then a SetupOperationPrecedenceException is thrown (trying to set to an
     * inconsistent state, potentially).
     * @throws SetupOperationPrecedenceException is thrown when setPlayer() or initialise() was called prior i.e. players
     * array is not null (expected).
     */
    @Test(expected = SetupOperationPrecedenceException.class)
    public void playersNotNull_setPlayersTest(){
        game.setPlayers(new Player[2]);
        assertNotNull(game.getPlayers()); // should pass - if not, test playersSet_setPlayersTest should fail too

        game.setPlayers(new Player[2]); // should throw exception since trying to set players array twice
    }

    /**
     * Tests that if map instance is null, then setMap sets the map instance accordingly.
     * @throws SetupOperationPrecedenceException is thrown when setMap() or initialise() was called prior i.e. map
     * instance is not null (not expected).
     */
    @Test
    public void mapSet_setMapTest(){
        game.setMap(MapCreator.createMap("basic", 5));
        assertNotNull(game.getMap());
    }

    /**
     * Tests that if map instance is not null, then a SetupOperationPrecedenceException is thrown (trying to set to an
     * inconsistent state, potentially).
     * @throws SetupOperationPrecedenceException is thrown when setMap() or initialise() was called prior i.e. map
     * instance is not null (expected).
     */
    @Test(expected = SetupOperationPrecedenceException.class)
    public void mapNotNull_setMapTest(){
        game.setMap(MapCreator.createMap("basic", 5));
        assertNotNull(game.getMap()); // should pass - if not, test mapSet_setMapTest should fail too

        game.setMap(MapCreator.createMap("basic", 5)); // should throw exception since trying to set map instance twice
    }

    /**
     * Tests that if teams array is null, then setTeams sets teams accordingly.
     * @throws SetupOperationPrecedenceException is thrown when setTeams() or initialise() was called prior i.e. teams
     * array is not null (not expected).
     */
    @Test
    public void teamSet_setTeamsTest(){
        game.setTeams(new Team[2]);
        assertNotNull(game.getTeams());
    }

    /**
     * Tests that if teams array is not null, then a SetupOperationPrecedenceException is thrown (trying to set to an
     * inconsistent state, potentially).
     * @throws SetupOperationPrecedenceException is thrown when setTeams() or initialise() was called prior i.e. teams
     * array is not null (expected).
     */
    @Test(expected = SetupOperationPrecedenceException.class)
    public void teamNotNull_setTeamsTest(){
        game.setTeams(new Team[2]);
        assertNotNull(game.getTeams()); // should pass - if not, test teamsSet_setTeamsTest should fail too

        game.setTeams(new Team[2]); // should throw exception since trying to set teams array twice
    }

    // ----- TESTING VALIDATION CHECK FUNCTIONS -----

    /**
     * Tests that false is returned when the number of players is less than 2.
     */
    @Test
    public void belowMin_NoOfPlayers_isValidNPlayersTest(){
        assertFalse(game.isValidNPlayers(1));
    }

    /**
     * Tests that false is returned when the number of players is greater than 8.
     */
    @Test
    public void aboveMax_NoOfPlayers_isValidNPlayersTest(){
        assertFalse(game.isValidNPlayers(9));
    }

    /**
     * Tests that false is returned when the map_size is less than 5, for any value of n_player.
     */
    @Test
    public void belowMin_MapSize_isValidMapSizeTest(){
        assertFalse(game.isValidMapSize(4, 1));
    }

    /**
     * Tests that false is returned when the map_size is greater than 50, for any value of n_player.
     */
    @Test
    public void aboveMax_MapSize_isValidMapSizeTest(){
        assertFalse(game.isValidMapSize(51, 1));
    }

    /**
     * Tests that false is returned when the map_size is greater than 5 but less than 8, for n_player {@literal >}= 5.
     */
    @Test
    public void smallMap_Min5Players_MapSize_isValidMapSizeTest(){
        assertFalse(game.isValidMapSize(6, 5));
    }

    /**
     * Tests that false is returned when n_teams {@literal <} 2, for any value of n_players.
     */
    @Test
    public void belowMin_NoOfTeams_isValidNTeamsTest(){
        assertFalse(game.isValidNTeams(1, 5));
    }

    /**
     * Tests that false is returned when n_teams {@literal >}= n_players.
     */
    @Test
    public void aboveMax_NoOfTeams_isValidNTeamsTest(){
        assertFalse(game.isValidNTeams(5, 5));
    }

    // ----- TESTING SETUP FUNCTIONS -----

    /**
     * Tests that an InvalidNumberOfPlayersException is thrown when the number of players is less than 2.
     * @throws InvalidNumberOfPlayersException is thrown if the number of players is {@literal <} 2 or {@literal >} 8 (expected).
     */
    @Test(expected = InvalidNumberOfPlayersException.class)
    public void belowMin_NoOfPlayers_genPlayersTest() throws InvalidNumberOfPlayersException{
        int n_players = 1;
        game.genPlayers(n_players);
    }

    /**
     * Tests that an InvalidNumberOfPlayersException is thrown when the number of players is greater than 8.
     * @throws InvalidNumberOfPlayersException is thrown if the number of players is {@literal <} 2 or {@literal >} 8 (expected).
     */
    @Test(expected = InvalidNumberOfPlayersException.class)
    public void aboveMax_NoOfPlayers_genPlayersTest() throws InvalidNumberOfPlayersException{
        int n_players = 9;
        game.genPlayers(n_players);
    }

    /**
     * Tests that the players array is initialized with the correct number of Player instances if input is valid.
     * @throws InvalidNumberOfPlayersException is thrown if the number of players is {@literal <} 2 or {@literal >} 8 (not expected).
     */
    @Test
    public void inrange_NoOfPlayers_genPlayersTest() throws InvalidNumberOfPlayersException{
        int n_players = 5;
        Player[] players = game.genPlayers(n_players);

        assertEquals(5, players.length);
        for(int i = 0; i < 5; i++){
            assertNotNull(players[i]);
        }
    }

    /**
     * Tests that an InvalidMapSizeException is thrown when the map_size is less than 5.
     * @throws InvalidMapSizeException is thrown if the map_size is invalid (expected).
     */
    @Test(expected = InvalidMapSizeException.class)
    public void belowMin_MapSize_genMapTest() throws InvalidMapSizeException{
        game.genMap(4, "safe", new Player[2]);
    }

    /**
     * Tests that an InvalidMapSizeException is thrown when the map_size is greater than 50.
     * @throws InvalidMapSizeException is thrown if the map_size is invalid (expected).
     */
    @Test(expected = InvalidMapSizeException.class)
    public void aboveMax_MapSize_genMapTest() throws InvalidMapSizeException{
        game.genMap(51, "safe", new Player[2]);
    }

    /**
     * Tests that an InvalidMapSizeException is thrown when the map_size is greater than 5 but less than 8 for 5 or more
     * initialized players.
     * @throws InvalidMapSizeException is thrown if the map_size is invalid (expected).
     */
    @Test(expected = InvalidMapSizeException.class)
    public void smallMap_Min5Players_genMapTest() throws InvalidMapSizeException{
        game.genMap(5, "safe", new Player[5]);
    }

    /**
     * Tests that a map of the appropriate size is initialized.
     * @throws InvalidMapSizeException is thrown if the map_size is invalid (not expected).
     */
    @Test
    public void inrange_MapSize_genMapTest() throws InvalidMapSizeException{
        int map_size = 5;
        Map map = game.genMap(map_size, "safe", new Player[2]);

        assertNotNull(map);
        assertEquals(map_size, map.getSize());
    }

    /**
     * Tests that all player positions have been initialized correctly.
     */
    @Test
    public void correct_genPlayerPositionsTest(){
        Player[] players = new Player[5];
        for(int i = 0; i < 5; i++){
            players[i] = new Player();
        }

        Map map = MapCreator.createMap("basic", tiles);
        map.isPlayable();

        players = game.genPlayerPositions(players, map);
        for(int i = 0; i < 5; i++){
            assertNotNull(players[i].getStartPosition());
        }
    }

    /**
     * Tests that an InvalidNumberOfTeamsException is thrown when n_teams {@literal <} 2, for any length of the players array.
     * @throws InvalidNumberOfTeamsException is thrown if n_teams is invalid (expected).
     * @throws SetupOperationPrecedenceException if player instances do not have an initialised starting position or if
     * they have already been joined to a team (not expected).
     */
    @Test(expected = InvalidNumberOfTeamsException.class)
    public void belowMin_NoOfTeams_genTeamsTest() throws InvalidNumberOfTeamsException{
        game.genTeams(2, new Player[1]);
    }

    /**
     * Tests that an InvalidNumberOfTeamsException is thrown when n_teams {@literal >} n_players.
     * @throws InvalidNumberOfTeamsException is thrown if n_teams is invalid (expected).
     * @throws SetupOperationPrecedenceException if player instances do not have an initialised starting position or if
     * they have already been joined to a team (not expected).
     */
    @Test(expected = InvalidNumberOfTeamsException.class)
    public void aboveMax_NoOfTeams_genTeamsTest() throws InvalidNumberOfTeamsException{
        game.genTeams(6, new Player[5]);
    }

    /**
     * Tests that a SetupOperationPrecedenceException is thrown when a Player instance does not have a start position.
     * @throws InvalidNumberOfTeamsException is thrown if n_teams is invalid (not expected).
     * @throws SetupOperationPrecedenceException if player instances do not have an initialised starting position or if
     * they have already been joined to a team (expected).
     */
    @Test(expected = SetupOperationPrecedenceException.class)
    public void noStartPosition_genTeamsTest() throws InvalidNumberOfTeamsException{
        Player[] players = new Player[2];

        // initialise position for player 0
        players[0] = new Player();
        players[0].setStartPosition(new Position(0,0));

        // but do not initialise position for player 1
        players[1] = new Player();

        game.genTeams(2, players);
    }

    /**
     * Tests that a SetupOperationPrecedenceException is thrown when a Player instance has already joined a team.
     * @throws InvalidNumberOfTeamsException is thrown if n_teams is invalid (not expected).
     * @throws SetupOperationPrecedenceException if player instances do not have an initialised starting position or if
     * they have already been joined to a team (expected).
     */
    @Test(expected = SetupOperationPrecedenceException.class)
    public void playerAlreadyJoinedTeam_genTeamsTest() throws InvalidNumberOfTeamsException{
        Player[] players = new Player[2];

        // initialise position and team for player 0
        players[0] = new Player();
        players[0].setStartPosition(new Position(0,0));
        players[0].setTeam(new Team());

        players[1] = new Player();

        game.genTeams(2, players);
    }

    /**
     * Tests that the correct number of Team instances are initialised.
     * @throws InvalidNumberOfTeamsException is thrown if n_teams is invalid (not expected).
     * @throws SetupOperationPrecedenceException if player instances do not have an initialised starting position or if
     * they have already been joined to a team (not expected).
     */
    @Test
    public void inrange_CorrectNumberOfTeams_genTeamsTest() throws InvalidNumberOfTeamsException{
        // initialising 6 player instances as position (0,0)
        Player[] players = new Player[6];
        for(int i = 0; i < players.length; i++){
            players[i] = new Player();
            players[i].setStartPosition(new Position(0, 0));
        }

        assertEquals(5, game.genTeams(5, players).length);
    }

    /**
     * Tests that each team has exactly one player when n_teams = n_players.
     * @throws InvalidNumberOfTeamsException is thrown if n_teams is invalid (not expected).
     * @throws SetupOperationPrecedenceException if player instances do not have an initialised starting position or if
     * they have already been joined to a team (not expected).
     */
    @Test
    public void inrange_NTeamsEqualsNPlayers_genTeamsTest() throws InvalidNumberOfTeamsException{
        // initialising 6 player instances as position (0,0)
        Player[] players = new Player[6];
        for(int i = 0; i < players.length; i++){
            players[i] = new Player();
            players[i].setStartPosition(new Position(0, 0));
        }

        Team[] teams = game.genTeams(6, players); // initialise teams

        for(Team t : teams){
            assertEquals(1, t.players.size());
        }
    }

    /**
     * Tests that each team a proper distribution on the number of players, when n_teams {@literal <} n_players.
     * @throws InvalidNumberOfTeamsException is thrown if n_teams is invalid (not expected).
     * @throws SetupOperationPrecedenceException if player instances do not have an initialised starting position or if
     * they have already been joined to a team (not expected).
     */
    @Test
    public void inrange_PlayerDistribution_genTeamsTest() throws InvalidNumberOfTeamsException{
        // initialising 6 player instances as position (0,0)
        Player[] players = new Player[7];
        for(int i = 0; i < players.length; i++){
            players[i] = new Player();
            players[i].setStartPosition(new Position(0, 0));
        }

        // Since 7/3 > 2 but < 3, then 2 teams should have 2 players while the last team should have 3 players.
        Team[] teams = game.genTeams(3, players); // initialise teams

        assertEquals(2, teams[0].players.size());
        assertEquals(2, teams[1].players.size());
        assertEquals(3, teams[2].players.size());
    }

    /**
     * Tests that a SetupOperationPrecedenceException is thrown when no directory path has been specified.
     * @throws SetupOperationPrecedenceException is thrown if no directory path specified (expected).
     * @throws IOException is thrown when there is a failure in persisting to disk [generally fatal] (not expected).
     */
    @Test(expected = SetupOperationPrecedenceException.class)
    public void noDirPathSpecified_writeHTMLFilesTest() throws IOException{
        game.dir = null;

        game.writeHTMLFile(new Player(), MapCreator.createMap("basic", 5));
    }

    /**
     * Tests that a SetupOperationPrecedenceException is thrown when no Map instance has been initialized.
     * @throws SetupOperationPrecedenceException is thrown if no directory path or Map has been initialized (expected).
     * @throws IOException is thrown when there is a failure in persisting to disk [generally fatal] (not expected).
     */
    @Test(expected = SetupOperationPrecedenceException.class)
    public void noMapInitialized_writeHTMLFilesTest() throws IOException{
        game.writeHTMLFile(new Player(), null);
    }

    @After
    public void teardownMainGameTest(){
        game.reset();
    }
}
