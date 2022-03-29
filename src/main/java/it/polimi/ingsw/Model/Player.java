package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.HashMap;

/** This COULD BE an abstract class in order to accommodate different game modes.
 * ALL methods implemented here is based on TWO-PERSON games, future adjustment/override is advised. */

public class Player {
    //TODO
    //TODO
    //TODO

    /** A Player in GAME, initially playing COLOR. */
    Player(Color color, Wizard wizard, int towerNum) {
        this._color = color;
        this._towerNum = towerNum;
        initEntranceStudents();
        initProfessors();
        initAssistant(wizard);
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
        int step = 1; //TODO
        for (int i = 1; i <= 5; i++) {
            for(int j = 0; j < 2; j++) {
                _assistant[step] = new Assistant(i, step++, wizard);
            }
        }
    }

    /** Use an assistant card.
     * @return the assistant card chosen by THIS player. */
    Assistant useAssistant(Assistant assistant) {
        int index = assistant.getMaxSteps();
        Assistant res = this._assistant[index];
        _assistant[index] = null;
        addUsedAssistant(assistant);
        return res;
    }


    /** Add the assistant card to _usedAssistant */
    void addUsedAssistant(Assistant assistant) {
        _usedAssistant.add(assistant);
    }

    /** Return the Assistant card most recently used by THIS player. */
    Assistant getAssistant() {
        return _usedAssistant.get(_usedAssistant.size() - 1);
    }

    /** Return the color I am currently playing. */
    Color getColor() {
        return this._color;
    }

    /** The getter of coins.
     * @return number of coins owned by THIS PLAYER.*/
    int getCoins() {
        return this._coins;
    }

    /** Add 1 coin to THIS PLAYER. */
    void addCoins() {
        this._coins++;
    }

    /** Remove 1 coin from THIS PLAYER. */
    void removeCoins(int x) {
        this._coins -= x;
    }

    /** The getter of nickname.
     * @return the nickname of THIS PLAYER. */
    String getNickName() {
        return this._nickName;
    }

    /** Add one student of StudentCOLOR color to THIS PLAYER. */
    void addStudentToEntrance(StudentColor color) {
        this._entranceStudents.put(color,  this._entranceStudents.get(color) + 1);
    }

    /** Remove one student of StudentCOLOR color from THIS PLAYER. */
    void removeStudentToEntrance(StudentColor color) {
        if (this._entranceStudents.get(color) > 0) {
            this._entranceStudents.put(color,  this._entranceStudents.get(color) - 1);
        } else {
            throw new GameException("Invalid operation.");
        }
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
            throw new GameException("Invalid operation.");
        }
    }

    /**The getter of _towerNum
     * @return number of towers owned by THIS PLAYER. */
    int getTowerNum() {
        return this._towerNum;
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

    /** The getter of _entranceStudents
     * @return the numbers of students in entrance of each color.*/
    HashMap<StudentColor, Integer> getEntranceStudents() {
        return this._entranceStudents;
    }

    /** The getter of _professors
     * @return the numbers of professors of each color. */
    HashMap<StudentColor, Integer> getProfessors() {
        return this._professors;
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

    /** Assistant cards of THIS player*/
    private Assistant[] _assistant;

    /** Used assistant cards of THIS player */
    private ArrayList<Assistant> _usedAssistant;

}
