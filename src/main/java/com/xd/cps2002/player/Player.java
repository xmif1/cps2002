package com.xd.cps2002.player;

import com.xd.cps2002.player.player_exceptions.MoveException;
import com.xd.cps2002.player.player_exceptions.NullPositionException;
import com.xd.cps2002.player.player_exceptions.NullTeamException;

/**
 * The Player class defines a player in the game, and is responsible for calculating the new Position after a move.
 *
 * @author Xandru Mifsud
 */
public class Player{
    private static int global_player_count = 0; // maintains count of the number of Player instances created
    private int player_id; // auto-incrementing upon instantiation
    private Position position = null; // maintain the current player position
    private Position start_position = null; // maintain the start position of the player

    private Team team;

    public Player(){
        this.player_id = global_player_count++; // auto-incrementation
    }

    /**
     * Getter for private static int global_player_count.
     * @return int global_player_count - the count of the number of Player instances created.
     */
    public static int get_global_player_count(){
        return global_player_count;
    }

    /**
     * Getter for the unique player id.
     * @return int this.player_id - the auto-incrementation derived unique player id.
     */
    public int get_pID(){
        return this.player_id;
    }

    /**
     * Getter for the Team instance associated with the player.
     * @return Team this.team - the Team instance associated with the player.
     */
    public Team getTeam(){
        return this.team;
    }

    /**
     * Setter for the Team instance associated with the player.
     * @param team - the Team instance to be associated with the player.
     * @throws IllegalArgumentException whenever the passed team instance is null.
     */
    public void setTeam(Team team){
        if(team == null){
            throw new IllegalArgumentException("Team instance cannot be null");
        }
        this.team = team;
    }

    /**
     * Getter for the player Position position.
     * @return Position position - the current player's position.
     */
    public Position getPosition(){
        return position;
    }

    /**
     * Setter for the player Position position, only allowed if start_position has been set beforehand. It is also
     * responsible for notifying the Team instance of the new position.
     * @param position is a Position object to which the player's position will be set upon correct execution.
     * @throws NullPositionException is thrown when Position start_position is null, i.e. when it has not been set.
     * @throws NullTeamException is thrown when Team team is null, i.e. when it has not been set.
     */
    public void setPosition(Position position){
        if(start_position == null){
            throw new NullPositionException(player_id);
        }
        else if(team == null){
            throw new NullTeamException(player_id);
        }
        else{
            this.position = position;
            team.update(position); // TO-DO: THROW EXCEPTION IF TEAM NOT SET
        }
    }

    /**
     * Getter for the player Position start_position.
     * @return Position start_position - the player's starting position.
     */
    public Position getStartPosition(){
        return start_position;
    }

    /**
     * Setter for the player Position start_position, and hence also the position.
     * @param start_position is a Position object to which the player's starting and current positions will be set.
     */
    public void setStartPosition(Position start_position){
        this.start_position = start_position;
        position = start_position;
    }

    /**
     * Resets the player's position to the starting position
     * @throws NullPositionException is thrown when Position start_position is null, i.e. when it has not been set.
     */
    public void reset(){
        if(start_position == null){
            throw new NullPositionException(player_id);
        }
        else {
            setPosition(start_position);
        }
    }

    /**
     * The boolean move(char input) function returns true if a character from the set {'u', 'd', 'r', 'l'} (case-
     * insensitive) is passed as input. Before doing so, the player position is updated accordingly, given that the
     * position has been set. If the position is null, a NullPositionException is thrown.
     *
     * The function is intentionally designed not to ask for input. This is to allow flexibility to the calling client,
     * eg. by choosing to impose a limitation on the number of times a player is asked for input.
     *
     * If an invalid character is passed, a MoveException is thrown.
     *
     * While being agnostic from the map, it is assumed throughout this project that the (i,j) index of a map is the
     * (i,j) cartesian coordinate. This is important to note when shifting indices to represent moves.
     * @param input is a char specifying the direction to shift the coordinates, if the char is in the set {'u', 'd', 'r', 'l'}.
     * @return Position object derived from the player's current position and the coordinate shifts.
     * @throws MoveException is thrown whenever the input is not a valid character.
     * @throws NullPositionException is thrown whenever the Player.position is not set.
     */
    public Position move(char input) throws MoveException{
        // in case position is not set, throw exception
        if(position == null){
            throw new NullPositionException(player_id);
        }
        switch(Character.toLowerCase(input)){ // test against cases to carry out necessary logic for shifting position
            case 'u': return new Position(position.x, position.y - 1);
            case 'd': return new Position(position.x, position.y + 1);
            case 'l': return new Position(position.x - 1, position.y);
            case 'r': return new Position(position.x + 1, position.y);

            default: throw new MoveException();
        }
    }
}