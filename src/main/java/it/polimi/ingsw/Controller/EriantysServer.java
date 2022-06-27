package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Client.EriantysClient;
import it.polimi.ingsw.Exceptions.NoSuchMatchException;
import it.polimi.ingsw.Network.Messages.ConnectionStatusMessage;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Singleton containing server information, i.e. its port and current matches.
 */
public class EriantysServer implements Runnable{

    private int port;
    private ArrayList<MatchController> currentMatches;
    private ArrayList<ClientHandler> clients; //ClientID corresponds to index of this arraylist.
    boolean shutdown;

    public EriantysServer() {
        //randomPort();
        port = 12345;
        currentMatches = new ArrayList<>();
        clients = new ArrayList<>();
        shutdown = false;
    }

    public static void main(String[] args) {
        EriantysServer server;
        server = new EriantysServer();
        new Thread(server).start();
    }

    /**
     * Starts the server by creating a new ServerSocket. For each incoming connection, it creates a new Socket
     * and passes it to a new ClientHandler.
     */
    @Override
    public void run() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        System.out.println("Server ready on port " + this.port);
        while (!shutdown) {
            try {
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(10000);
                ClientHandler newClient = new ClientHandler(this, socket);
                executor.submit(newClient);
                clients.add(newClient);
                new Thread(newClient).start();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Fatal exception error, server down.");
                shutdown = true;
            }
        }

        executor.shutdown();
    }

    synchronized public void sendToAll(Object msg) {
        if (msg == null) {
            throw new IllegalArgumentException("Null cannot be sent as a message.");
        }
        for (ClientHandler cl : clients)
            cl.send(msg);
    }

    synchronized void clientDisconnected(ClientHandler player) {
        int index = clients.indexOf(player);
        if (clients.remove(player)) {
            ConnectionStatusMessage msg = new ConnectionStatusMessage(index, false, getClients());
            sendToAll(msg);
            playerDisconnected(player);
            System.out.println("Connection with client number " + index + " closed.\n");
        }
    }

    protected void playerDisconnected(ClientHandler player) {
        //TODO Resilienza alle disconnessioni; Persistenza
    }

    public void randomPort() {
        int lowest_port = 1025;
        int highest_port = 65535;
        port = new Random().nextInt(highest_port+1 - lowest_port) + lowest_port;
    }

    public boolean isNicknameAvailable(String nickname) {
        for (ClientHandler client: clients) {
            if (nickname.equals(client.getNickname())) return false;
        }
        return true;
    }

    public void removeClient(ClientHandler clientToRemove) {
        clients.remove(clientToRemove);
    }

    public void addMatch(MatchController matchToAdd) {
        this.currentMatches.add(matchToAdd);
    }

    public void removeMatch(MatchController matchToRemove) {
        this.currentMatches.remove(matchToRemove);
    }

    public int generateMatchID() {
        Random rnd = new Random();

        int id = rnd.nextInt(Integer.MAX_VALUE);
        List<Integer> IDs = new ArrayList<>();
        for (MatchController match :
                currentMatches) {
            IDs.add(match.getID());
        }
        while (IDs.contains(id)) {
            id = rnd.nextInt(Integer.MAX_VALUE);
        }

        return id;
    }


    public MatchController getMatchById(int ID) throws NoSuchMatchException {//TODO why ID differs by match.getID by 1?
        for (MatchController match :
                currentMatches) {
            if (match.getID() == ID)
                return match;
        }
        throw new NoSuchMatchException();
    }

    public int getPort() { return this.port; }

    public List<MatchController> getCurrentMatches() {
        return new ArrayList<>(this.currentMatches);
    }

    public List<MatchController> getMatchmakingMatches() {
        List<MatchController> res = new ArrayList<>();
        for (MatchController match :
                currentMatches) {
            if (match.getStatus() == MatchStatus.MATCHMAKING)
                res.add(match);
        }
        return res;
    }

    public List<ClientHandler> getClients() {
        return new ArrayList<>(this.clients);
    }
}
