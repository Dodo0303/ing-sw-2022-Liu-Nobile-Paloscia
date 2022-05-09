package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.*;

import java.util.*;

import static it.polimi.ingsw.Exceptions.GameException.error;

/** A new game.
 */

public class GameModel {

    /** An uninitialized GameModel.  Only for use by subtypes. */
    protected GameModel() {
    }

    /**
     * Constructor of GameModel.
     * @param wizards the wizards chosen by each player.
     * @param numOfPlayers number of players in game. Must be between 2 and 4.
     */
    public GameModel(Wizard[] wizards, int numOfPlayers) {
        if (numOfPlayers < 2 || numOfPlayers > 4) {
            throw new GameException();
        }
        _bag = new Bag();
        _spareCoins = 20;
        _numIslands = 12;
        initializeIslands();
        initializePlayers(wizards, numOfPlayers);
        initializeClouds(numOfPlayers);
        initializeInfluences();
        setEntranceStudents(numOfPlayers);
        Random randomMothernature = new Random();
        _motherNature = randomMothernature.nextInt(12); //automatically choose a random island for mothernature.
        Random randomPlayer = new Random();
        _currentPlayer = _players.get(randomPlayer.nextInt(numOfPlayers)); //Determine the first player at random.
    }

    /** Initialize 12 islands with 2 students each.
     */
    private void initializeIslands() {
        int rnd;
        _islands = new HashMap<>();
        ArrayList<StudentColor> twoForEachColor = new ArrayList<>();
        for (StudentColor color : StudentColor.values()) {
            for(int i = 0; i < 2; i++) {
                twoForEachColor.add(color);
            }
        }
        for(int i = 0; i < 12; i++) {
            _islands.put(i, new Island());
            if (i != _motherNature && i != ((_motherNature < 6)? _motherNature + 6 : _motherNature - 6)) {
                rnd = new Random().nextInt(twoForEachColor.size()); //rnd is a random number between 0 and twoForEachColor.size().
                _islands.get(i).addStudent(twoForEachColor.remove(rnd)); //add the student at rnd position of the arraylist twoForEachColor to the ISLAND and then remove it from twoForEachColor.
            }
        }
    }

    /**
     * Creates instances of class Player.
     * @param wizards, the array of Wizards chosen by each player.
     * @param numOfPlayers number of player, to decide how many objects must be created.
     */
    private void initializePlayers(Wizard[] wizards, int numOfPlayers) {
        _players = new ArrayList<>();
        if (numOfPlayers == 2) {
            _players.add(new Player(Color.WHITE, wizards[0], numOfPlayers));
            _players.add(new Player(Color.BLACK, wizards[1], numOfPlayers));
        } else if (numOfPlayers == 3) {
            _players.add(new Player(Color.WHITE, wizards[0], numOfPlayers));
            _players.add(new Player(Color.BLACK, wizards[1], numOfPlayers));
            _players.add(new Player(Color.GRAY, wizards[2], numOfPlayers));
        } else if (numOfPlayers == 4) {
            _players.add(new Player(Color.WHITE, wizards[0], numOfPlayers));
            _players.add(new Player(Color.WHITE, wizards[1], numOfPlayers));
            _players.add(new Player(Color.BLACK, wizards[2], numOfPlayers));
            _players.add(new Player(Color.BLACK, wizards[3], numOfPlayers));
        }
    }

    /**
     * Initialize the map of influences with all zeros
     */
    private void initializeInfluences() {
        influences = new HashMap<>();
        for (Island island :
                _islands.values()) {
            Integer[] values = {0,0,0,0};
            influences.put(island, values);
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

    /** Extract 7/9 students from bag and add to the entrance of each player.
     */
    void setEntranceStudents(int numOfPlayers){
        int x = (numOfPlayers == 3)? 9 : 7;
        for (Player player : _players) {
            for(int i = 0; i < x; i++) {
                try {
                    player.addStudentToEntrance(_bag.extractStudent());
                } catch (EmptyBagException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @return number of coins not obtained by any player */
    int getSpareCoins() {
        return this._spareCoins;
    }

    /**
     * @return the island with mothernature */
    public Island getMotherNature() {
        return _islands.get(_motherNature);
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


    /** * Set current player to PLAYER */
    public void setMothernature(int x) {
        _motherNature = x;
    }
    public void setInfluences(Island island, Integer[] influences) {
        if (this.influences.replace(island, influences) == null)
            throw new IllegalArgumentException("The island doesn't exists");
    }

    /** * Set _numIslands to x, then return it.*/
    public int setNumIslands(int x) {
        _numIslands = x;
        return this._numIslands;
    }

    /** @return the players.*/
    public ArrayList<Player> getPlayers() {
        return this._players;
    }

    /** @return the clouds.*/
    public ArrayList<Cloud> getClouds() {
        return this._clouds;
    }

    /** @return the islands.*/
    public HashMap<Integer, Island> getIslands() {
        return this._islands;
    }

    /** @return _bag.*/
    public Bag getBag() {
        return this._bag;
    }

    /** @return _numIslands.*/
    public int getNumIslands() {
        return this._numIslands;
    }

    /** @return _motherNature.*/
    public int getMotherNatureIndex() {
        return this._motherNature;
    }

    /**
     *
     * @param island island of interest
     * @return the influence of each player in that island
     */
    public List<Integer> getInfluences(Island island) {
        List<Integer> res = new ArrayList<Integer>();
        Integer[] values = influences.get(island);
        Collections.addAll(res, values);
        return res;
    }

    /**
     * Return the influence of a certain player in a certain island
     * @param island island of interest
     * @param player player of interest
     * @return the influence of that player in that island
     */
    public int getInfluenceOfPlayer(Island island, Player player) {
        int index = _players.indexOf(player);
        return influences.get(island)[index];
    }

    public ReadOnlyGameModel readOnlyGame() {
        return _readonlyGame;
    }


    /** All islands */
    private HashMap<Integer, Island> _islands;

    /** Coins not obtained by any player. Initially set to 20 */
    private int _spareCoins;

    /** The index of the island with mothernature */
    private int _motherNature;

    /** Current player */
    private Player _currentPlayer;

    /** Number of islands (merged islands count as 1) */
    private int _numIslands;

    /** Students bag */
    private Bag _bag;

    /** Current players */
    private ArrayList<Player> _players;

    /** Clouds on board */
    private ArrayList<Cloud> _clouds;

    /**
     * Maps each island with an array of integers representing the influence of each player in that island
     */
    private Map<Island, Integer[]> influences;

    /** A read-only version of this Board. */
    private ReadOnlyGameModel _readonlyGame;
}
