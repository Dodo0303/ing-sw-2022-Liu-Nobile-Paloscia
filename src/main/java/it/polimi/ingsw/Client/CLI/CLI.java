package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Model.Wizard;
import it.polimi.ingsw.Network.Messages.toClient.ActionPhase.ChangeTurnMessage;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.ConfirmJoiningMessage;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.NickResponseMessage;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.SendMatchesMessage;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;
import it.polimi.ingsw.Network.Messages.toClient.Uncategorized.DisconnectMessage;
import it.polimi.ingsw.Network.Messages.toClient.Uncategorized.ResetOutputMessage;
import it.polimi.ingsw.Network.Messages.toClient.Uncategorized.StatusMessage;
import it.polimi.ingsw.Network.Messages.toServer.JoiningPhase.CreateMatchMessage;
import it.polimi.ingsw.Network.Messages.toServer.JoiningPhase.SendNickMessage;
import it.polimi.ingsw.Network.Messages.toServer.JoiningPhase.SendStartInfoMessage;

import java.io.IOException;
import java.util.Scanner;

public class CLI {
    private String nickname;
    private String host;
    private int port;
    private Scanner input = new Scanner(System.in);
    private ServerHandler serverHandler;
    private Phase currPhase;


    public void start() {
        currPhase = Phase.BuildingConnection;
        buildConnection();
        Thread serverHandlerThread  = new Thread(this.serverHandler);
        serverHandlerThread.start();
        requireNickname();
        chooseGameMode();
    }

    public void disconnect() {
        serverHandler.send(new DisconnectMessage());
    }

    public void send(Object message) {
        if (message == null) {
            throw new IllegalArgumentException("Null cannot be sent as a message.\n");
        } else if (serverHandler.closed) {
            throw new IllegalStateException("You are off-line!\n");
        } else {
            serverHandler.send(message);
        }
    }

    public void resetOutput() {
        serverHandler.send(new ResetOutputMessage());
    }

    protected void messageReceived(Object message) {
        if (message instanceof NickResponseMessage) {
            if (currPhase.equals(Phase.PickingNickname)) {
                ((NickResponseMessage) message).process(this.serverHandler);
                currPhase = Phase.ChoosingGameMode;
            } else {
                serverHandler.getOutgoingMessages().add(message);
            }
        } else if(message instanceof SendMatchesMessage) {
            if (currPhase.equals(Phase.ChoosingGameMode)) {
                ((SendMatchesMessage) message).process(this.serverHandler);
                currPhase = Phase.JoiningGame1;
            } else {
                serverHandler.getOutgoingMessages().add(message);
            }
        } else if(message instanceof ConfirmJoiningMessage) {
            if (currPhase.equals(Phase.ChoosingGameMode)) {
                ((ConfirmJoiningMessage) message).process(this.serverHandler);
                currPhase = Phase.GameCreated;
            } else if (currPhase.equals(Phase.JoiningGame2)) {
                ((ConfirmJoiningMessage) message).process(this.serverHandler);
                currPhase = Phase.GameJoined;
            } else {
                serverHandler.getOutgoingMessages().add(message);
            }
        }
    }

    public void requireNickname() {
        System.out.print("Nickname?");
        String temp = input.nextLine();
        send(new SendNickMessage(temp));
    }


    private void buildConnection() {
        while(serverHandler == null) {
            System.out.print("Host?\n");
            host = input.nextLine();
            System.out.print("Port?\n");
            port = Integer.parseInt(input.nextLine());
            try {
                serverHandler = new ServerHandler(host,port, this);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        currPhase = Phase.PickingNickname;
    }

    private void chooseGameMode() {
        int temp = 0;
        while(temp != 1 && temp != 2) {
            System.out.print("Press 1 for new game, press 2 for joining exiting game.\n");
            temp = Integer.parseInt(input.nextLine());
        }
        if (temp == 1) {
            send(new CreateMatchMessage(true));
            newgame();
        } else {
            send(new CreateMatchMessage(false));
            joinGame();
        }
    }

    private void newgame() {
        int numPlayer = 0, wiz = 0;
        String mode = "";
        while(numPlayer > 4 || numPlayer < 2) {
            System.out.print("choose between 2,3, or 4 for total number of players.\n");
            numPlayer = Integer.parseInt(input.nextLine());
        }
        while(!mode.equals("true") && !mode.equals("false")) {
            System.out.print("turn on expert mode? Respond with true or false.\n");
            mode = input.nextLine();
        }
        while(wiz > 4 || wiz < 1) {
            System.out.print("Choose a wizard for yourself. Type in 1,2,3, or 4.\n");
            wiz = Integer.parseInt(input.nextLine());
        }
        send(new SendStartInfoMessage(numPlayer, Boolean.parseBoolean(mode), Wizard.values()[Integer.parseInt(input.nextLine())]));
    }

    private void joinGame() {

    }

    public String getNickname() {
        return nickname;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Scanner getInput() {
        return input;
    }

    public ServerHandler getServerHandler() {
        return serverHandler;
    }

    public void setNickName(String str) {
        this.nickname = str;
    }

    public void setPhase(Phase phase) {
        this.currPhase = phase;
    }
}
