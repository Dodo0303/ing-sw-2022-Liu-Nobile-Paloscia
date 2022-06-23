package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Phases.ClientHandlerPhase;
import it.polimi.ingsw.Controller.Phases.NicknamePhase;
import it.polimi.ingsw.Exceptions.MatchMakingException;
import it.polimi.ingsw.Model.Wizard;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.SendAvailableWizardsMessage;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;
import it.polimi.ingsw.Network.Messages.toServer.DisconnectMessage;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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
    private ClientHandlerPhase phase;

    public ClientHandler(EriantysServer server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        this.playerID = playerID;
        phase = new NicknamePhase(this);
        wizard = null;
        closed = false;
        incomingMessages = new LinkedBlockingQueue<Object>();
        outgoingMessages = new LinkedBlockingQueue<Object>();
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        sendThread =  new SendThread();
        sendThread.start();
    }

    @Override
    public void run() {
        try {
            while(true) {
                MessageToServer message = (MessageToServer) incomingMessages.take();
                phase.process(message);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
    }
    /** Set up connection and send message when send(Object msg) is called.
     */
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
                        objectOutputStream.reset();
                        System.out.print(msg.getClass().toString() + " sent by server" + "\n"); //TODO delete after tests
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            } catch (Exception e) {
                System.out.println("Unexpected error shuts down server's sendThread:\n");
                e.printStackTrace();
                close();
            }
        }
    }
    /** Listen on the chosen port, receive any incoming message
     */
    private class ReceiveThread extends Thread {
        public void run() {
            while(!closed) {
                try {
                    Object msg = objectInputStream.readObject();
                    if (msg instanceof DisconnectMessage) {
                        closed = true;
                        outgoingMessages.clear();
                        objectOutputStream.writeObject(new DisconnectMessage());
                        objectOutputStream.flush();
                        server.clientDisconnected(playerID);
                        close();
                    } else {
                        incomingMessages.put(msg);
                        System.out.print(msg.getClass().toString() + " received by server" + "\n"); //TODO delete after tests
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Connection with " + nickname +" lost.");
                    match.broadCastDisconnection(nickname);
                    close();
                }
            }
        }
    }

    /** Shut down THIS clientHandler.
     */
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

    /** Shut down THIS clientHandler and announce the disconnection to other clients.
     */
    void closeWithError(String err) {
        //TODO announce to other clients.
        close();
    }

    /** Send message msg.
     */
    public void send(Object msg) {
        if (msg == null) {
            throw new IllegalArgumentException("Null cannot be sent as a message.");
        }
        while (!outgoingMessages.offer(msg));
    }

    public void createMatchController(int numOfPlayers, Wizard wizard, boolean expertMode) {
        match = new MatchController(server.generateMatchID(), numOfPlayers);
        new Thread(match).start();
        this.wizard = wizard;
        server.addMatch(match);
        try {
            match.addPlayer(this);
            match.setWizardOfPlayer(this, wizard);
        } catch (MatchMakingException e) {
            e.printStackTrace();
        }
    }

    public void sendAvailableWizards() throws IOException {
        if (!wizardAvailable()) {
            MessageToClient availableWizards = new SendAvailableWizardsMessage(match.getAvailableWizards());
            send(availableWizards);
        }
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

    public EriantysServer getServer() {
        return this.server;
    }

    public String getNickname() { return this.nickname; }

    public void setWizard(Wizard wizard){
        this.wizard = wizard;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public void setMatch(MatchController match){
        this.match = match;
    }

    public ClientHandlerPhase getPhase() {
        return phase;
    }

    public void setPhase(ClientHandlerPhase phase) {
        this.phase = phase;
    }
}
