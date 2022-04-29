package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static it.polimi.ingsw.Model.GameException.error;

/**  A new player. */

public class Player {
    /** A Player in GAME, initially playing COLOR. */
    Player(Color color, Wizard wizard, int numOfPlayers) {
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
            for(int j = 0; j < 2; j++) {
                _assistant[index] = new Assistant(++index, maxSteps, wizard);
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

    /** 1 professor of StudentCOLOR color to THIS PLAYER. */
    void addProfessor(StudentColor color) {
        if (!this._professors.contains(color))
            this._professors.add(color);
    }

    /** Remove 1 professor of StudentCOLOR color from THIS PLAYER. */
    void removeProfessor(StudentColor color) {
            this._professors.remove(color);
    }

    /** Add one student of StudentCOLOR color to THIS PLAYER. */
    void addStudentToEntrance(StudentColor color) {
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
    void removeStudentFromEntrance(StudentColor color) {
        if (this._entranceStudents.get(color) > 0) {
            this._entranceStudents.put(color,  this._entranceStudents.get(color) - 1);
        } else {
            throw new GameException("No student of this color left.");
        }
    }

    /** Use an assistant card.
     * @return the assistant card chosen by THIS player. */
    Assistant useAssistant(Assistant assistant) {
        int index = assistant.getValue()-1;
        Assistant res = _assistant[index];
        if (res == null)
            throw new GameException("Assistant already used");
        _lastUsedAssistant = res;
        _assistant[index] = null;
        return res;
    }

    /** Add student to the correspondent dining table. */
    void addToDiningTable(StudentColor color) throws FullTableException {
        this._diningTable.get(color).addStudent();
    }

    /** Remove a student from the correspondent dining table. */
    void removeFromDiningTable(StudentColor color) throws EmptyTableException {
        this._diningTable.get(color).removeStudent();
    }

    /** Remove all students in entrance. */
    void clearEntrance() {
        for(StudentColor color : _entranceStudents.keySet()) {
            _entranceStudents.put(color, 0);
        }
    }


    /** Return the color I am currently playing. */
    public Color getColor() {
        return this._color;
    }

    /**The getter of _towerNum
     * @return number of towers owned by THIS PLAYER. */
    int getTowerNum() {
        return this._towerNum;
    }

    /** The getter of coins.
     * @return number of coins owned by THIS PLAYER.*/
    int getCoins() {
        return this._coins;
    }

    public void setNickName(String nickname){
        if (_nickName == null)
            _nickName = nickname;
    }

    /** The getter of nickname.
     * @return the nickname of THIS PLAYER. */
    String getNickName() {
        return this._nickName;
    }


    /** The getter of _professors
     * @return the numbers of professors of each color. */
    public List<StudentColor> getProfessors() {
        return new ArrayList<>(this._professors);
    }

    /** Set professors. */
    void setProfessors(StudentColor color) {
        this._professors.add(color);
    }

    /** The getter of _entranceStudents
     * @return the numbers of students in entrance of each color.*/
    HashMap<StudentColor, Integer> getEntranceStudents() {
        return this._entranceStudents;
    }

    /** The getter of _assistant.
     * @return unused assistants of THIS player. */
    Assistant[] getAssistants() {
        return this._assistant;
    }

    /** The getter of _usedAssistant.
     * @return last used assistant */
    Assistant getUsedAssistant() {
        return this._lastUsedAssistant;
    }


    /** The getter of DiningTables.
    * @return the dining tables. */
    HashMap<StudentColor, DiningTable> getDiningTables() {
        return new HashMap<>(this._diningTable);
    }

    /**
     *
     * @return the maximum amount of students that can be in the entrance
     */
    public int getMaxEntranceStudents() {
        return _maxEntranceStudents;
    }

    /**
     *
     * @return the maximum amount of towers that this player can have
     */
    public int getMaxTowerNum(){
        return _maxTowerNum;
    }

    /** My current color. */
    private Color _color;

    /** number of towers owned by THIS PLAYER. */
    private int _towerNum;

    /** maximum amount of towers that the player can hold */
    private int _maxTowerNum;

    /** number of coins owned by THIS PLAYER. */
    private int _coins;

    /** The nickname set by THIS PLAYER */
    private String _nickName;

    /** The numbers of professors of each color. */
    private List<StudentColor> _professors;

    /** The numbers of students in entrance of each color. */
    private HashMap<StudentColor, Integer> _entranceStudents; //TODO Hashmap doesn't allow us to know exactly the position of the students in the entrance

    /** Max amount of students in the entrance */
    private int _maxEntranceStudents;

    /** Assistant cards of THIS player. Assistant[] is being used because the index of each card represents their maxStep.*/
    private Assistant[] _assistant = new Assistant[10];

    /** Last used assistant. */
    private Assistant _lastUsedAssistant;

    /** Dining tables of THIS player. */
    private HashMap<StudentColor, DiningTable> _diningTable;

}
