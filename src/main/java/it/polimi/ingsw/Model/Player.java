package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.EmptyTableException;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Exceptions.GameException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**  A new player. */

public class Player implements Serializable {

    /** My current color. */
    private Color _color;

    /** Number of towers owned by THIS PLAYER. */
    private int _towerNum;

    /** Maximum amount of towers that the player can hold */
    private final int _maxTowerNum;

    /** Number of coins owned by THIS PLAYER. */
    private int _coins;

    /** The nickname set by THIS PLAYER */
    private String _nickName;

    /** The numbers of professors of each color. */
    private List<StudentColor> _professors;

    /** The numbers of students in entrance of each color. */
    private HashMap<StudentColor, Integer> _entranceStudents; //TODO Hashmap doesn't allow us to know exactly the position of the students in the entrance

    /** Max amount of students in the entrance */
    private final int _maxEntranceStudents;

    /** Assistant cards of THIS player. Assistant[] is being used because the index of each card represents their maxStep.*/
    private final ArrayList<Assistant> _assistants = new ArrayList<>(10);

    /** Last used assistant. */
    private Assistant _lastUsedAssistant;

    /** Dining tables of THIS player. */
    private HashMap<StudentColor, DiningTable> _diningTable;



    // CONSTRUCTOR

    /** A Player in GAME, initially playing COLOR. */
    Player(String nickname, Color color, Wizard wizard, int numOfPlayers) {
        this._nickName = nickname;
        this._color = color;
        if ((numOfPlayers == 2 || numOfPlayers == 4) && (color != Color.GRAY && color != Color.VOID)) {
            this._towerNum = 0; //0 towers because in the case of 4 players, one player of the team has 8 towers while the other player has 0 towers.
            this._maxTowerNum = 8;
            this._maxEntranceStudents = 7;
        } else if (numOfPlayers == 3 && color != Color.VOID) {
            this._towerNum = 6;
            this._maxTowerNum = 6;
            this._maxEntranceStudents = 9;
        } else {
            throw new IllegalArgumentException();
        }
        initEntranceStudents();
        initProfessors();
        initAssistant(wizard);
        initDiningTable();
    }



    // INITIALIZATION

    /** Initialize _entranceStudents */
    private void initEntranceStudents() {
        this._entranceStudents = new HashMap<>();
        for(StudentColor color : StudentColor.values()) {
            this._entranceStudents.put(color, 0);
        }
    }
    /** Initialize _professors */
    private void initProfessors() {
        this._professors = new ArrayList<>();
    }

    /** Initialize _assistant */
    private void initAssistant(Wizard wizard) {
        int index = 0;
        for (int maxSteps = 1; maxSteps <= 5; maxSteps++) {
            for (int j = 0; j < 2; j++) {
                this._assistants.add(new Assistant(++index, maxSteps, wizard));
            }
        }
    }

    /** Initialize _diningTable */
    private void initDiningTable() {
        this._diningTable = new HashMap<>();
        for(StudentColor color : StudentColor.values()) {
            this._diningTable.put(color, new DiningTable(color));
        }
    }



    // ADD AND REMOVE TOWERS

    /** Add 1 tower to THIS PLAYER. */
    void addTower() {
        if (_towerNum < _maxTowerNum)
            this._towerNum++;
        else
            throw new GameException("Too many towers");
    }

    /** Remove 1 tower from THIS PLAYER. */
    void removeTower() {
        if (this._towerNum > 0) {
            this._towerNum--;
        } else {
            throw new GameException("Invalid operation.");
        }
    }



    // ADD AND REMOVE COINS

    /** Add 1 coin to THIS PLAYER. */
    void addCoin() {
        this._coins++;
    }

    /** Remove x coins from THIS PLAYER. */
    void removeCoins(int x) {
        if (x < 0)
            throw new GameException("Can't remove a negative number of coins");
        if (_coins >= x)
            this._coins -= x;
        else
            throw new GameException("Not enough coins");
    }



    // ADD AND REMOVE PROFESSORS

    /** 1 professor of StudentCOLOR color to THIS PLAYER. */
    public void addProfessor(StudentColor color) {
        if (!this._professors.contains(color))
            this._professors.add(color);
    }

    /** Remove 1 professor of StudentCOLOR color from THIS PLAYER. */
    public void removeProfessor(StudentColor color) {
            this._professors.remove(color);
    }



    // ADD AND REMOVE STUDENTS FROM ENTRANCE

    /** Add one student of StudentCOLOR color to THIS PLAYER. */
    public void addStudentToEntrance(StudentColor color) {
        int num = 0;
        for(StudentColor student : StudentColor.values()) {
            num += this._entranceStudents.get(student);
            if (num >= _maxEntranceStudents) {
                throw new GameException("Entrance is full.");
            }
        }
        this._entranceStudents.put(color,  this._entranceStudents.get(color) + 1);
    }

    /** Remove one student of StudentCOLOR color from THIS PLAYER. */
    public void removeStudentFromEntrance(StudentColor color) {
        if (this._entranceStudents.get(color) > 0) {
            this._entranceStudents.put(color,  this._entranceStudents.get(color) - 1);
        } else {
            throw new GameException("No student of this color left.");
        }
    }

    /** Remove all students in entrance. */
    public void clearEntrance() {
        for (StudentColor color : _entranceStudents.keySet()) {
            _entranceStudents.put(color, 0);
        }
    }



    // ASSISTANT

    /** Use an assistant card.
     * @return the assistant card chosen by THIS player. */
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

    /** Add student to the correspondent dining table. */
    public void addToDiningTable(StudentColor color) throws FullTableException {
        this._diningTable.get(color).addStudent();
    }

    /** Remove a student from the correspondent dining table. */
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

    public boolean hasProfessor(StudentColor color) {
        return this._professors.contains(color);
    }



    // GETTERS

    /** Return the color I am currently playing. */
    public Color getColor() {
        return this._color;
    }

    /**The getter of _towerNum
     * @return number of towers owned by THIS PLAYER. */
    public int getTowerNum() {
        return this._towerNum;
    }

    /** The getter of coins.
     * @return number of coins owned by THIS PLAYER.*/
    public int getCoins() {
        return this._coins;
    }

    /** The getter of nickname.
     * @return the nickname of THIS PLAYER. */
    public String getNickName() {
        return this._nickName;
    }

    /** The getter of _professors
     * @return the numbers of professors of each color. */
    public List<StudentColor> getProfessors() {
        return new ArrayList<>(this._professors);
    }

    /** Set professors. */
    public void setProfessors(StudentColor color) {
        this._professors.add(color);
    }

    /** The getter of _entranceStudents
     * @return the numbers of students in entrance of each color.*/
    public HashMap<StudentColor, Integer> getEntranceStudents() {
        return this._entranceStudents;
    }

    /** The getter of _assistant.
     * @return unused assistants of THIS player. */
    public ArrayList<Assistant> getAssistants() {
        return this._assistants;
    }

    /** The getter of _usedAssistant.
     * @return last used assistant */
    public Assistant getUsedAssistant() {
        return this._lastUsedAssistant;
    }

    /** The getter of DiningTables.
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

    public boolean lastAssistant() {
        return this._assistants.size() == 1;
    }
}
