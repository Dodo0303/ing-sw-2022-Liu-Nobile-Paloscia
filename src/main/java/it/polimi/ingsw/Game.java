package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/** A new game. FOR NOW, THIS WORKS ONLY FOR 2-PERSON GAMES. MAKE GAME CLASS AN ABSTRACT ONE?//TODO
 */

public class Game {

    /** A new 2-person game. //TODO Wizard(assistant card objects)
     */
    Game(Wizard[] wizards) {
        initializeIslands();
        initializePlayers();
        initializeClouds();
        setWizard(_players[0], wizards[0]);
        setWizard(_players[1, wizards[1]);
        setSevenStudents(_players[0]);
        setSevenStudents(_players[1]);
        _bag = new Bag();
        _spareCoins = 20;
        _numIslands = 12;
        Random randomMothernature = new Random();
        _motherNature = randomMothernature.nextInt(12); //automatically choose a random island for mothernature. OR let players choose it manually?//TODO
        Random randomPlayer = new Random();
        _currentPlayer = _players[randomPlayer.nextInt(2)]; //Determine the first player at random.
    }

    /** Initialize 12 islands with 2 students.
     */
    private void initializeIslands() {
        islands = new HashMap<>();
        ArrayList<StudentColor> twoForEachColor = new ArrayList<>();
        for (StudentColor color : StudentColor.values()) {
            for(int i = 0; i < 2; i++) {
                twoForEachColor.add(color);
            }
        }

        int rnd;
        for(int i = 0; i < 12; i++) {
            islands.put(i, new Island());
            rnd = new Random().nextInt(twoForEachColor.size()); //rnd is a random number between 0 and twoForEachColor.size().
            islands.get(i).addStudent(twoForEachColor.remove(rnd)); //add the student at rnd position of the arraylist twoForEachColor to the ISLAND and then remove it from twoForEachColor.
        }
    }

    /** Initialize 2 players. COULD BE ABSTRACT. //TODO
     * player 0 is me, player 1 is the opponent.
     */
    private void initializePlayers() {
        _players[0] = new Player(Color.WHITE);
        _players[1] = new Player(Color.BLACK);
    }

    /** Initialize clouds.
     */
    private void initializeClouds(){

    }

    /** Set assistant cards of wizard WIZARD to player PLAYER. //TODO
     * @param wizard Assistant cards of wizard WIZARD.
     * @param player The player to get assistant cards of wizard WIZARD.
     */
    void setWizard(Player player, Wizard wizard) {

    }

    /** Extract 7 students from bag and add to the entrance of player PLAYER.
     */
    private void setSevenStudents(Player player){
        for(int i = 0; i < 7; i++) {
            player.addStudentToEntrance(_bag.extractStudent()); //TODO Why String? Why does the caller have to handle excp?
        }
    }


    /** In the case that x == numIslands - 1(ex. x = 11, y = 0), use the yth island to merge the xth island, just like deleting the tail node of a linked list.
     * @param x the index of one of the islands to be merged.
     * @param y the index of one of the islands to be merged.
     */
    private void mergeIslands(int x, int y) {
        if (x == _numIslands - 1) {
            islands.get(y).copyFrom(x);
            if (x == _motherNature) {
                _motherNature = y;
            }
        }
        else {
            islands.get(x).copyFrom(y);
            if (y == _motherNature) {
                _motherNature = x;
            }
            for (int i = y; i < _numIslands - 1; i++) {
                islands.put(i, islands.get(i + 1)); // move islands after the yth forward by 1.
            }
        }
        islands.remove(_numIslands--);
    }

    /**
     * @return number of coins not obtained by any player.*/
    int getSpareCoins() {
        return this._spareCoins;
    }

    /**
     * @return the island with mothernature.*/
    Island getMotherNature() {
        return islands.get(_motherNature);
    }

    /**
     * Move mothernature to xth island.*/
    void moveMotherNature(int x) {
        if (_motherNature == x || x > _numIslands - 1) {
            throw new GameException("Illegal movement.");
        } else {
            _motherNature = x;
        }
    }

    /**
     * @return the current player.*/
    Player getCurrentPlayer() {
        return this._currentPlayer;
    }

    /**
     * Set current player to PLAYER.*/
    void setCurrentPlayer(Player player) {
        try {
            _currentPlayer = player;
        } catch (IndexOutOfBoundsException excp) {
            throw excp;
        }
    }

    /**
     * @return the winner of current game.*/
    Player getWinner() {
        return this._winner;
    }

    /** @Return true iff it would currently be legal for PLAYER to move.*/
    boolean isLegal(Player player) {
        if (!_currentPlayer.equals(player)) {
            return true;
        }
        return false;
    }

    /** All islands.*/
    private HashMap<Integer, Island> islands;

    /** Coins not obtained by any player. Initially set to 20.*/
    private int _spareCoins;

    /** the index of the island with mothernature.*/
    private int _motherNature;

    /** current player. */
    private Player _currentPlayer;

    /** the winner of current game.*/
    private Player _winner;

    /** number of islands. (merged islands count as 1) */
    private int _numIslands;

    /** students bag*/
    private Bag _bag;

    /** currrent players. 2-PERSON GAME RESTRICTED! //TODO */
    private Player[] _players = new Player[2];

    /** clouds on board. 2-PERSON GAME RESTRICTED! //TODO */
    private CloudTwoFourPlayers[] _clouds = new CloudTwoFourPlayers[2];
}
