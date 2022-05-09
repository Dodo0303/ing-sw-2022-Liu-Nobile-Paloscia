package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.Wizard;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

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


    @Override
    public void run() {

    }

    public boolean wizardAvailable() {
        //TODO
        return true;
    }

    public void send(MessageToClient msg) {

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
