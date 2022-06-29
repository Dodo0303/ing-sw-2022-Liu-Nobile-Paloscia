package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.EmptyTableException;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Exceptions.GameException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**  Class for the player */

public class Player implements Serializable {

    /** Color of the player */
    private Color _color;

    /** Number of towers owned by the player */
    private int _towerNum;

    /** Maximum amount of towers that the player can hold */
    private final int _maxTowerNum;

    /** Number of coins owned by the player */
    private int _coins;

    /** The nickname set by the player */
    private String _nickName;

    /** The number of professors of each color. */
    private List<StudentColor> _professors;

    /** The number of students in the entrance of each color. */
    private List<StudentColor> _entranceStudents;

    /** Max amount of students in the entrance */
    private final int _maxEntranceStudents;

    /** Assistant cards of the player */
    private final ArrayList<Assistant> _assistants = new ArrayList<>(10);

    /** Last used assistant. */
    private Assistant _lastUsedAssistant;

    /** Dining tables of the player */
    private HashMap<StudentColor, DiningTable> _diningTable;

    /** Player of the team that controls the towers */
    private Player captain;



    // CONSTRUCTOR

    
    Player(String nickname, Color color, Wizard wizard, int numOfPlayers) {
        this._nickName = nickname;
        this._color = color;
        this._coins = 12;//todo =0 after tested
        if ((numOfPlayers == 2 || numOfPlayers == 4) && (color != Color.GRAY && color != Color.VOID)) {
            this._towerNum = 8; 
            this._maxTowerNum = 8;
            this._maxEntranceStudents = 7;
        } else if (numOfPlayers == 3 && color != Color.VOID) {
            this._towerNum = 6;
            this._maxTowerNum = 6;
            this._maxEntranceStudents = 9;
        } else {
            throw new IllegalArgumentException();
        }
        this._entranceStudents = new ArrayList<>();
        this._professors = new ArrayList<>();
        initAssistant(wizard);
        initDiningTable();
    }

    Player(String nickname, Color color, Wizard wizard, int numOfPlayers, Player captain) {
        this._nickName = nickname;
        this._color = color;
        this._coins = 12;//todo =0 after tested
        this.captain = captain;
        if ((numOfPlayers == 2 || numOfPlayers == 4) && (color != Color.GRAY && color != Color.VOID)) {
            this._towerNum = 0; 
            this._maxTowerNum = 8;
            this._maxEntranceStudents = 7;
        } else if (numOfPlayers == 3 && color != Color.VOID) {
            this._towerNum = 6;
            this._maxTowerNum = 6;
            this._maxEntranceStudents = 9;
        } else {
            throw new IllegalArgumentException();
        }
        this._entranceStudents = new ArrayList<>();
        this._professors = new ArrayList<>();        
        initAssistant(wizard);
        initDiningTable();
    }

    // INITIALIZATION
    
    /** Initialize the assistants of this player */
    private void initAssistant(Wizard wizard) {
        int index = 0;
        for (int maxSteps = 1; maxSteps <= 5; maxSteps++) {
            for (int j = 0; j < 2; j++) {
                this._assistants.add(new Assistant(++index, maxSteps, wizard));
            }
        }
    }

    /** Initialize the dining tables of this player */
    private void initDiningTable() {
        this._diningTable = new HashMap<>();
        for(StudentColor color : StudentColor.values()) {
            this._diningTable.put(color, new DiningTable(color));
        }
    }

    // ADD AND REMOVE TOWERS

    /**
     * Add towers to this player
     * @param toAdd amount of towers to be added
     */
    public void addTower(int toAdd) {
        if (captain != null) {
            captain.addTower(toAdd);
        } else {
            if (_towerNum < _maxTowerNum)
                this._towerNum+=toAdd;
            else
                throw new GameException("Too many towers");
        }
    }

    /**
     * Add one tower to this player
     */
    public void addTower() {
        if (captain != null) {
            captain.addTower();
        } else {
            if (_towerNum < _maxTowerNum)
                this._towerNum++;
            else
                throw new GameException("Too many towers");
        }
    }

    /**
     * Remove towers from this player
     * @param toRemove amount of towers to be removed
     */
    public void removeTower(int toRemove) {
        if (captain != null) {
            captain.removeTower(toRemove);
        } else {
            if (this._towerNum > 0) {
                this._towerNum -= toRemove;
            } else {
                throw new GameException("Invalid operation.");
            }
        }
    }

    /**
     * Remove one tower from this player
     */
    public void removeTower() {
        if (captain != null) {
            captain.removeTower();
        } else {
            if (this._towerNum > 0) {
                this._towerNum --;
            } else {
                throw new GameException("Invalid operation.");
            }
        }
    }



    // ADD AND REMOVE COINS

    /** Add 1 coin to this player */
    void addCoin() {
        this._coins++;
    }

    /** Remove x coins from this player */
    void removeCoins(int x) {
        if (x < 0)
            throw new GameException("Can't remove a negative number of coins");
        if (_coins >= x)
            this._coins -= x;
        else
            throw new GameException("Not enough coins");
    }



    // ADD AND REMOVE PROFESSORS

    /**
     * Add a professor to this player
     * @param color color of the professor to be added
     */
    public void addProfessor(StudentColor color) {
        if (!this._professors.contains(color))
            this._professors.add(color);
    }

    /**
     * Remove a professor from this player
     * @param color color of the professor to be removed
     */
    public void removeProfessor(StudentColor color) {
            this._professors.remove(color);
    }



    // ADD AND REMOVE STUDENTS FROM ENTRANCE

    /**
     * Add one student in the last position of the entrance
     * @param color color of the student to be added
     */
    public void addStudentToEntrance(StudentColor color) {
        if (_entranceStudents.size() >= _maxEntranceStudents) {
            throw new GameException("Entrance is full");
        } else {
            _entranceStudents.add(color);
        }
    }

    /**
     * Remove a student from the entrance
     * @param index index of the student to be removed
     */
    public StudentColor removeStudentFromEntrance(int index) throws IndexOutOfBoundsException{
        return _entranceStudents.remove(index);
    }

    /** Remove all students in entrance. */
    public void clearEntrance() {
        _entranceStudents.clear();
    }



    // ASSISTANT

    /** Use an assistant card.
     * @param assistant assistant to be used
     * @return the assistant card chosen by the player */
    public Assistant useAssistant(Assistant assistant) {
        int index = assistant.getValue()-1;
        Assistant res = _assistants.get(index);
        if (res == null)
            throw new GameException("Assistant already used");
        _lastUsedAssistant = res;
        _assistants.set(index, null);
        return res;
    }

    // ADD AND REMOVE FROM DINING TABLE

    /** Add a student to the correspondent dining table. 
     * @param color color of the student
     */
    public void addToDiningTable(StudentColor color) throws FullTableException {
        this._diningTable.get(color).addStudent();
    }

    /** Remove a student from the correspondent dining table. 
     * @param color color of the student
     */
    void removeFromDiningTable(StudentColor color) throws EmptyTableException {
        this._diningTable.get(color).removeStudent();
    }



    // UTILS

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(_nickName, player._nickName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_nickName);
    }

    /**
     * Checks whether the player has a certain professor
     * @param color color of the professor
     * @return true if the player has that professor, false if not
     */
    public boolean hasProfessor(StudentColor color) {
        return this._professors.contains(color);
    }



    // GETTERS

    /**
     * 
     * @return the color of the player
     */
    public Color getColor() {
        return this._color;
    }

    /**
     * @return number of towers owned by the player */
    public int getTowerNum() {
        if (captain != null) {
            return captain.getTowerNum();
        } else {
            return this._towerNum;
        }
    }

    /** 
     * @return number of coins owned by the player*/
    public int getCoins() {
        return this._coins;
    }


    /**
     * @return the nickname of the player */
    public String getNickName() {
        return this._nickName;
    }

    /**
     * @return the numbers of professors of each color. */
    public List<StudentColor> getProfessors() {
        return new ArrayList<>(this._professors);
    }

    /**
     * @return unused assistants of the player */
    public ArrayList<Assistant> getAssistants() {
        return this._assistants;
    }

    /**
     * @return the numbers of students in entrance of each color.*/
    public List<StudentColor> getEntranceStudents() {
        return this._entranceStudents;
    }

    /**
     * @return last used assistant */
    public Assistant getUsedAssistant() {
        return this._lastUsedAssistant;
    }

    /**
    * @return the dining tables. */
    public HashMap<StudentColor, DiningTable> getDiningTables() {
        return new HashMap<>(this._diningTable);
    }

    /**
     * @return the maximum amount of students that can be in the entrance
     */
    public int getMaxEntranceStudents() {
        return _maxEntranceStudents;
    }

    /**
     * @return the maximum amount of towers that this player can have
     */
    public int getMaxTowerNum(){
        return _maxTowerNum;
    }

    /**
     * Checks whether the player has only one character left
     * @return true if the player has only one character. false if not.
     */
    public boolean lastAssistant() {
        return assistantsLeft() == 1;
    }


    /**
     * Checks how many assistants the player can still use
     * @return the number of assistants of the player
     */
    public int assistantsLeft() {
        int res = 0;
        for (Assistant assistant :
                _assistants) {
            if (assistant != null) res++;
        }

        return res;
    }

    /**
     * Set the amount of coins owned by the player
     * @param coin amount of coins
     */
    public void setCoins(int coin) {
        this._coins = coin;
    }

    /**
     * Set the amount of towers owned by this player
     * @param tower amount of towers
     */
    public void setTowers(int tower) {
        this._towerNum = tower;
    }

    /**
     * Sets the last used assistant to the given one.
     * This method is used for test purposes only.
     * @param value value of the assistant to be set
     */
    protected void setLastUsedAssistant(int value) {
        this._lastUsedAssistant = this._assistants.remove(value-1);
    }

    /**
     *
     * @return the Player of the team that controls the towers
     */
    public Player getCaptain() {
        return this.captain;
    }
}
