package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Phases.ClientHandlerPhase;
import it.polimi.ingsw.Controller.Phases.NicknamePhase;
import it.polimi.ingsw.Exceptions.MatchMakingException;
import it.polimi.ingsw.Model.Wizard;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.SendAvailableWizardsMessage;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;
import it.polimi.ingsw.Network.Messages.toServer.DisconnectMessage;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;
import it.polimi.ingsw.Network.Messages.toServer.PingMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class that manage the connection between the client and the match
 */
public class ClientHandler implements Runnable {
    /**
     * Socket used to communicate with the client
     */
    private Socket socket;
    /**
     * Server instance
     */
    private EriantysServer server;
    /**
     * Match instance, if it's been created
     */
    private MatchController match;
    /**
     * Nickname of the client, if already set
     */
    private String nickname;
    /**
     * Wizard chosen by the client
     */
    private Wizard wizard;
    /**
     * Stream of objects coming from the client
     */
    ObjectInputStream objectInputStream;
    /**
     * Stream of objects headed to the client
     */
    ObjectOutputStream objectOutputStream;
    /**
     * Queue of messages received from the client
     */
    private BlockingQueue<Object> incomingMessages;
    /**
     * Queue of messages to send to the client
     */
    private LinkedBlockingQueue<Object> outgoingMessages;
    /**
     * Instance of the thread in charge of sending messages to the client
     */
    private Thread sendThread;
    /**
     * Instance of the thread in charge of receiving messages from the client
     */
    private volatile Thread receiveThread; // Created only after connection is open.
    /**
     * True if the communication with the client has to be closed
     */
    boolean closed;
    /**
     * Phase of the connection with the client.
     */
    private ClientHandlerPhase phase;

    public ClientHandler(EriantysServer server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        phase = new NicknamePhase(this);
        wizard = null;
        closed = false;
        incomingMessages = new LinkedBlockingQueue<Object>();
        outgoingMessages = new LinkedBlockingQueue<Object>();
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        sendThread =  new SendThread(this);
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
        private ClientHandler client;

        public SendThread(ClientHandler client) {
            this.client = client;
        }

        public void run() {
            //setting up connection with client
            try {
                String handle = (String) objectInputStream.readObject();
                if (!"Hello Server.".equals(handle)) {
                    throw new IOException("Incorrect string received from client.\n");
                }
                int temp = 0;
                objectOutputStream.writeObject(temp);
                objectOutputStream.flush();
                receiveThread = new ReceiveThread(client);
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
                if (!closed)
                    close();
            }
        }
    }
    /** Listen on the chosen port, receive any incoming message
     */
    private class ReceiveThread extends Thread {
        private ClientHandler client;
        public ReceiveThread(ClientHandler client) {
            this.client = client;
        }

        public void run() {
            while(!closed) {
                try {
                    Object msg = objectInputStream.readObject();
                    if (msg instanceof DisconnectMessage) {
                        closed = true;
                        outgoingMessages.clear();
                        objectOutputStream.writeObject(new DisconnectMessage());
                        objectOutputStream.flush();
                        server.clientDisconnected(client);
                        close();
                    } else if (msg instanceof PingMessage) {
                        //System.out.println("Received ping");
                    } else {
                        incomingMessages.put(msg);
                        System.out.print(msg.getClass().toString() + " received by server" + "\n"); //TODO delete after tests
                    }
                } catch (IOException | InterruptedException | ClassNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("Connection with " + nickname + " lost.");
                    server.clientDisconnected(client);
                    if (match != null)
                        match.broadCastDisconnection(nickname);
                    close();
                }
            }
        }
    }

    /** Shut down THIS clientHandler.
     */
    private synchronized void close() {
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

    /**
     * Send a message to the client
     * @param msg message to be sent
     */
    public void send(Object msg) {
        if (msg == null) {
            throw new IllegalArgumentException("Null cannot be sent as a message.");
        }
        while (!outgoingMessages.offer(msg));
    }

    /**
     * Creates the match controller that will manage the game created by this player.
     * @param numOfPlayers number of player for this match
     * @param wizard wizard chosen by the client
     * @param expertMode true to turn on expert mode. False to play in normal mode.
     */
    public void createMatchController(int numOfPlayers, Wizard wizard, boolean expertMode) {
        match = new MatchController(server.generateMatchID(), numOfPlayers, server);
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

    /**
     * Send the list of wizards available to the client
     * @throws IOException if any I/O error occurred
     */
    public void sendAvailableWizards() throws IOException {
        if (!wizardAvailable()) {
            if (match.getTotalMatchPlayers() == 4) {
                String[] nicknames = new String[4];
                for (int i = 0;i < 4;i ++) {
                    if (match.getWizards()[i] != null) {
                        if (match.getWizards()[i]== Wizard.WIZARD1) {
                            nicknames[0] = match.getClients().get(i).getNickname();
                        } else if (match.getWizards()[i] == Wizard.WIZARD2) {
                            nicknames[1] = match.getClients().get(i).getNickname();
                        } else if (match.getWizards()[i] == Wizard.WIZARD3) {
                            nicknames[2] = match.getClients().get(i).getNickname();
                        } else if (match.getWizards()[i] == Wizard.WIZARD4) {
                            nicknames[3] = match.getClients().get(i).getNickname();
                        }
                    }
                }
                MessageToClient availableWizards = new SendAvailableWizardsMessage(match.getAvailableWizards(), 4, nicknames);
                send(availableWizards);
            } else {
                MessageToClient availableWizards = new SendAvailableWizardsMessage(match.getAvailableWizards());
                send(availableWizards);
            }
        }
    }

    /**
     *
     * @return whether the client has chosen a wizard or not.
     */
    public boolean wizardAvailable() {
        return wizard != null;
    }

    /**
     *
     * @return the match played by this game
     */
    public MatchController getMatch() {
        return this.match;
    }

    /**
     *
     * @return the wizard chosen by the client
     */
    public Wizard getWizard() { return this.wizard; }

    /**
     *
     * @return the instance of the server
     */
    public EriantysServer getServer() {
        return this.server;
    }

    /**
     *
     * @return the nickname of the player
     */
    public String getNickname() { return this.nickname; }

    /**
     * Set the wizard of the client
     * @param wizard wizard to be set
     */
    public void setWizard(Wizard wizard){
        this.wizard = wizard;
    }

    /**
     * Set the nickname of the client
     * @param nickname nickname to be set
     */
    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    /**
     * Set the match played by the client
     * @param match match to be set
     */
    public void setMatch(MatchController match){
        this.match = match;
    }

    /**
     *
     * @return the phase of the connection with the client
     */
    public ClientHandlerPhase getPhase() {
        return phase;
    }

    /**
     * Set the phase of the connection with the client
     * @param phase phase to be set
     */
    public void setPhase(ClientHandlerPhase phase) {
        this.phase = phase;
    }
}
