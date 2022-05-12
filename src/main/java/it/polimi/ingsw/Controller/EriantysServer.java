package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.NoSuchMatchException;

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
public class EriantysServer {

    private final int port;
    private static EriantysServer instance;
    private ArrayList<MatchController> currentMatches;
    private ArrayList<ClientHandler> clients;

    private EriantysServer(int port) {
        this.port = port;
        currentMatches = new ArrayList<>();
        clients = new ArrayList<>();
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

    public void addMatch(MatchController matchToAdd) {
        this.currentMatches.add(matchToAdd);
    }

    public void removeMatch(MatchController matchToRemove) {
        this.currentMatches.remove(matchToRemove);
    }

    public static EriantysServer instance() {
        int lowest_port = 1025;
        int highest_port = 65535;
        if (instance==null) {
            instance = new EriantysServer(new Random().nextInt(highest_port+1 - lowest_port) + lowest_port);
        }
        return instance;
    }

    /**
     * Starts the server by creating a new ServerSocket. For each incoming connection, it creates a new Socket
     * and passes it to a new ClientHandler.
     */
    public void startServer() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }

        System.out.println("Server ready on port " + this.port);

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                ClientHandler newClient = new ClientHandler(this, socket);
                executor.submit(newClient);
                clients.add(newClient);
            } catch (IOException e) {
                break;
            }
        }

        executor.shutdown();
    }

    public void removeClient(ClientHandler clientToRemove) {
        clients.remove(clientToRemove);
    }

    public boolean isNicknameAvailable(String nickname) {
        for (ClientHandler client: clients) {
            if (nickname.equals(client.getNickname())) return false;
        }
        return true;
    }

    public MatchController getMatchById(int ID) throws NoSuchMatchException {
        for (MatchController match :
                currentMatches) {
            if (match.getID() == ID)
                return match;
        }
        throw new NoSuchMatchException();
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

}
