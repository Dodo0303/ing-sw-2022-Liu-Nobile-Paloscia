package it.polimi.ingsw.Model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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

    private EriantysServer(int port) { this.port = port; }

    public int getPort() { return this.port; }

    public ArrayList<MatchController> getCurrentMatches() {
        return new ArrayList<>(this.currentMatches);
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
                executor.submit(new ClientHandler(this, socket));
            } catch (IOException e) {
                break;
            }
        }

        executor.shutdown();
    }

}
