package it.polimi.ingsw.Exceptions;

public class WrongEffectException extends Exception{

    public WrongEffectException() {
        super();
    }

    public WrongEffectException(String error) {
        super(error);
    }
}
