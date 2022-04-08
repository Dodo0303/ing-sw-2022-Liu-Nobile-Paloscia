package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.HashMap;

import static it.polimi.ingsw.Model.GameException.error;

/**  A new player. */

public class Player {
    /** A Player in GAME, initially playing COLOR. */
    Player(Color color, Wizard wizard, int towerNum) {
        this._color = color;
        this._towerNum = towerNum;
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
        this._professors = new HashMap<>();
        for(StudentColor color : StudentColor.values()) {
            this._professors.put(color, 0);
        }
    }

    /** Initialize _assistant */
    private void initAssistant(Wizard wizard) {
        int step = 1;
        for (int i = 1; i <= 5; i++) {
            for(int j = 0; j < 2; j++) {
                _assistant[step] = new Assistant(i, step++, wizard);
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
        this._towerNum++;
    }

    /** Remove 1 coin from THIS PLAYER. */
    void removeTower() {
        if (this._towerNum > 0) {
            this._towerNum--;
        } else {
            throw new GameException("Invalid operation.");
        }
    }

    /** Add 1 coin to THIS PLAYER. */
    void addCoins() {
        this._coins++;
    }

    /** Remove 1 coin from THIS PLAYER. */
    void removeCoins(int x) {
        this._coins -= x;
    }

    /** 1 professor of StudentCOLOR color to THIS PLAYER. */
    void addProfessor(StudentColor color) {
        this._professors.put(color,  this._professors.get(color) + 1);
    }

    /** Remove 1 professor of StudentCOLOR color from THIS PLAYER. */
    void removeProfessor(StudentColor color) {
        if (this._professors.get(color) > 0) {
            this._professors.put(color,  this._professors.get(color) - 1);
        } else {
            throw error("Invalid operation.");
        }
    }

    /** Add one student of StudentCOLOR color to THIS PLAYER. */
    void addStudentToEntrance(StudentColor color) {
        int num = 0;
        for(StudentColor student : StudentColor.values()) {
            num += this._entranceStudents.get(student);
            if (num > 7) {
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
            throw new GameException("Invalid operation.");
        }
    }

    /** Use an assistant card.
     * @return the assistant card chosen by THIS player. */
    Assistant useAssistant(Assistant assistant) {
        int index = assistant.getMaxSteps();
        Assistant res = this._assistant[index];
        _assistant[index] = null;
        this.addUsedAssistant(assistant);
        return res;
    }

    /** Add the assistant card to _usedAssistant. */
    private void addUsedAssistant(Assistant assistant) {
        _usedAssistant.add(assistant);
    }

    /** Add student to the correspondent dining table. */
    void addToDiningtable(StudentColor student) throws FullTableException {
        this._diningTable.get(student).addStudent();
    }

    /** Remove a student from the correspondent dining table. */
    void removeFromDiningtable(StudentColor student) throws EmptyTableException {
        this._diningTable.get(student).removeStudent();
    }


    /** Return the color I am currently playing. */
    Color getColor() {
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

    /** The getter of nickname.
     * @return the nickname of THIS PLAYER. */
    String getNickName() {
        return this._nickName;
    }

    /** The getter of professors.
     * @return professors of THIS player. */
    HashMap<StudentColor, Integer> getprofessors() {
        return this._professors;
    }

    /** The getter of _professors
     * @return the numbers of professors of each color. */
    HashMap<StudentColor, Integer> getProfessors() {
        return this._professors;
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
     * @return unused assistants of THIS player. */
    ArrayList<Assistant> getUsedAssistants() {
        return this._usedAssistant;
    }

    /** Return the Assistant card most recently used by THIS player. */
    Assistant getMostRecentAssistant() {
        return _usedAssistant.get(_usedAssistant.size() - 1);
    }

    /** The getter of DiningTables.
    * @return the dining tables. */
    HashMap<StudentColor, DiningTable> getDiningTable() {
        return _diningTable;
    }

    /** My current color. */
    private Color _color;

    /** number of towers owned by THIS PLAYER. */
    private int _towerNum;

    /** number of coins owned by THIS PLAYER. */
    private int _coins;

    /** The nickname set by THIS PLAYER */
    private String _nickName;

    /** The numbers of professors of each color. */
    private HashMap<StudentColor, Integer> _professors;

    /** The numbers of students in entrance of each color. */
    private HashMap<StudentColor, Integer> _entranceStudents;

    /** Assistant cards of THIS player. Assistant[] is being used because the index of each card represents their maxStep.*/
    private Assistant[] _assistant;

    /** Used assistant cards of THIS player. Arraylist is being used because _usedAssistant acts like a stack.*/
    private ArrayList<Assistant> _usedAssistant;

    /** Dining tables of THIS player. */
    private HashMap<StudentColor, DiningTable> _diningTable;

}
