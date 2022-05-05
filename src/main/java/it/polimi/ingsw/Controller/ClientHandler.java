package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.Wizard;

import java.net.Socket;

//TODO

public class ClientHandler implements Runnable {
    private Socket socket;
    private EriantysServer server;
    private MatchController match;
    private int playerID;
    private String nickname;
    private Wizard wizard;

    public ClientHandler(EriantysServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

    }

    public boolean wizardAvailable() {
        //TODO
        return true;
    }

    public MatchController getMatch() {
        return this.match;
    }

    public Wizard getWizard() { return this.wizard; }

    public int getPlayerID() {
        return this.playerID;
    }

    public String getNickname() { return this.nickname; }
}
