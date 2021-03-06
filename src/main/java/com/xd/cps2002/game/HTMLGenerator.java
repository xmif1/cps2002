package com.xd.cps2002.game;

import com.xd.cps2002.map.Map;
import com.xd.cps2002.map.TileType;
import com.xd.cps2002.player.Player;
import com.xd.cps2002.player.Position;
import com.xd.cps2002.player.player_exceptions.NullTeamException;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The HTMLGenerator class is responsible for generating the HTML maps for each player. It implements a Singleton design
 * pattern, to allow for better resource management and provide better control over writing to the generated HTML files.
 *
 * By decoupling the CSS style definitions of the tiles from this generator, the possibility to add new tile types is
 * greatly simplified.
 *
 * @author Xandru Mifsud
 */
public class HTMLGenerator{
    private static HTMLGenerator instance = null; // the singleton instance

    /**
     * Private constructor to initialize an HTMLGenerator instance (if one does not already exist).
     */
    private HTMLGenerator(){ }

    /**
     * Returns an HTMLGenerator instance; in the case that an instance already exists, it returns the exisiting one.
     * Else it creates a new instance and returns it.
     * @return HTMLGenerator instance is the singleton to be returned.
     */
    public static HTMLGenerator getHTMLGenerator(){
        if(instance == null){
            instance = new HTMLGenerator();
        }
        return instance;
    }

    /**
     * Dereferences the HTMLGenerator instance to null.
     */
    public static void dereferenceHTMLGenerator(){
        instance = null;
    }

    /**
     * Convenience function to generate the head block and nested CSS style block of the HTML file.
     * Add the tile style for each tile defined in the TileType enum, by means of the html_blurb.
     * @param map_size is used to define the CSS grid-container size, by means of grid-template-columns and
     *                 grid-template-rows.
     * @return Returns a string with the correctly formatted head block and nested CSS style block.
     */
    private String genHTMLHead(int map_size){
        StringBuilder style = new StringBuilder("<head>\n" + // begin head block
                                                "<style>\n\n" + // begin style block

                                                // define the CSS grid-container
                                                ".grid-container {display: grid;\n" +
                                                " grid-template-columns: repeat(" + map_size + ", 40px);\n" +
                                                " grid-template-rows: repeat(" + map_size + ", 40px);\n" +
                                                " padding: 10px;}\n\n" +

                                                // defining style of an uncovered tile
                                                ".uncovered {background-color: rgba(150, 150, 150, 0.8);\n" +
                                                " border: 1px solid rgba(0, 0, 0, 0.8);}\n\n");

        // add the tile style for each tile defined in the TileType enum
        for(TileType tile : TileType.values()){
            style.append(tile.html_blurb);
        }

        style.append("</style>\n</head>\n\n"); // close style block followed by head block

        return style.toString();
    }

    /**
     * Responsible for generating the HTML map, as a structured String ArrayList
     * @param player is a Player instance for which a map will be generated.
     * @param map is a Map instance on which the HTML map will be based.
     * @throws IllegalArgumentException whenever the Player or Map instances passed are null.
     * @throws NullTeamException is thrown when Team team is null, i.e. when it has not been set.
     * @return an ArrayList of type String with the lines of the HTML file.
     */
    public ArrayList<String> genPlayerMap(Player player, Map map){
        if(map == null){
            throw new IllegalArgumentException("Map instance cannot be null.");
        }
        else if(player == null){
            throw new IllegalArgumentException("Player instance cannot be null.");
        }
        else if(player.getTeam() == null){
            throw new NullTeamException(player.get_pID());
        }

        int map_size = map.getSize();

        ArrayList<String> html = new ArrayList<String>(); // will be used to hold the HTML file structure
        /* The following describes the structure of the String ArrayList html, which in turn reflects the structure of
         * the resulting HTML file. Any modifications to this function must either (i) adhere to this structure or (ii)
         * reflect any structural changes throughout the function.
         * ---------------------------------------------------------------------------------------------------
         *      idx     |                                 Description
         * -------------|-------------------------------------------------------------------------------------
         *       0      | Begin html: "<!DOCTYPE html>\n<html>\n\n"
         * -------------|-------------------------------------------------------------------------------------
         *              | Stores the head and nested CSS style block, as generated by genHTMLHead(map_size).
         *       1      | Changes to the grid-container definition and the way the identifiers for the tile
         *              | colourings are defined from the TileType, may require adaptation of unit tests.
         * -------------|-------------------------------------------------------------------------------------
         *              | Stores the first part of the <body> block - up to the grid-container class. Changes
         *       2      | are allowed here, so long as <div class="grid-container"> remains the last line.
         *              | Changes may include headers, body text, etc.
         * -------------|-------------------------------------------------------------------------------------
         * 3 to n^2 + 3 | Stores (in a linearised way) the tile style for each tile in the map of size n by n.
         * -------------|-------------------------------------------------------------------------------------
         *    n^2 + 4   | End html by closing any open blocks.
         * ---------------------------------------------------------------------------------------------------
         */
        html.add("<!DOCTYPE html>\n<html>\n\n"); // begin html [at index 0]
        // add the head and nested CSS style block, as generated by genHTMLHead(map_size) [at index 1]
        html.add(genHTMLHead(map_size));

        String title = (player.getTeam().players.size() == 1)
                     ? "<h1>Map for Player #" + player.get_pID() + "</h1>\n\n"
                     : "<h1>Map for Player #" + player.get_pID() + " in Team #" + player.getTeam().get_tID() + "</h1>\n\n";

        // add the first part of the <body> block - up to the grid-container class [at index 2]
        html.add("<body>\n\n" +
                title + // header with player number
                "<div class=\"grid-container\">\n");

        // add map_size^2 uncovered tiles to the map [at index 3 to map_size^2 + 3]
        html.addAll(Collections.nCopies(map_size*map_size, "<div class=\"uncovered\"></div>\n"));
        html.add("</div>\n</body>"); // end html

        int idx;
        for(Position position : player.getTeam().getPositionHistory()){ // for each visited position by the player
            idx = 3 + map_size*position.y + position.x; // calculate linearised 1D index corresponding to 2D coordinates
            // set the tile style to the corresponding one in the map, based on the player's visited position
            html.set(idx, "<div class=\"" + map.getTileType(position.x, position.y).html_handle + "\"></div>\n");
        }

        // marking the start position with a symbol (a Torii Gate)
        Position start_position = player.getStartPosition();
        idx = 3 + map_size*start_position.y + start_position.x;
        html.set(idx, "<div class=\"" + map.getTileType(start_position.x, start_position.y).html_handle +
                "\">&#x26E9;</div>\n");

        // marking the current position with a symbol (a gleaming Sun)
        Position curr_position = player.getPosition();
        idx = 3 + map_size*curr_position.y + curr_position.x;
        html.set(idx, "<div class=\"" + map.getTileType(curr_position.x, curr_position.y).html_handle +
                "\">&#x1F31E;</div>\n");

        return html;

        // Example use of FileWriter to persist html String ArrayList as a .html file to disk
        // FileWriter writer = new FileWriter(dir + "player_" + player.get_pID() + "_map.html");
        // for(String ln: html){ // iterate through the html ArrayList and persist
        //     writer.write(ln);
        // }
        // writer.close();
    }
}
