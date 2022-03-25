package it.polimi.ingsw;

import java.util.HashMap;

public class Game {

    /**
     * @param motherNature index of the island with mother nature.
     */
    Game(int motherNature) {
        initializeIslands();
        _bag = new Bag();
        _spareCoins = 20;
        _motherNature = motherNature;
        _numIslands = 12;

    }
    /** Initialize 12 empty islands.
     */
    private void initializeIslands() {
        islands = new HashMap<>();
        for(int i = 0; i < 12; i++) {
            islands.put(i, new Island());
        }
    }


    /** In the case that x == numIslands - 1(ex. x = 11, y = 0), use the yth island to merge the xth island, just like deleting the tail node of a linked list.
     * @param x the index of one of the islands to be merged.
     * @param y the index of one of the islands to be merged.
     */
    private void mergeIslands(int x, int y) {
        if (x == _numIslands - 1) {
            islands.get(y).copyFrom(x);
            if (x == _motherNature) {
                _motherNature = y;
            }
        }
        else {
            islands.get(x).copyFrom(y);
            if (y == _motherNature) {
                _motherNature = x;
            }
            for (int i = y; i < _numIslands - 1; i++) {
                islands.put(i, islands.get(i + 1)); // move islands after the yth forward by 1.
            }
        }
        islands.remove(_numIslands--);
    }

    /**
     * @return number of coins not obtained by any player.*/
    private int getSpareCoins() {
        return this._spareCoins;
    }

    /**
     * @return the island with mothernature.*/
    private Island getMotherNature() {
        return islands.get(_motherNature);
    }

    /**
     * Move mothernature to xth island.*/
    private void moveMotherNature(int x) {
        if (_motherNature == x || x > _numIslands - 1) {
            throw new GameException("Illegal movement.");
        } else {
            _motherNature = x;
        }
    }

    /**
     * @return the current player.*/
    private Player getCurrentPlayer() {
        return this._currentPlayer;
    }

    /**
     * Set current player to PLAYER.*/
    private void setCurrentPlayer(Player player) {
        try {
            _currentPlayer = player;
        } catch (IndexOutOfBoundsException) {
            //TODO
        }
    }

    /**
     * @return the winner of current game.*/
    private Player getWinner() {
        return this._winner;
    }

    /** @Return true iff it would currently be legal for PLAYER to move.*/
    boolean isLegal(Player player) {
        if (!_currentPlayer.equals(player)) {
            return true;
        }

        return false;
    }

    /** All islands.*/
    private HashMap<Integer, Island> islands;

    /** Coins not obtained by any player. Initially set to 20.*/
    private int _spareCoins;

    /** the index of the island with mothernature.*/
    private int _motherNature;

    /** current player. */
    private Player _currentPlayer;

    /** the winner of current game.*/
    private Player _winner;

    /** number of islands. (merged islands count as 1) */
    private int _numIslands;

    /** students bag*/
    private Bag _bag;
}
