package it.polimi.ingsw;

public class GameException extends RuntimeException{

    /** A GameException with no message. */
    GameException() {
    }

    /** A GameException for which .getMessage() is MSG. */
    GameException(String msg) {
        super(msg);
    }
}
