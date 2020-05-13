package com.xd.cps2002.player;

import com.xd.cps2002.player.player_exceptions.NullPositionException;
import com.xd.cps2002.player.player_exceptions.TeamOverrideException;

import java.util.ArrayList;

/**
 * The Team class defines the notion of a team in the game, and is responsible for maintaining the collective history
 * of all players forming part of the team. It is based on the Mediator design pattern, coordinating a shared history
 * between all the players in a team. While no 'mediation' is used between specific player instances, the capacity to
 * do so can be easily provided thanks to this design pattern. Eg. in the future, if one player of the team dies, all
 * all players of that team may be reset to their respective starting positions.
 *
 * @author Xandru Mifsud
 */
public class Team{
    private static int global_team_count = 0; // maintains count of the number of Team instances created
    private int team_id; // auto-incrementing upon instantiation
    private ArrayList<Position> historical_positions = new ArrayList<Position>(); // maintain record of visited coords

    public ArrayList<Player> players = new ArrayList<Player>(); // maintain record of joined players

    public Team(){
        this.team_id = global_team_count++; // auto-incrementation
    }

    /**
     * Getter for private static int global_team_count.
     * @return int global_team_count - the count of the number of Team instances created.
     */
    public static int get_global_team_count(){
        return global_team_count;
    }

    /**
     * Getter for the unique team id.
     * @return int this.team_id - the auto-incrementation derived unique team id.
     */
    public int get_tID(){
        return this.team_id;
    }

    /**
     * Getter for the team's Position history (from start/reset state till the current state).
     * @return ArrayList{@literal <}Position{@literal >} historical_positions is an ArrayList of all previously visited Position(s).
     */
    public ArrayList<Position> getPositionHistory(){
        return historical_positions;
    }

    /**
     * Responsible for the updating of the historical_positions array, ensuring no duplicates are added.
     * Duplicate checking is carried out to restrict memory usage. The penalty to this is that duplication 
     * checking is carried out in O(n^2) time complexity, where n is the size of the map (n x n positions).
     * Future implementations, especially those involving larger maps, may wish to implement the Comparable
     * interface and use hashing for better retrieval. 
     */
    public void update(Position position){
        if(!historical_positions.contains(position)){
            historical_positions.add(position);
        }
    }

    /**
     * Allows for players to join a team, provided that:
     * i. They are a non-null Player instance,
     * ii. Player player.start_position has been initialised i.e. is not null.
     * iii. Player player.team is null.
     * @throws NullPositionException is thrown whenever the player.start_position is not set.
     * @throws IllegalArgumentException is thrown whenever Player player is null.
     * @throws TeamOverrideException is thrown whenever a Player instance has already joined a team.
     */
    public void join(Player player) throws TeamOverrideException{
        if(player == null){
            throw new IllegalArgumentException();
        }
        else if(player.getStartPosition() == null){
            throw new NullPositionException(player.get_pID());
        }
        else if(player.getTeam() != null){
            throw new TeamOverrideException(player.get_pID());
        }
        else{
            players.add(player);
            update(player.getStartPosition());

            player.setTeam(this);
        }
    }

}