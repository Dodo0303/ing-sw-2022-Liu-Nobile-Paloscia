package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** This COULD BE an abstract class in order to accommodate different game modes.
 * ALL methods implemented here is based on TWO-PERSON games, future adjustment/override is advised. */

public abstract class Player {
    //TODO
    //TODO
    //TODO

    /** A Player in GAME, initially playing COLOR. */
    Player(Color color, Wizard wizard) {
        _color = color;
        _towerNum = 8;
        initEntranceStudents();
        initProfessors();
    }

    /** Initialize _entranceStudents */
    private void initEntranceStudents() {
        _entranceStudents = new HashMap<>();
        for(StudentColor color : StudentColor.values()) {
            _entranceStudents.put(color, 0);
        }
    }
    /** Initialize _professors */
    private void initProfessors() {
        _professors = new HashMap<>();
        for(StudentColor color : StudentColor.values()) {
            _professors.put(color, 0);
        }
    }

    /** Return the color I am currently playing. */
    Color getColor() {
        return _color;
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

}
