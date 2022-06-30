package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Client.EriantysClient;
import it.polimi.ingsw.Exceptions.NoSuchMatchException;
import it.polimi.ingsw.Network.Messages.ConnectionStatusMessage;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.io.IOException;
import java.net.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Singleton containing server information, i.e. its port and current matches.
 */
public class EriantysServer implements Runnable{

    /**
     * Port of the server
     */
    private int port;
    /**
     * Matches in progress
     */
    private ArrayList<MatchController> currentMatches;
    /**
     * Clients connected to the server
     */
    private ArrayList<ClientHandler> clients; //ClientID corresponds to index of this arraylist.
    /**
     * True if the server is shutting down
     */
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
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            System.out.println("\n\nNetwork interfaces:\n");
            while (networks.hasMoreElements()){
                NetworkInterface network = networks.nextElement();
                System.out.println(network.getName() + ":\n");
                Enumeration<InetAddress> addresses = network.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    System.out.println("\t" + ip + "\n");
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
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

    /**
     * Sends a message to every client connected to the server
     * @param msg message to be sent
     */
    synchronized public void sendToAll(Object msg) {
        if (msg == null) {
            throw new IllegalArgumentException("Null cannot be sent as a message.");
        }
        for (ClientHandler cl : clients)
            cl.send(msg);
    }

    /**
     * This method handle the disconnection of a client
     * @param player player disconnected
     */
    synchronized void clientDisconnected(ClientHandler player) {
        int index = clients.indexOf(player);
        if (clients.remove(player)) {
            ConnectionStatusMessage msg = new ConnectionStatusMessage(index, false, getClients());
            sendToAll(msg);
            System.out.println("Connection with client number " + index + " closed.\n");
        }
    }

    /**
     * Pick a random port
     */
    public void randomPort() {
        int lowest_port = 1025;
        int highest_port = 65535;
        port = new Random().nextInt(highest_port+1 - lowest_port) + lowest_port;
    }

    /**
     * Checks whether a nickname is available or not
     * @param nickname nickname to be checked
     * @return true if the nickname is available. False if not
     */
    public boolean isNicknameAvailable(String nickname) {
        for (ClientHandler client: clients) {
            if (nickname.equals(client.getNickname())) return false;
        }
        return true;
    }

    /**
     * Remove a client from the list of clients connected
     * @param clientToRemove client to be removed
     */
    public void removeClient(ClientHandler clientToRemove) {
        clients.remove(clientToRemove);
    }

    /**
     * Add a match to the list of matches in progress
     * @param matchToAdd match to be added
     */
    public void addMatch(MatchController matchToAdd) {
        this.currentMatches.add(matchToAdd);
    }

    /**
     * Remove a match from the list of matches in progress
     * @param matchToRemove match to be removed
     */
    public void removeMatch(MatchController matchToRemove) {
        this.currentMatches.remove(matchToRemove);
    }

    /**
     * Generates an ID for a match
     * @return the ID generated
     */
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


    /**
     *
     * @param ID ID of the match
     * @return the instance of the match requested
     * @throws NoSuchMatchException if there is no match with the given ID
     */
    public MatchController getMatchById(int ID) throws NoSuchMatchException {//TODO why ID differs by match.getID by 1?
        for (MatchController match :
                currentMatches) {
            if (match.getID() == ID)
                return match;
        }
        throw new NoSuchMatchException();
    }

    /**
     *
     * @return the list of matches in progress
     */
    public List<MatchController> getCurrentMatches() {
        return new ArrayList<>(this.currentMatches);
    }

    /**
     *
     * @return the list of matches still in matchmaking
     */
    public List<MatchController> getMatchmakingMatches() {
        List<MatchController> res = new ArrayList<>();
        for (MatchController match :
                currentMatches) {
            if (match.getStatus() == MatchStatus.MATCHMAKING)
                res.add(match);
        }
        return res;
    }

    /**
     *
     * @return the list of clients connected
     */
    public List<ClientHandler> getClients() {
        return new ArrayList<>(this.clients);
    }
}
