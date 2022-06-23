package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.Character.CharacterCard;
import it.polimi.ingsw.Model.Character.CharacterFactory;

import java.io.Serializable;
import java.util.*;

/** A new game.
 */

public class GameModel implements Serializable {

    /** An uninitialized GameModel.  Only for use by subtypes. */
    protected GameModel() {
    }

    /** All islands */
    private HashMap<Integer, Island> _islands;

    /** The index of the island with mothernature */
    private int _motherNature;

    /**
     * Maps each island with an array of integers representing the influence of each player in that island
     */
    private Map<Island, Integer[]> influences;

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
        initializeInfluences();
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

        /**
        for (int i = 0; i < 12; i++) {
            characterIDs.add(i, i + 1);
            characters.add(factory.createCharacter(i + 1));
        }
         */

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

    public List<StudentColor> getEntranceOfPlayer(Player player) {
        return player.getEntranceStudents();
    }

    /** @return number of coins not obtained by any player */
    int getSpareCoins() {
        return this._spareCoins;
    }

    /** @return the island with mothernature */
    public Island getMotherNature() {
        return this._islands.get(_motherNature);
    }


    /** Set position of mothernature */
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

    /** @return _bag.*/
    public Bag getBag() {
        return this._bag;
    }

    /** @return _numIslands.*/
    public int getNumIslands() {
        return this._islands.size();
    }

    /** @return _motherNature.*/
    public int getMotherNatureIndex() {
        return this._motherNature;
    }

    /** @return a copy of the list of spare professors.*/
    public ArrayList<StudentColor> getProfessors() {
        return new ArrayList<>(this._professors);
    }

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
     *
     * @param island island of interest
     * @return the influence of each player in that island
     */
    public List<Integer> getInfluences(Island island) {
        List<Integer> res = new ArrayList<>();
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

    public void setAssistantOfPlayer(String playerNickname, Assistant assistant) {
        for (Player player : _players) {
            if (player.getNickName().equals(playerNickname)) {
                player.useAssistant(assistant);
                break;
            }
        }
    }

    public int getPlayerIndexFromNickname(String nickname) {
        for (Player player : this._players) {
            if (player.getNickName().equals(nickname)) return this._players.indexOf(player);
        }
        throw new NoSuchElementException();
    }

    public StudentColor removeStudentFromEntrance(Player p, int studentIndex) {
        return p.removeStudentFromEntrance(studentIndex);
    }

    public void addStudentToEntrance(Player p, StudentColor student){
        p.addStudentToEntrance(student);
    }

    public void removeStudentFromTable(Player p, StudentColor color) throws EmptyTableException {
        p.removeFromDiningTable(color);
    }

    public void addToDiningTable(Player p, StudentColor student) throws FullTableException {
        p.addToDiningTable(student);
    }

    public void addStudentToIsland(StudentColor student, Island island) {
        island.addStudent(student);
    }

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

    public void addNoEntry(Island island){
        island.addNoEntry();
    }

    public CharacterCard getCharacterById(int ID){
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
     */
    public void updateCharacterById(CharacterCard update) {
        for (int i = 0; i < characters.size(); i++) {
            if (characters.get(i).getID() == update.getID()) {
                characters.set(i, update);
                return;
            }
        }
        throw new GameException("Character not present");
    }

    public void removeCoinsToPlayer(String playerNickname, int characterID){
        Player p = null;
        for (Player player :
                _players) {
            if (player.getNickName().equals(playerNickname))
                p = player;
        }
        if (p==null) {
            throw new GameException("That player doesn't exist");
        } else {

            p.removeCoins(getCharacterById(characterID).getPrice());

        }
    }

    public void addCoinsToPlayer(String playerNickname, int coins){
        Player p = null;
        for (Player player :
                _players) {
            if (player.getNickName().equals(playerNickname))
                p = player;
        }
        if (p==null) {
            throw new GameException("That player doesn't exist");
        } else {
            for (int i = 0; i < coins; i++) {
                p.addCoin();
            }
        }
    }

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

    public void useEffectOfCharacter(int characterID) throws WrongEffectException, NotEnoughNoEntriesException {
        getCharacterById(characterID).useEffect();
    }

    public StudentColor useEffectOfCharacter(int characterID, int studentIndex, StudentColor studentToAdd) throws WrongEffectException {
        return getCharacterById(characterID).useEffect(studentIndex, studentToAdd);
    }

    public Player getPlayerByNickname(String playerNickname) throws GameException{
        for (Player player :
                _players) {
            if (player.getNickName().equals(playerNickname))
                return player;
        }
        throw new GameException("The player doesn't exist");
    }

    public void sortPlayers() {
        this._players.sort(new PlayerComparator());
    }

    public void set_islands(HashMap<Integer, Island> _islands) {
        this._islands = _islands;
    }

    public void set_motherNature(int _motherNature) {
        this._motherNature = _motherNature;
    }

    public void setInfluences(Map<Island, Integer[]> influences) {
        this.influences = influences;
    }

    public void set_players(ArrayList<Player> _players) {
        this._players = _players;
    }

    public void set_bag(Bag _bag) {
        this._bag = _bag;
    }

    public void set_clouds(ArrayList<Cloud> _clouds) {
        this._clouds = _clouds;
    }

    public void set_spareCoins(int _spareCoins) {
        this._spareCoins = _spareCoins;
    }

    public void set_professors(ArrayList<StudentColor> _professors) {
        this._professors = _professors;
    }

    public List<CharacterCard> getCharacters() {
        return characters;
    }

    private class PlayerComparator implements Comparator<Player> {

        @Override
        public int compare(Player p1, Player p2) {
            return Integer.compare(p1.getUsedAssistant().getValue(), p2.getUsedAssistant().getValue());
        }
    }

}
