package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.Character.CharacterCard;
import it.polimi.ingsw.Model.Character.CharacterFactory;

import java.io.Serializable;
import java.util.*;

/** A new game.
 */

public class GameModel implements Serializable {

    /** All islands */
    private HashMap<Integer, Island> _islands;

    /** The index of the island with mothernature */
    private int _motherNature;


    /** Current players */
    private ArrayList<Player> _players;

    /** Students bag */
    private Bag _bag;

    /** Clouds on board */
    private ArrayList<Cloud> _clouds;

    /** Coins not obtained by any player. Initially set to 20 */
    private int _spareCoins;

    /** Professors yet to be assigned to a player */
    private ArrayList<StudentColor> _professors;

    private List<CharacterCard> characters;



    /**
     * Constructor of GameModel.
     * @param wizards the wizards chosen by each player.
     * @param numOfPlayers number of players in game. Must be between 2 and 4.
     */
    public GameModel(int numOfPlayers, String[] nicknames, Wizard[] wizards) {
        if (numOfPlayers < 2 || numOfPlayers > 4) {
            throw new GameException();
        }
        this._bag = new Bag();
        this._spareCoins = 20;
        this._motherNature = new Random().nextInt(12); // Automatically choose a random island for mothernature.
        initializeIslands();
        initializePlayers(wizards, numOfPlayers, nicknames);
        initializeCharacters();
        this._professors = new ArrayList<>(Arrays.asList(StudentColor.BLUE, StudentColor.GREEN, StudentColor.PINK, StudentColor.RED, StudentColor.YELLOW));
        initializeClouds(numOfPlayers);
        setEntranceStudents(numOfPlayers);
    }


    // INITIALIZERS

    /**
     * Initialize 12 islands with 2 students each.
     */
    private void initializeIslands() {
        int rnd, i;
        _islands = new HashMap<>();
        ArrayList<StudentColor> twoForEachColor = new ArrayList<>(10);

        for (StudentColor color : StudentColor.values()) {
            for(i = 0; i < 2; i++) {
                twoForEachColor.add(color);
            }
        }

        for(i = 0; i < 12; i++) {
            _islands.put(i, new Island());
            if (i != _motherNature && i != (_motherNature + 6) % 12) {
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
    private void initializePlayers(Wizard[] wizards, int numOfPlayers, String[] nicknames) {
        _players = new ArrayList<>(numOfPlayers);
        if (numOfPlayers == 2) {
            _players.add(new Player(nicknames[0], Color.WHITE, wizards[0], numOfPlayers));
            _players.add(new Player(nicknames[1], Color.BLACK, wizards[1], numOfPlayers));
        } else if (numOfPlayers == 3) {
            _players.add(new Player(nicknames[0], Color.WHITE, wizards[0], numOfPlayers));
            _players.add(new Player(nicknames[1], Color.BLACK, wizards[1], numOfPlayers));
            _players.add(new Player(nicknames[2], Color.GRAY, wizards[2], numOfPlayers));
        } else if (numOfPlayers == 4) {
            _players.add(new Player(nicknames[0], Color.WHITE, wizards[0], numOfPlayers));
            _players.add(new Player(nicknames[1], Color.WHITE, wizards[1], numOfPlayers));
            _players.add(new Player(nicknames[2], Color.BLACK, wizards[2], numOfPlayers));
            _players.add(new Player(nicknames[3], Color.BLACK, wizards[3], numOfPlayers));
        }
    }

    /** Initialize clouds.
     * @param numOfPlayers number of players in the game, to decide number of students on each Cloud.
     */
    private void initializeClouds(int numOfPlayers){
        _clouds = new ArrayList<>(numOfPlayers);
        for (int i=0; i < numOfPlayers; i++) {
            _clouds.add(new Cloud(numOfPlayers));
        }
    }

    /**
     * Randomly choose and initialize three characters
     */
    private void initializeCharacters(){
        characters = new ArrayList<>();
        CharacterFactory factory = new CharacterFactory(this);
        List<Integer> characterIDs = new ArrayList<>();

        /*
        Random rnd = new Random();
        int id;
        for (int i = 0; i < 3; i++) {
            id = rnd.nextInt(12);
            id++;
            while(characterIDs.contains(id)){
                id = rnd.nextInt(12);
                id++;
            }
            characterIDs.add(i, id);
            characters.add(factory.createCharacter(id));
        }

         */
        //todo I NEED TO TEST EVERY CHARACTER CARD, KEEP THIS PART OF CODE, I WILL DELETE THIS PART AND UNCOMMENT THE ABOVE PART AFTER THE FEATURE IS FULLY TESTED
        for (int i = 0; i < 12; i++) {
            characterIDs.add(i, i + 1);
            characters.add(factory.createCharacter(i + 1));
        }


    }

    /** 
     * Extract 7/9 students from bag and add to the entrance of each player.
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
     * Get the entrance of a specific player.
     * @param player Player instance.
     * @return the entrance of the specified player.
     */
    public List<StudentColor> getEntranceOfPlayer(Player player) {
        return player.getEntranceStudents();
    }

    /** 
     * @return number of coins not obtained by any player 
     */
    int getSpareCoins() {
        return this._spareCoins;
    }

    /** @return the island with mothernature */
    public Island getIslandWithMotherNature() {
        return this._islands.get(_motherNature);
    }


    /**
     * Set the position of mothernature
     * @param x index of the island that will receive mothernature
     */
    public void setMothernature(int x) {
        if (_islands.containsKey(x))
            this._motherNature = x;
        else
            throw new GameException("The island " + x + " doesn't exist");
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

    /** @return the bag.*/
    public Bag getBag() {
        return this._bag;
    }

    /** @return the total number of islands*/
    public int getNumIslands() {
        return this._islands.size();
    }

    /** @return the index of the island that has mothernature*/
    public int getMotherNatureIndex() {
        return this._motherNature;
    }

    /** @return a copy of the list of spare professors.*/
    public ArrayList<StudentColor> getProfessors() {
        return new ArrayList<>(this._professors);
    }

    /**
     * Checks whether the player can afford a character.
     * @param playerNickname player nickname that wants to use the character.
     * @param characterID ID of the character that has to be used.
     * @return true if the player can use this character. False if not.
     */
    public boolean canAffordCharacter(String playerNickname, int characterID){
        try {
            Player p = getPlayerByNickname(playerNickname);
            for (CharacterCard character:
                    characters) {
                if (character.getID() == characterID) {
                    if (p.getCoins() >= character.getPrice()) {
                        return true;
                    } else {
                        break;
                    }
                }
            }
        } catch (GameException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    /**
     * Removes a professor from spare professors.
     * @param color is the color of the professor to be removed.
     */
    public void removeSpareProfessor(StudentColor color) {
        this._professors.remove(color);
    }

    /**
     * Method called from controller to use an assistant and to set it as last assistant used
     * @param playerNickname nickname of the player that is using the assistant
     * @param assistant assistant used by the player
     */
    public void setAssistantOfPlayer(String playerNickname, Assistant assistant) {
        Player player = getPlayerByNickname(playerNickname);
        player.useAssistant(assistant);
    }

    /**
     * Get the current index of the player given its nickname
     * @param nickname nickname of the player
     * @return the current index of the player
     */
    public int getPlayerIndexFromNickname(String nickname) {
        for (Player player : this._players) {
            if (player.getNickName().equals(nickname)) return this._players.indexOf(player);
        }
        throw new NoSuchElementException();
    }

    /**
     * Remove a student from the entrance of a player given the player and the index of the student
     * @param p player
     * @param studentIndex index of the student to be removed
     * @return  the color of the removed student
     */
    public StudentColor removeStudentFromEntrance(Player p, int studentIndex) {
        return p.removeStudentFromEntrance(studentIndex);
    }

    /**
     * Add a student to the entrance of a player
     * @param p player
     * @param student student to be added
     * @throws GameException if the entrance is full
     */
    public void addStudentToEntrance(Player p, StudentColor student) throws GameException{
        p.addStudentToEntrance(student);
    }

    /**
     * Removes a student from the table of a player
     * @param p player
     * @param color color of the student (and table) to be removed
     * @throws EmptyTableException if the table has no students
     */
    public void removeStudentFromTable(Player p, StudentColor color) throws EmptyTableException {
        p.removeFromDiningTable(color);
    }

    /**
     * Adds a student to the dining table of the player
     * @param p player
     * @param student student color to be added
     * @throws FullTableException if the table has too many students
     */
    public void addToDiningTable(Player p, StudentColor student) throws FullTableException {
        p.addToDiningTable(student);
    }

    /**
     * This method adds a student to an island
     * @param student student color that has to be added
     * @param island island that will receive the student
     */
    public void addStudentToIsland(StudentColor student, Island island) {
        island.addStudent(student);
    }

    /**
     * Get the number of students that are in the table of a player
     * @param p player
     * @param color color of the students
     * @return the number of students in the table
     */
    public int getTableNumber(Player p, StudentColor color) {
        return p.getDiningTables().get(color).getNumOfStudents();
    }

    /**
     * draw one student from the bag
     * @return the StudentColor of the student
     * @throws EmptyBagException if the bga is empty
     */
    public StudentColor drawStudentFromBag() throws EmptyBagException {
        return _bag.extractStudent();
    }

    /**
     * This method adds a new-entry tile to an island
     * @param island island that will receive the no-entry tile
     */
    public void addNoEntry(Island island){
        island.addNoEntry();
    }

    /**
     *
     * @param ID ID of the character
     * @return the CharacterCard that has the given ID, if present
     * @throws GameException if there are no characters with the given ID
     */
    public CharacterCard getCharacterById(int ID) throws GameException{
        for (CharacterCard character:
             characters) {
            if (character.getID() == ID)
                return character;
        }
        throw new GameException("Character not present");
    }

    /**
     * Update the character card if already present
     * @param update Character card that should replace the existing one
     * @throws GameException if the character doesn't exist
     */
    public void updateCharacterById(CharacterCard update) throws GameException{
        for (int i = 0; i < characters.size(); i++) {
            if (characters.get(i).getID() == update.getID()) {
                characters.set(i, update);
                return;
            }
        }
        throw new GameException("Character not present");
    }


    /**
     * Remove the coins to a player given the character that he wants to use
     * @param playerNickname nickname of the player
     * @param characterID character ID
     * @throws GameException if the player doesn't exist
     */
    public void removeCoinsToPlayer(String playerNickname, int characterID) throws GameException{
        Player p = getPlayerByNickname(playerNickname);
        p.removeCoins(getCharacterById(characterID).getPrice());
    }

    /**
     * Add coins to the player
     * @param playerNickname player nickname
     * @param coins coins to be added
     */
    public void addCoinsToPlayer(String playerNickname, int coins){
        Player p = getPlayerByNickname(playerNickname);
        for (int i = 0; i < coins; i++) {
            p.addCoin();
        }
    }

    /**
     * Set the correct number of towers to each player, checking how many islands have been conquered
     */
    public void calculateNumIslandsForPlayers() {
        int numWhite = 0;
        int numBlack = 0;
        int numGray = 0;
        for (int i = 0; i < _islands.size(); i++) {
            if (!_islands.get(i).getTowerColor().equals(Color.VOID)) {
                if (_islands.get(i).getTowerColor().equals(Color.WHITE)) {
                    numWhite += _islands.get(i).getNumTower();
                } else if (_islands.get(i).getTowerColor().equals(Color.BLACK)) {
                    numBlack += _islands.get(i).getNumTower();
                } else if (_islands.get(i).getTowerColor().equals(Color.GRAY)) {
                    numGray += _islands.get(i).getNumTower();
                }
            }
            for (Player player : _players) {
                if (player.getColor().equals(Color.WHITE)) {
                    player.setTowers(player.getMaxTowerNum() - numWhite);
                } else if (player.getColor().equals(Color.BLACK)) {
                    player.setTowers(player.getMaxTowerNum() - numBlack);
                } else if (player.getColor().equals(Color.GRAY)) {
                    player.setTowers(player.getMaxTowerNum() - numGray);
                }
            }
        }
    }

    /**
     * Use the effect of a character
     * @param characterID character to be used
     * @throws WrongEffectException if the effect expects some parameters
     * @throws NotEnoughNoEntriesException if the character has no no-entry tiles left to use
     */
    public void useEffectOfCharacter(int characterID) throws WrongEffectException, NotEnoughNoEntriesException {
        getCharacterById(characterID).useEffect();
    }

    /**
     * Use effect of a character
     * @param characterID character to be used
     * @param studentIndex index of the student to be moved from the character
     * @param studentToAdd student that will replace the one that has been removed
     * @return the color of the student removed from the character
     * @throws WrongEffectException if the character doesn't expect these parameters
     */
    public StudentColor useEffectOfCharacter(int characterID, int studentIndex, StudentColor studentToAdd) throws WrongEffectException {
        return getCharacterById(characterID).useEffect(studentIndex, studentToAdd);
    }

    /**
     * Get the Player object given the nickname
     * @param playerNickname nickname of the player that has to be retrieved
     * @return the Player instance
     * @throws GameException if there is no player with such nickname
     */
    public Player getPlayerByNickname(String playerNickname) throws GameException{
        for (Player player :
                _players) {
            if (player.getNickName().equals(playerNickname))
                return player;
        }
        throw new GameException("The player doesn't exist");
    }

    /**
     * Sort the player based on the last assistant that they used
     */
    public void sortPlayers() {
        this._players.sort(new PlayerComparator());
    }

    /**
     * Set the island to the ones given.
     * This method is used from the client.
     * @param _islands islands to be set.
     */
    public void set_islands(HashMap<Integer, Island> _islands) {
        this._islands = _islands;
    }

    /**
     * Set the players to the ones given.
     * This method is used from the client.
     * @param _players players to be set.
     */
    public void set_players(ArrayList<Player> _players) {
        this._players = _players;
    }

    /**
     * Set the clouds to the ones given.
     * This method is used from the client.
     * @param _clouds clouds to be set.
     */
    public void set_clouds(ArrayList<Cloud> _clouds) {
        this._clouds = _clouds;
    }

    /**
     * Get the characters allowed for this game.
     * @return the list of the characters.
     */
    public List<CharacterCard> getCharacters() {
        return characters;
    }

    private class PlayerComparator implements Comparator<Player> {

        @Override
        public int compare(Player p1, Player p2) {
            return Integer.compare(p1.getUsedAssistant().getValue(), p2.getUsedAssistant().getValue());
        }
    }

    /**
     * Set the characters for this game.
     * This method is used for test purposes only.
     * @param characters characters to be set
     */
    protected void setCharacters(List<CharacterCard> characters) {
        this.characters = characters;
    }

}
