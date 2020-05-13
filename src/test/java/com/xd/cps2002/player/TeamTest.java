package com.xd.cps2002.player;

import com.xd.cps2002.player.player_exceptions.NullPositionException;
import com.xd.cps2002.player.player_exceptions.TeamOverrideException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tester class for the Team class.
 * @author Xandru Mifsud
 */
public class TeamTest{
    // Player object to initialise on setup and de-reference on teardown.
    private Team team;

    @Before
    public void setupTeamTest(){
        team = new Team(); // initialising new team

        // add two players
        team.players.add(new Player());
        team.players.add(new Player());
    }

    /**
     * Testing that team_id is correctly auto-incremented whenever a new Team instance is created.
     */
    @Test
    public void autoincID_Test(){
        Team team2 = new Team();
        // consistency check: id of previously initialised Team object does not change
        assertEquals(Team.get_global_team_count() - 2, team.get_tID());
        assertEquals(Team.get_global_team_count() - 1, team2.get_tID());

        Team team3 = new Team();
        // consistency check: id of previously initialised Team object does not change
        assertEquals(Team.get_global_team_count() - 3, team.get_tID());
        assertEquals(Team.get_global_team_count() - 2, team2.get_tID());
        assertEquals(Team.get_global_team_count() - 1, team3.get_tID());
    }

    /**
     * Testing that upon joining a team, the player must have an initialised starting position.
     * @throws NullPositionException is thrown whenever the Player.start_position is set to null (expected).
     * @throws IllegalArgumentException is thrown whenever the Player is null (not expected).
     * @throws TeamOverrideException is thrown whenever a Player instance has already joined a team (not expected).
     */
    @Test(expected = NullPositionException.class)
    public void nullStartPosition_joinTest() throws TeamOverrideException{
        Player new_player = new Player(); // create new player

        team.join(new_player);
    }

    /**
     * Testing that upon joining a team, the player be an initialised Player instance.
     * @throws NullPositionException is thrown whenever the Player.start_position is set to null (not expected).
     * @throws IllegalArgumentException is thrown whenever the Player is null (expected).
     * @throws TeamOverrideException is thrown whenever a Player instance has already joined a team (not expected).
     */
    @Test(expected = IllegalArgumentException.class)
    public void nullPlayer_joinTest() throws TeamOverrideException{
        team.join(null);
    }

    /**
     * Testing that upon attempting to join a team, an exception is thrown if the Player has already joined another team
     * @throws NullPositionException is thrown whenever the Player.start_position is set to null (not expected).
     * @throws IllegalArgumentException is thrown whenever the Player is null (not expected).
     * @throws TeamOverrideException is thrown whenever a Player instance has already joined a team (expected).
     */
    @Test(expected = TeamOverrideException.class)
    public void playerHasTeam_joinTest() throws TeamOverrideException{
        Player new_player = new Player(); // create new player
        new_player.setStartPosition(new Position(0, 0)); // set start position
	    new_player.setTeam(new Team());

        team.join(new_player);
    }

    /**
     * Testing that upon joining a team, the new Player is added to players.
     * @throws NullPositionException is thrown whenever the Player.start_position is set to null (not expected).
     * @throws IllegalArgumentException is thrown whenever the Player is null (not expected).
     * @throws TeamOverrideException is thrown whenever a Player instance has already joined a team (not expected).
     */
    @Test
    public void addPlayer_joinTest() throws TeamOverrideException{
        Player new_player = new Player(); // create new player
        new_player.setStartPosition(new Position(0, 0)); // set start position
        int pID = new_player.get_pID();

        int t_player_len = team.players.size();

        team.join(new_player);
        assertEquals(t_player_len + 1, team.players.size()); // check that size increased by 1
        // check that last player is new_player by comparing pID
        assertEquals(pID, team.players.get(team.players.size()-1).get_pID());
    }

    /**
     * Testing that upon adding a new Position in historical_positions, it is correctly appended to the end.
     */
    @Test
    public void addNewPosition_updateTest(){
        team.update(new Position(0, 1));
        
	assertEquals(1, team.getPositionHistory().size()); // check that size is 1
	// verify appended position coordinates
	assertEquals(0, team.getPositionHistory().get(0).x);
	assertEquals(1, team.getPositionHistory().get(0).y);
    }

    /**
     * Testing that upon adding a Position in historical_positions with coordinates that already exist, the
     * position is not appended.
     */
    @Test
    public void addDuplicatePosition_updateTest(){
	team.getPositionHistory().add(new Position(0, 1));
	assertEquals(1, team.getPositionHistory().size()); // check that size is 1

	team.update(new Position(0, 1)); // attempt to add duplicate
	assertEquals(1, team.getPositionHistory().size()); // check that size is still 1

    }

    @After
    public void teardownTeamTest(){
        team = null; // dereference
    }
}
