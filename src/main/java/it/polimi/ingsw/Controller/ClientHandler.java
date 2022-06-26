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
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientHandler implements Runnable {
    private Socket socket;
    private EriantysServer server;
    private MatchController match;
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

    /** Send message msg.
     */
    public void send(Object msg) {
        if (msg == null) {
            throw new IllegalArgumentException("Null cannot be sent as a message.");
        }
        while (!outgoingMessages.offer(msg));
    }

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

    public boolean wizardAvailable() {
        return wizard != null;
    }

    public MatchController getMatch() {
        return this.match;
    }

    public Wizard getWizard() { return this.wizard; }

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
