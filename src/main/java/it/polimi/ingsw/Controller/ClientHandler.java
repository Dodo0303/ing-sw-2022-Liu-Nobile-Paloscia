package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Exceptions.MatchMakingException;
import it.polimi.ingsw.Exceptions.NoSuchMatchException;
import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.Wizard;
import it.polimi.ingsw.Network.Messages.ConnectionStatusMessage;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.ConfirmJoiningMessage;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.NickResponseMessage;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.SendAvailableWizardsMessage;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.SendMatchesMessage;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;
import it.polimi.ingsw.Network.Messages.toClient.Uncategorized.DisconnectMessage;
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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

//TODO

public class ClientHandler implements Runnable {
    private Socket socket;
    private EriantysServer server;
    private MatchController match;
    private int playerID;
    private String nickname;
    private Wizard wizard;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    private BlockingQueue<Object> incomingMessages;
    private LinkedBlockingQueue<Object> outgoingMessages;
    private Thread sendThread;
    private volatile Thread receiveThread; // Created only after connection is open.
    boolean closed;

    public ClientHandler(EriantysServer server, Socket socket, int playerID) throws IOException {
        this.server = server;
        this.socket = socket;
        this.playerID = playerID;
        wizard = null;
        closed = false;
        incomingMessages = new LinkedBlockingQueue<Object>();
        outgoingMessages = new LinkedBlockingQueue<Object>();
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        sendThread =  new SendThread();
    }

    @Override
    public void run() {
        try {
            sendThread.start();
            //At this point the client handler must receive the nickname from the client
            receiveNickname();
            //Now the handler must receive CreateMatchMessage
            receiveCreateMatch();
            //Here the game starts. The handler must listen for incoming messages
            while(true) {
                MessageToServer message = (MessageToServer) incomingMessages.take();
                Thread messageHandler = new Thread(() -> match.process(message, this));
                messageHandler.start();
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class SendThread extends Thread {
        public void run() {
            //setting up connection with client
            try {
                String handle = (String) objectInputStream.readObject();
                if (!"Hello Server.".equals(handle)) {
                    throw new IOException("Incorrect string received from client.\n");
                }
                objectOutputStream.writeObject(playerID);
                objectOutputStream.flush();
                receiveThread = new ReceiveThread();
                receiveThread.start();
            } catch (Exception e) {
                try {
                    closed = true;
                    socket.close();
                } catch (Exception ignored) {
                }
                System.out.println("Error while setting up connection: " + e + "\n");
                return;
            }
            //send message
            try {
                while(!closed) {
                    try {
                        Object msg = outgoingMessages.take();
                        objectOutputStream.writeObject(msg);
                    } catch (InterruptedException e) {
                        System.out.println("Error while sending message to client: " + e + "\n");
                        return;
                    }
                }
            } catch (IOException e) {
                if (!closed) {
                    closeWithError("Error while sending message to client: " + e + "\n");
                    System.out.println("Server sendThread terminated by IOException: " + e + "\n");
                }
            } catch (Exception e) {
                if (!closed) {
                    closeWithError("Internal Error: Unexpected exception in output thread: " + e + "\n");
                    System.out.println("Unexpected error shuts down server's sendThread:\n");
                    e.printStackTrace();
                }
            }
        }
    }

    private class ReceiveThread extends Thread {
        public void run() {
            while(!closed) {
                try {
                    Object msg = objectInputStream.readObject();
                    if (msg instanceof DisconnectMessage) {
                        closed = true;
                        outgoingMessages.clear();
                        objectOutputStream.writeObject("*goodbye*");
                        objectOutputStream.flush();
                        server.clientDisconnected(playerID);
                        close();
                    } else {
                        incomingMessages.put(msg);
                    }
                } catch (IOException | ClassNotFoundException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void close() {
        closed = true;
        sendThread.interrupt();
        if (receiveThread != null)
            receiveThread.interrupt();
        try {
            socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    void closeWithError(String err) {
        //TODO announce to other clients.
        close();
    }

    public void send(Object msg) {
        if (msg == null) {
            throw new IllegalArgumentException("Null cannot be sent as a message.");
        } else if (msg instanceof DisconnectMessage){
            outgoingMessages.clear();
        }
        outgoingMessages.add(msg);
    }

    private void receiveNickname() throws ClassNotFoundException, IOException, InterruptedException {
        MessageToServer nickMessage = (MessageToServer) incomingMessages.take();
        while (!(nickMessage instanceof SendNickMessage)) {
            incomingMessages.put(nickMessage);
            nickMessage = (MessageToServer) incomingMessages.take();
        }
        while (!server.isNicknameAvailable(((SendNickMessage) nickMessage).getNickname())) {
            //Send the refuse
            MessageToClient msg = new NickResponseMessage(null);
            send(msg);
            //Read new nick proposal
            nickMessage = (MessageToServer) incomingMessages.take();
        }
        //Received valid nickname
        nickname = ((SendNickMessage) nickMessage).getNickname();
        MessageToClient msg = new NickResponseMessage(nickname);
        send(msg);
    }

    private void receiveCreateMatch() throws IOException, ClassNotFoundException, InterruptedException {
        MessageToServer matchMessage = (MessageToServer) incomingMessages.take();
        while (!(matchMessage instanceof CreateMatchMessage)) {
            incomingMessages.put(matchMessage);
            System.out.println("Expected CreateMatchMessage, received " + matchMessage.getClass());
            matchMessage = (MessageToServer) incomingMessages.take();
        }
        if (((CreateMatchMessage)matchMessage).getNewMatch()) {
            createNewMatch(objectInputStream);
        } else {
            joinMatch(objectInputStream);
        }
    }
//TODO 05/12/2022
    private void createNewMatch(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        MessageToServer infoMessage = (MessageToServer) objectInputStream.readObject();
        while (!(infoMessage instanceof SendStartInfoMessage)){
            System.out.println("Expected CreateMatchMessage, received " + infoMessage.getClass());
            infoMessage = (MessageToServer) objectInputStream.readObject();
        }
        //Received information
        match = new MatchController(server.generateMatchID(), ((SendStartInfoMessage) infoMessage).getNumOfPlayers());
        new Thread(match);
        wizard = ((SendStartInfoMessage) infoMessage).getWizard();
        //TODO Manage game mode
        server.addMatch(match);
        try {
            match.addPlayer(this);
        } catch (MatchMakingException e) {
            e.printStackTrace();
            return;
        }
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
        try {
            match.setWizardOfPlayer(this, ((SendChosenWizardMessage) wizardChosen).getWizard());
            this.wizard = ((SendChosenWizardMessage) wizardChosen).getWizard();
            MessageToClient confirm = new ConfirmJoiningMessage(true, "You joined the game");
            send(confirm);
        } catch (GameException e) {
            MessageToClient denyJoining = new ConfirmJoiningMessage(false, "Wizard not available");
            send(denyJoining);
            receiveWizard(objectInputStream);
        }
    }

    public void sendAvailableWizards() throws IOException {
        MessageToClient availableWizards = new SendAvailableWizardsMessage(match.getAvailableWizards());
        send(availableWizards);
    }

    public boolean wizardAvailable() {
        return wizard != null;
    }

    public MatchController getMatch() {
        return this.match;
    }

    public Wizard getWizard() { return this.wizard; }

    public int getPlayerID() {
        return this.playerID;
    }

    public String getNickname() { return this.nickname; }


    //METHODS FOR TEST PURPOSES ONLY

    protected void setWizard(Wizard wizard){
        this.wizard = wizard;
        match.setWizardOfPlayer(this, wizard);
    }

    protected void setNickname(String nickname){
        this.nickname = nickname;
    }

    protected void setMatch(MatchController match){
        this.match = match;
    }
}
