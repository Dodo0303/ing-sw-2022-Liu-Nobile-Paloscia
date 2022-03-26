package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** This is an abstract class in order to accommodate different game modes.
 * ALL methods implemented here is based on TWO-PERSON games, future adjustment/override is advised. */

public abstract class Player {
    //TODO
    //TODO
    //TODO
    
    /** A Player in GAME, initially playing COLOR. */
    Player(Color color) {
        _color = color;
        initEntranceStudents();
        initProfessors();
    }

    /***/
    void initEntranceStudents() {
        _entranceStudents = new HashMap<>();
        for(StudentColor color : StudentColor.values()) {
            _entranceStudents.put(color, 0);
        }
    }

    void initProfessors() {
        _professors = new HashMap<>();
        for(StudentColor color : StudentColor.values()) {
            _professors.put(color, 0);
        }
    }

    /** Return the color I am currently playing. */
    Color getColor() {
        return _color;
    }

    /***/
    int getCoins() {
        return this._coins;
    }

    /***/
    void addCoins() {
        this._coins++;
    }

    /***/
    void removeCoins(int x) {
        this._coins -= x;
    }

    /***/
    String getNickName() {
        return this._nickName;
    }

    /***/
    void addStudentToEntrance(StudentColor color) {
        this._entranceStudents.put(color,  this._entranceStudents.get(color) + 1);
    }

    /***/
    void removeStudentToEntrance(StudentColor color) {
        if (this._entranceStudents.get(color) > 0) {
            this._entranceStudents.put(color,  this._entranceStudents.get(color) - 1);
        } else {
            throw new GameException("Invalid operation.");
        }
    }

    /***/
    void addProfessor(StudentColor color) {
        this._professors.put(color,  this._professors.get(color) + 1);
    }

    /***/
    void removeProfessor(StudentColor color) {
        if (this._professors.get(color) > 0) {
            this._professors.put(color,  this._professors.get(color) - 1);
        } else {
            throw new GameException("Invalid operation.");
        }
    }

    /***/
    int getTowerNum() {
        return this._towerNum;
    }

    /***/
    void addTower() {
        this._towerNum++;
    }

    /***/
    void removeTower() {
        if (this._towerNum > 0) {
            this._towerNum--;
        } else {
            throw new GameException("Invalid operation.");
        }
    }

    /***/
    HashMap<StudentColor, Integer> getEntranceStudents() {
        return this._entranceStudents;
    }

    /***/
    HashMap<StudentColor, Integer> getProfessors() {
        return this._professors;
    }

    /** My current color. */
    private Color _color;

    /***/
    private int _towerNum;

    /***/
    private int _coins;

    /***/
    private String _nickName;

    /***/
    private HashMap<StudentColor, Integer> _professors;

    /***/
    private HashMap<StudentColor, Integer> _entranceStudents;

}
