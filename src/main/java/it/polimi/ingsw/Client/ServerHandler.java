package it.polimi.ingsw.Client;

import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerHandler implements Runnable {
    private ObjectOutputStream output;
    private Scanner input;
    private Socket socket;
    private LinkedBlockingQueue<Object> outgoingMessages;

    ServerHandler(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.input = new Scanner(socket.getInputStream());
        this.output = new ObjectOutputStream(socket.getOutputStream());
        outgoingMessages = new LinkedBlockingQueue<Object>();
    }

    @Override
    public void run() {
        while (true) {
            String incomingMessage = input.nextLine();

        }
    }
}
