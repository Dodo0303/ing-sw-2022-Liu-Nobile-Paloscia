package it.polimi.ingsw.Client;

public interface ViewController {
    void messageReceived(Object message) throws Exception;

    void gameOver(String msg);
}
