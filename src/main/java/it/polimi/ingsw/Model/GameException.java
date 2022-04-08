package it.polimi.ingsw.Model;

public class GameException extends RuntimeException{

    /** A GameException with no message. */
    GameException() {
    }

    /** A GameException for which .getMessage() is MSG. */
    GameException(String msg) {
        super(msg);
    }

    static GameException error(String format, Object... args) {
        return new GameException(String.format(format, args));
    }
}
