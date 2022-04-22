package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.HashMap;
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
            if (i != _motherNature && i != oppositeMothernature()) {
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

    /** This is a method for the Planning phase.
     * Draw 3/4 students from _bag and then place them on ONLY ONE cloud tile. Repeat this method for the 2nd and 3rd cloud tiles.
     */
    void addStudentsToCloud(Cloud cloud, int numOfPlayers){
        int x = (numOfPlayers == 3)? 4 : 3;
        try {
            for (int i = 0; i < x; i++) {
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
            if (p != player && (p.getUsedAssistant() != null && assistant.getMaxSteps() == p.getUsedAssistant().getMaxSteps())) {
                throw error("The card is being used.");
            }
        }
        player.useAssistant(assistant);
    }

    //!The following two methods implement game logic, should we move them to GameController?
    // YL : My thought was that the Game controller calls these methods when needed,
    // such as Game g = new Game()
    // g.moveStudentTODiningRoom(player, student)
    // etc...
    // In this case, we dont need to pass Game g to them every time we use them like moveStudentTODiningRoom(g, g.player, g.student)
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
            player.addToDiningTable(student);
        } catch (FullTableException e2) {
            throw error("The dining table is full.");
        }
    }

    /** This is a method for the Action phase.
     * The player PLAYER moves a student to the island ISLAND.
     */
    void moveStudentToIsland(Player player, Island island, StudentColor student) {
        try {
            player.removeStudentFromEntrance(student);
        } catch (GameException e1) {
            throw error(e1.getMessage());
        }
        island.addStudent(student);
    }

    /** This is a method for the Action phase.
     * Player PLAYER moves mothernature to xth island and try to control/conquer the xth island. */
    void moveMotherNature(int x, Player player) {
        int distance;
        if (x > _numIslands - 1) {
            throw error("The chosen island does not exist.");
        } else if (x < _motherNature) {
            distance = x + _numIslands - _motherNature;
        } else if (x > _motherNature) {
            distance = x - _motherNature;
        } else {
            distance = 0;
        }
        if (distance > player.getUsedAssistant().getMaxSteps() || distance == 0) {
            throw error("The chosen island is too far away!");
        } else {
            _motherNature = x;
            controlIsland(x);
            unifyIslands(x);
        }
    }

    /**@return the opposite index of mothernature. */
    int oppositeMothernature() {
        //TODO
        return 0;
    }

    /** This is a method for the Action phase.
     * The player PLAYER takes 3/4 students from the cloud CLOUD, and then place them on his entrance.
     */
    void takeStudentsFromCloud(Player player, Cloud cloud, int numOfPlayers) {
        int x = (numOfPlayers == 3)? 4 : 3;
        try {
            for (int i = 0; i < x; i++) {
                player.addStudentToEntrance(cloud.extractStudent());
            }
        } catch (EmptyCloudException e) {
            throw error(e.getMessage());
        }
    }

    /** First, check if the xth island can be controlled/conquered.
     * If positive, then the color with most influence controls the island ISLAND.
     * If negative, do nothing. */
    private void controlIsland(int x) {
        Island island = _islands.get(x);
        int influence;
        int maxInfluence = 0;
        Player maxInfluencer = null;
        for (int i = 0; i < _players.size(); i++) {
            influence = island.calculateInfluence(_players.get(i));
            if (influence > maxInfluence) {
                maxInfluencer = _players.get(i);
                maxInfluence = influence;
            }
        }
        if (maxInfluencer != null) {
            island.setTowerColor(maxInfluencer.getColor());
        }

    }

    /** First, check if the xth island can merge any adjacent island.
     * If positive, then call mergeIslands().
     * If negative, do nothing. */
    private void unifyIslands(int x) {
        Island island = _islands.get(x);
        int left = (x > 0) ? x - 1 : _numIslands - 1;
        if (_islands.get(left).getTowerColor().equals(island.getTowerColor())) {
            mergeIslands(left, x--);
        }
        int right = (x < _numIslands - 1) ? x + 1 : 0;
        if (_islands.get(right).getTowerColor().equals(island.getTowerColor())) {
            mergeIslands(x, right);
        }
    }


    /** In the case that x == numIslands - 1(ex. x = 11, y = 0), use the yth island to merge the xth island, just like deleting the tail node of a linked list.
     * @param x the index of one of the islands to be merged.
     * @param y the index of one of the islands to be merged.
     */
    private void mergeIslands(int x, int y) {
        if (x == _numIslands - 1) {
            _islands.get(y).copyFrom(_islands.get(x));
            if (x == _motherNature) {
                _motherNature = y;
            }
        }
        else {
            _islands.get(x).copyFrom(_islands.get(y));
            if (y == _motherNature) {
                _motherNature = x;
            }
            for (int i = y; i < _numIslands - 1; i++) {
                _islands.put(i, _islands.get(i + 1)); // move islands after the yth forward by 1.
            }
        }
        _islands.remove(_numIslands--);
    }

    /**
     * @return number of coins not obtained by any player */
    int getSpareCoins() {
        return this._spareCoins;
    }

    /**
     * @return the island with mothernature */
    Island getMotherNature() {
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

    /**
     * Set current player to PLAYER */
    void setMothernature(int x) {
        _motherNature = x;
    }

    /**
     * @return the winner of current game */
    public Player getWinner() {
        return this._winner;
    }

    /**
     * @return the players.*/
    public ArrayList<Player> getPlayers() {
        return this._players;
    }

    /**
     * @return the clouds.*/
    public ArrayList<Cloud> getClouds() {
        return this._clouds;
    }

    /**
     * @return the clouds.*/
    public HashMap<Integer, Island> getIslands() {
        return this._islands;
    }

    /** @return true iff it would currently be legal for PLAYER to move */
    boolean isLegal(Player player) {
        return !_currentPlayer.equals(player);
    }

    /** All islands */
    private HashMap<Integer, Island> _islands;

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
