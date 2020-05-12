package com.xd.cps2002.player;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tester class for the Team class.
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
     * Testing that upon joining a team, the new Player is added to players.
     */
    @Test
    public void addPlayer_joinTeamTest(){
        Player new_player = new Player(); // create new player
        int pID = new_player.get_pID();

        int t_player_len = team.players.size();

        team.add(new_player);
        assertEquals(t_player_len + 1, team.players.size()); // check that size increased by 1
        assertEquals(pID, team.get(team.size()-1)); // check that last player is new_player by comparing pID
    }

    /**
     * Testing that upon joining a team, the player must have an initialised starting position.
     * @throws NullPositionException is expected to be thrown, since the Player.start_position is set to null.
     */
    @Test(expected = NullPositionException.class)
    public void nullStartPosition_joinTeamTest(){
        Player new_player = new Player(); // create new player

        team.add(new_player);
    }

    /**
     * Testing that upon adding a new Position in historical_positions, it is correctly appended to the end.
     */
    @Test
    public void addNewPosition_updateTest(){
        team.update(new Position(0, 1))
        
	assertEquals(1, team.historical_positions.size()) // check that size is 1
	// verify appended position coordinates
	assertEquals(0, team.historical_positions.get(0).x)
	assertEquals(1, team.historical_positions.get(0).y)
    }

    /**
     * Testing that upon adding a Position in historical_positions with coordinates that already exist, the
     * position is not appended.
     */
    @Test
    public void addDuplicatePosition_updateTest(){
	team.historical_positions.add(new Position(0, 1))
	assertEquals(1, team.historical_positions.size()) // check that size is 1

	team.update(new Position(0, 1)) // attempt to add duplicate
	assertEquals(1, team.historical_positions.size()) // check that size is still 1

    }

    @After
    public void teardownTeamTest(){
        team = null; // dereference
    }
}
