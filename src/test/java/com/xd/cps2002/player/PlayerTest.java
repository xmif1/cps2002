package com.xd.cps2002.player;

import com.xd.cps2002.player.player_exceptions.MoveException;
import com.xd.cps2002.player.player_exceptions.NullPositionException;
import com.xd.cps2002.player.player_exceptions.NullTeamException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tester class for the Player class.
 * @author Xandru Mifsud
 */
public class PlayerTest{
    // Player object to initialise on setup and de-reference on teardown.
    private Player player;
    private char input = '\0';

    @Before
    public void setupPlayerTest(){
        player = new Player(); // initialising new player
        player.setStartPosition(new Position(0,0));// setting to origin
        player.setTeam(new Team()); // set to some Team instance
    }

    /**
     * Testing that player_id is correctly auto-incremented whenever a new Player instance is created.
     */
    @Test
    public void autoincID_Test(){
        Player player2 = new Player();
        // consistency check: id of previously initialised Player object does not change
        assertEquals(Player.get_global_player_count() - 2, player.get_pID());
        assertEquals(Player.get_global_player_count() - 1, player2.get_pID());

        Player player3 = new Player();
        // consistency check: id of previously initialised Player object does not change
        assertEquals(Player.get_global_player_count() - 3, player.get_pID());
        assertEquals(Player.get_global_player_count() - 2, player2.get_pID());
        assertEquals(Player.get_global_player_count() - 1, player3.get_pID());
    }

    /* ---- This section is intended to test the failing scenarios of Player.move() ---- */

    /**
     * Testing exception is thrown when a char outside of the set {'u', 'd', 'r', 'l'} is passed.
     * @throws MoveException is expected to be thrown, since the input is not a valid character.
     * @throws NullPositionException is thrown whenever the Player.position is not set (not expected).
     */
    @Test(expected = MoveException.class)
    public void move_InvalidInputTest() throws MoveException{
        input = 'i'; // character is invalid, since 'i' is not in {'u', 'd', 'r', 'l'}
        player.move(input);
    }

    /**
     * Testing exception is thrown when the null char '\0' is passed.
     * @throws MoveException is expected to be thrown, since the input is not a valid character.
     * @throws NullPositionException is thrown whenever the Player.position is not set (not expected).
     */
    @Test(expected = MoveException.class)
    public void move_NullCharInputTest() throws MoveException{
        input = '\0'; // character is invalid, since it is not in {'u', 'd', 'r', 'l'}
        player.move(input);
    }

    /**
     * Testing exception is thrown when Player Position position is null.
     * @throws MoveException is thrown whenever the input is not a valid character (not expected).
     * @throws NullPositionException is expected to be thrown, since the Player.position is set to null.
     */
    @Test(expected = NullPositionException.class)
    public void move_NullPositionTest() throws MoveException{
        player.setPosition(null);
        input = 'u'; // providing valid input, beyond scope of this test
        player.move(input);
    }

    /* ---- This section is intended to test the passing scenarios of Player.move() ----
     *
     * Note that the Player is agnostic of the game map - hence negative coordinates are allowed. The client may then
     * handle these accordingly, eg. by wrap around (similar to Snake or Pac-Man)
     *
     * While being agnostic from the Map, it is assumed throughout this project that the (i,j) index of a map is the
     * (i,j) cartesian coordinate.
     */

    /**
     * Testing if the 'u' character results in a decrement in the y-axis while the x-axis remains fixed.
     * @throws MoveException is thrown whenever the input is not a valid character (not expected).
     * @throws NullPositionException is thrown whenever the Player.position is not set (not expected).
     */
    @Test
    public void move_UpPosCalcTest() throws MoveException{
        input = 'u';

        Position p = player.move(input);
        assertEquals(0, p.x); // x remains at the origin
        assertEquals(-1, p.y); // y decreases in accordance to the cartesian coordinates
    }

    /**
     * Testing if the 'd' character results in an increment in the y-axis while the x-axis remains fixed.
     * @throws MoveException is thrown whenever the input is not a valid character (not expected).
     * @throws NullPositionException is thrown whenever the Player.position is not set (not expected).
     */
    @Test
    public void move_DownPosCalcTest() throws MoveException{
        input = 'd';

        Position p = player.move(input);
        assertEquals(0, p.x); // x remains at the origin
        assertEquals(1, p.y); // y increases in accordance to the cartesian coordinates
    }

    /**
     * Testing if the 'l' character results in a decrease in the x-axis while the y-axis remains fixed
     * @throws MoveException is thrown whenever the input is not a valid character (not expected).
     * @throws NullPositionException is thrown whenever the Player.position is not set (not expected).
     */
    @Test
    public void move_LeftPosCalcTest() throws MoveException{
        input = 'l';

        Position p = player.move(input);
        assertEquals(-1, p.x); // x decreases in accordance to the cartesian coordinates
        assertEquals(0, p.y); // y remains at the origin
    }

    /**
     * Testing if the 'r' character results in an increment in the x-axis while the y-axis remains fixed
     * @throws MoveException is thrown whenever the input is not a valid character (not expected).
     * @throws NullPositionException is thrown whenever the Player.position is not set (not expected).
     */
    @Test
    public void move_RightPosCalcTest() throws MoveException{
        input = 'r';

        Position p = player.move(input);
        assertEquals(1, p.x); // x increases in accordance to the cartesian coordinates
        assertEquals(0, p.y); // y remains at the origin
    }

    /* ---- This section is intended to test the setPosition() functionality ---- */

    /**
     * Testing exception is thrown when setPosition() is called and Player Position start_position is null.
     * @throws NullPositionException is thrown when Position start_position is null, i.e. when it has not been set (expected).
     * @throws NullTeamException is thrown when Team team is null, i.e. when it has not been set (not expected).
     */
    @Test(expected = NullPositionException.class)
    public void nullStartPosition_setPositionTest(){
        Player player2 = new Player();
        player2.setPosition(new Position(0, 0));
    }

    /**
     * Testing exception is thrown when setPosition() is called and Team team is null.
     * @throws NullPositionException is thrown when Position start_position is null, i.e. when it has not been set (not expected).
     * @throws NullTeamException is thrown when Team team is null, i.e. when it has not been set (expected).
     */
    @Test(expected = NullTeamException.class)
    public void nullTeam_setPositionTest(){
        Player player2 = new Player();
        player2.setStartPosition(new Position(0, 0));

        player2.setPosition(new Position(0, 1));
    }

    /* ---- This section is intended to test the setTeam() functionality ---- */

    /**
     * Testing whether an IllegalArgumentException is raised when setTeam is called with a null Team instance.
     * @throws IllegalArgumentException whenever the Team instance passed is null (expected).
     */
    @Test(expected = IllegalArgumentException.class)
    public void nullTeam_setTeamTest(){
        player.setTeam(null);
    }

    /* ---- This section is intended to test the reset() functionality ---- */

    /**
     * Testing exception is thrown when reset() is called and Player Position start_position is null.
     * @throws NullPositionException is expected to be thrown, since the Player.position is set to null.
     */
    @Test(expected = NullPositionException.class)
    public void reset_NullStartPositionTest(){
        Player player2 = new Player();
        player2.reset();
    }

    @After
    public void teardownPlayerTest(){
        player = null; // dereference
        input = '\0';
    }
}
