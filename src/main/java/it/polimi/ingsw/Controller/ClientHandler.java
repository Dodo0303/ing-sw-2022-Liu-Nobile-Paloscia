package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.Wizard;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;
import it.polimi.ingsw.Network.Messages.toServer.JoiningPhase.CreateMatchMessage;
import it.polimi.ingsw.Network.Messages.toServer.JoiningPhase.SendNickMessage;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
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

        try {
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            //At this point the client handler must receive the nickname from the client
            MessageToServer nickMessage = (MessageToServer) objectInputStream.readObject();
            while (!(nickMessage instanceof SendNickMessage) || !server.isNicknameAvailable(((SendNickMessage) nickMessage).getNickname())) {
                //TODO sendRefuseNickname
                nickMessage = (MessageToServer) objectInputStream.readObject();
            }
            //Received nickname
            nickname = ((SendNickMessage) nickMessage).getNickname();

            //Now the handler must receive CreateMatchMessage
            MessageToServer matchMessage = (MessageToServer) objectInputStream.readObject();
            while (!(matchMessage instanceof CreateMatchMessage)) {
                System.out.println("Expected CreateMatchMessage, received " + matchMessage.getClass());
                matchMessage = (MessageToServer) objectInputStream.readObject();
            }
            if (((CreateMatchMessage)matchMessage).getNewMatch()) {
                //TODO Create a new match
            } else {
                //TODO Join a match
            }

            //Here the game starts. The handler must listen for incoming messages
            while(true) {
                MessageToServer message = (MessageToServer) objectInputStream.readObject();
                Thread messageHandler = new Thread(() -> match.process(message, this));
                messageHandler.start();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }




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
