package it.polimi.ingsw.Model;

public class WrongEffectException extends Exception{

    public WrongEffectException() {
        super();
    }

    public WrongEffectException(String error) {
        super(error);
    }
}
