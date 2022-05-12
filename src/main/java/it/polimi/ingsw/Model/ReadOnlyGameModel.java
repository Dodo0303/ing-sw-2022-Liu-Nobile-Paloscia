package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ReadOnlyGameModel extends GameModel {

    /** A new ReadOnlyGameModel that allows a read-only view of game. That is,
     *  all operations are delegated to game. */
    ReadOnlyGameModel(GameModel game) {
        _gameModel = game;
    }

    @Override
    public void setMothernature(int x){}

    @Override
    public int setNumIslands(int x) {return 0;}

    @Override
    void setEntranceStudents(int numOfPlayers){}

    /** GameModel to which all operations are delegated. */
    private GameModel _gameModel;
}
