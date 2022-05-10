package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.MatchMakingException;
import it.polimi.ingsw.Exceptions.NoSuchMatchException;
import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.Wizard;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.ConfirmJoiningMessage;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.NickResponseMessage;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.SendAvailableWizardsMessage;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.SendMatchesMessage;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;
import it.polimi.ingsw.Network.Messages.toServer.JoiningPhase.*;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        wizard = null;
    }


    @Override
    public void run() {

        try {
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            //At this point the client handler must receive the nickname from the client
            receiveNickname(objectInputStream);

            //Now the handler must receive CreateMatchMessage
            receiveCreateMatch(objectInputStream);

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

    private void receiveNickname(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        MessageToServer nickMessage = (MessageToServer) objectInputStream.readObject();
        while (!(nickMessage instanceof SendNickMessage) || !server.isNicknameAvailable(((SendNickMessage) nickMessage).getNickname())) {
            //Send the refuse
            MessageToClient msg = new NickResponseMessage(false);
            send(msg);
            //Read new nick proposal
            nickMessage = (MessageToServer) objectInputStream.readObject();
        }
        //Received valid nickname
        nickname = ((SendNickMessage) nickMessage).getNickname();
    }

    private void receiveCreateMatch(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        MessageToServer matchMessage = (MessageToServer) objectInputStream.readObject();
        while (!(matchMessage instanceof CreateMatchMessage)) {
            System.out.println("Expected CreateMatchMessage, received " + matchMessage.getClass());
            matchMessage = (MessageToServer) objectInputStream.readObject();
        }
        if (((CreateMatchMessage)matchMessage).getNewMatch()) {
            createNewMatch(objectInputStream);
        } else {
            joinMatch(objectInputStream);
        }
    }

    private void createNewMatch(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        MessageToServer infoMessage = (MessageToServer) objectInputStream.readObject();
        while (!(infoMessage instanceof SendStartInfoMessage)){
            System.out.println("Expected CreateMatchMessage, received " + infoMessage.getClass());
            infoMessage = (MessageToServer) objectInputStream.readObject();
        }
        //Received information
        match = new MatchController(new Random().nextInt(100000), ((SendStartInfoMessage) infoMessage).getNumOfPlayers()); //TODO Manage random better
        wizard = ((SendStartInfoMessage) infoMessage).getWizard();
        //TODO Manage game mode
        server.addMatch(match);
        //Send ConfirmJoiningMessage
        MessageToClient msg = new ConfirmJoiningMessage(true, "Game created");
        send(msg);

    }

    private void joinMatch(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        sendAvailableMatchesToClient(objectInputStream);

        receiveAndSetMatchChosen(objectInputStream);

        //Add the player to the match

        try {
            match.addPlayer(this);
        } catch (MatchMakingException e) {
            MessageToClient denyJoining = new ConfirmJoiningMessage(false, "Match full");
            send(denyJoining);
            joinMatch(objectInputStream);
            return;
        }

        //Choose the wizard
        receiveWizard(objectInputStream);
    }

    private void sendAvailableMatchesToClient(ObjectInputStream objectInputStream) throws IOException {
        List<MatchController> availableMatches = server.getMatchmakingMatches();
        List<Integer> matchesID = new ArrayList<>();
        List<List<String>> players = new ArrayList<>();
        List<String> matchPlayers = new ArrayList<>();

        for (MatchController matchController:
                availableMatches) {
            matchesID.add(matchController.getID());
            for (ClientHandler client :
                    matchController.getClients()) {
                matchPlayers.add(client.getNickname());
            }
            players.add(matchPlayers);
            matchPlayers.clear();
        }

        MessageToClient msg = new SendMatchesMessage(matchesID, players);
        send(msg);
    }

    private void receiveAndSetMatchChosen(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        MessageToServer matchChosen =(MessageToServer) objectInputStream.readObject();
        while (!(matchChosen instanceof MatchChosenMessage)){
            System.out.println("Expected MatchChosenMessage, received " + matchChosen.getClass());
            matchChosen = (MessageToServer) objectInputStream.readObject();
        }

        //Set the match attribute
        try {
            match = server.getMatchById(((MatchChosenMessage) matchChosen).getMatchID());
        } catch (NoSuchMatchException e) {
            MessageToClient denyJoining = new ConfirmJoiningMessage(false, "Match doesn't exists");
            send(denyJoining);
            joinMatch(objectInputStream);
        }
    }

    private void receiveWizard(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        sendAvailableWizards();

        MessageToServer wizardChosen = (MessageToServer) objectInputStream.readObject();
        while (!(wizardChosen instanceof SendChosenWizardMessage)){
            System.out.println("Expected CreateMatchMessage, received " + wizardChosen.getClass());
            wizardChosen = (MessageToServer) objectInputStream.readObject();
        }

        //Check whether the wizard is available and set the wizard
        if (match.isWizardAvailable(((SendChosenWizardMessage) wizardChosen).getWizard())){
            wizard = ((SendChosenWizardMessage) wizardChosen).getWizard();
            MessageToClient confirm = new ConfirmJoiningMessage(true, "You joined the game");
            send(confirm);
        } else {
            MessageToClient denyJoining = new ConfirmJoiningMessage(false, "Wizard not available");
            send(denyJoining);
            receiveWizard(objectInputStream);
        }
    }

    private void sendAvailableWizards() throws IOException {
        MessageToClient availableWizards = new SendAvailableWizardsMessage(match.getAvailableWizards());
        send(availableWizards);
    }

    public boolean wizardAvailable() {
        return wizard != null;
    }

    public void send(MessageToClient msg) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(msg);
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
