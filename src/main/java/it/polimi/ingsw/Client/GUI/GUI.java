package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.Wizard;

import java.util.List;
import java.util.Scanner;

public class GUI {
    GameModel game;
    boolean closed;
    private String nickname;
    private String host;
    private int port;
    private Scanner input = new Scanner(System.in);
    private ServerHandler serverHandler;
    private Phase currPhase;
    private List<Wizard> wizards;
    private int ap1Moves;

    public void start() {
        ap1Moves = 0;
        closed = false;
        currPhase = Phase.BuildingConnection;
        buildConnection();
        Thread serverHandlerThread  = new Thread(this.serverHandler);
        serverHandlerThread.start();
        requireNickname();
        chooseGameMode();
    }

    public void send(Object message) {
        if (message == null) {
            throw new IllegalArgumentException("Null cannot be sent as a message.\n");
        } else {
            serverHandler.send(message);
        }
    }

    private void buildConnection() {
    }

    private void requireNickname() {
    }

    private void chooseGameMode() {
    }


    public void messageReceived(Object message) {
    }

    public void setNickName(String str) {
    }
}
