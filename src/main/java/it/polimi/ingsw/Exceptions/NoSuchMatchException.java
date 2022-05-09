package it.polimi.ingsw.Exceptions;

public class NoSuchMatchException extends Exception{
    public NoSuchMatchException() {
    }

    public NoSuchMatchException(String message) {
        super(message);
    }
}
