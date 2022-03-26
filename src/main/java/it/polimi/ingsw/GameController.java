package it.polimi.ingsw;

/** The Eriantys game. FOR NOW, ONLY 2-PERSON GAME IS IMPLEMENTED.
 */

public class GameController {
    //TODO
    //TODO
    //TODO

    GameController(int numPlayers) {
        _board = new Game();
        _numPlayers = numPlayers;
    }

    /** Return true iff the current game is not over. */
    boolean gameInProgress() {
        return _board.getWinner() == null;
    }

    /** Play a session of Eriantis.
     */
    int play() {
        boolean winnerAnnounced = false;
        _exit = -1;
        while(_exit < 0) {
            String cmd;
            if (_board.getWinner() == null) {
                winnerAnnounced = false;
                try {
                    //executeCommand();
                } catch (GameException excp) {
                    throw excp;
                }
            } else if (!winnerAnnounced){
                //TODO
            }
        }

        return _exit;
    }

    private void executeCommand(String cmnd) {

    }

    /** The board on which I record all moves. */
    private final Game _board;

    /** The number of players. */
    private final int _numPlayers;

    /** When set to 0, indicates that play should terminate.
     * When _exit < 0, the session is not over. */
    private int _exit;
}
