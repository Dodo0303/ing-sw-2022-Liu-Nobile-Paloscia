package it.polimi.ingsw.Exceptions;

public class GameException extends RuntimeException{

    /** A GameException with no message. */
    public GameException() {
    }

    /** A GameException for which .getMessage() is MSG. */
    public GameException(String msg) {
        super(msg);
    }

    public static GameException error(String format, Object... args) {
        return new GameException(String.format(format, args));
    }
}
