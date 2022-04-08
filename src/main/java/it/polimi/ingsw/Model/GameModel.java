package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import static it.polimi.ingsw.Model.GameException.error;

/** A new game.
 */

public class GameModel {

    /**
     * Constructor of GameModel.
     * @param wizards the wizards chosen by each player.
     * @param numOfPlayers number of players in game. Must be between 2 and 4.
     */
    GameModel(Wizard[] wizards, int numOfPlayers) {

        if (numOfPlayers < 2 || numOfPlayers > 4) {
            throw new GameException();
        }

        initializeIslands();

        try {
            initializePlayers(wizards, numOfPlayers);
        } catch (IndexOutOfBoundsException indx) {
            indx.printStackTrace();
        }

        initializeClouds(numOfPlayers);

        setSevenStudents(_players.get(0));
        setSevenStudents(_players.get(1));

        _bag = new Bag();
        _spareCoins = 20;
        _numIslands = 12;

        Random randomMothernature = new Random();
        _motherNature = randomMothernature.nextInt(12); //automatically choose a random island for mothernature.

        Random randomPlayer = new Random();
        _currentPlayer = _players.get(randomPlayer.nextInt(2)); //Determine the first player at random.
    }

    /** Initialize 12 islands with 2 students each.
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

    /**
     * Creates instances of class Player.
     * @param wizards, the array of Wizards chosen by each player.
     * @param numOfPlayers number of player, to decide how many objects must be created.
     * @throws IndexOutOfBoundsException in case  array wizards is not of length numOfPlayers
     */
    private void initializePlayers(Wizard[] wizards, int numOfPlayers) {
        if (numOfPlayers == 2) {
            _players.add(new Player(Color.WHITE, wizards[0], 8));
            _players.add(new Player(Color.BLACK, wizards[1], 8));
        } else if (numOfPlayers == 3) {
            _players.add(new Player(Color.WHITE, wizards[0], 6));
            _players.add(new Player(Color.BLACK, wizards[1], 6));
            _players.add(new Player(Color.GRAY, wizards[2], 6));
        } else if (numOfPlayers == 4) {
            _players.add(new Player(Color.WHITE, wizards[0], 8));
            _players.add(new Player(Color.WHITE, wizards[1], 0));
            _players.add(new Player(Color.BLACK, wizards[2], 8));
            _players.add(new Player(Color.BLACK, wizards[3], 0));
        }
    }

    /** Initialize clouds.
     * @param numOfPlayers number of players in the game, to decide number of students on each Cloud.
     */
    private void initializeClouds(int numOfPlayers){
        _clouds = new ArrayList<>();
        for (int i=0; i<numOfPlayers; i++) {
            _clouds.add(new Cloud(numOfPlayers));
        }
    }

    /** Extract 7 students from bag and add to the entrance of player PLAYER.
     */
    private void setSevenStudents(Player player){
        for(int i = 0; i < 7; i++) {
            try {
                player.addStudentToEntrance(_bag.extractStudent());
            } catch (EmptyBagException e) {
                e.printStackTrace();
            }
        }
    }

    /** This is a method for the Planning phase.
     * Draw 3 students from _bag and then place them on ONLY ONE cloud tile. Repeat this method for the 2nd and 3rd cloud tiles.
     */
    void addStudentsToCloud(Cloud cloud){
        try {
            for (int i = 0; i < 3; i++) {
                cloud.addStudent(_bag.extractStudent());
            }
        } catch (FullCloudException e1) {
            throw error("the cloud is full.");
        } catch (EmptyBagException e2) {
            throw error("the bag is empty.");
        }
    }

    /** This is a method for the Planning phase.
     * Player PLAYER plays the assistant card ASSISTANT when other players are not playing the same card.
     */
    void playAssistant(Assistant assistant, Player player) {
        for (Player p : _players) {
            if (p != player && assistant.getMaxSteps() == p.getMostRecentAssistant().getMaxSteps()) {
                throw new GameException("The card is being used.");
            }
        }
        player.useAssistant(assistant);
    }

    /** This is a method for the Action phase.
     * The player PLAYER moves a student to the correspondent dining room.
     */
    void moveStudentToDiningRoom(Player player, StudentColor student) {
        try{
            try{
                player.removeStudentFromEntrance(student);
            } catch (GameException e1) {
                throw error(e1.getMessage());
            }
            player.addToDiningtable(student);
        } catch (FullTableException e2) {
            throw error("The dining table is full.");
        }
    }

    /** This is a method for the Action phase.
     * The player PLAYER moves a student to the island ISLAND.
     */
    void moveStudentToIsland(Player player, Island island, StudentColor student) {
        try{
            player.removeStudentFromEntrance(student);
        } catch (GameException e1) {
            throw error(e1.getMessage());
        }
        island.addStudent(student);
    }


    /** In the case that x == numIslands - 1(ex. x = 11, y = 0), use the yth island to merge the xth island, just like deleting the tail node of a linked list.
     * @param x the index of one of the islands to be merged.
     * @param y the index of one of the islands to be merged.
     */
    private void mergeIslands(int x, int y) {
        if (x == _numIslands - 1) {
            islands.get(y).copyFrom(islands.get(x));
            if (x == _motherNature) {
                _motherNature = y;
            }
        }
        else {
            islands.get(x).copyFrom(islands.get(y));
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
     * @return number of coins not obtained by any player */
    int getSpareCoins() {
        return this._spareCoins;
    }

    /**
     * @return the island with mothernature */
    Island getMotherNature() {
        return islands.get(_motherNature);
    }

    /**
     * Move mothernature to xth island */
    void moveMotherNature(int x) {
        if (_motherNature == x || x > _numIslands - 1) {
            throw new GameException("Illegal movement.");
        } else {
            _motherNature = x;
        }
    }

    /**
     * @return the current player */
    Player getCurrentPlayer() {
        return this._currentPlayer;
    }

    /**
     * Set current player to PLAYER */
    void setCurrentPlayer(Player player) {
        _currentPlayer = player;
    }

    /**
     * @return the winner of current game */
    public Player getWinner() {
        return this._winner;
    }

    /** @return true iff it would currently be legal for PLAYER to move */
    boolean isLegal(Player player) {
        return !_currentPlayer.equals(player);
    }

    /** All islands */
    private HashMap<Integer, Island> islands;

    /** Coins not obtained by any player. Initially set to 20 */
    private int _spareCoins;

    /** The index of the island with mothernature */
    private int _motherNature;

    /** Current player */
    private Player _currentPlayer;

    /** The winner of current game */
    private Player _winner;

    /** Number of islands (merged islands count as 1) */
    private int _numIslands;

    /** Students bag */
    private final Bag _bag;

    /** Current players */
    private ArrayList<Player> _players;

    /** Clouds on board */
    private ArrayList<Cloud> _clouds;
}
