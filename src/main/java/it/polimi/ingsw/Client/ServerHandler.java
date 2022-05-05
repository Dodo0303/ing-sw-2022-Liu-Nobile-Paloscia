package it.polimi.ingsw.Client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerHandler implements Runnable{
    //TODO

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket socket;
    private EriantysClient client;

    ServerHandler(EriantysClient client, Socket server) {
        this.socket = socket;
        this.client = client;
    }

    @Override
    public void run() {

    }
}
