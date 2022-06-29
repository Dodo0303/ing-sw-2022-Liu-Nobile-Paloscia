package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Network.Messages.toClient.DropConnectionMessage;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;
import it.polimi.ingsw.Network.Messages.toServer.DisconnectMessage;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;
import it.polimi.ingsw.Network.Messages.toServer.PingMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerHandler implements Runnable {
    int playerID;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket socket;
    private LinkedBlockingQueue<Object> outgoingMessages;
    private LinkedBlockingQueue<Object> incomingMessages;
    Boolean closed;
    private SendThread sendThread;
    private ReceiveThread receiveThread;
    private ArrayList<ClientHandler> clients; //clients who are currently connected to the server.
    private ViewController client;
    private PingThread pingThread;

    public ServerHandler(String host, int port, ViewController client) throws IOException {
        this.socket = new Socket(host, port);
        this.input = new ObjectInputStream(socket.getInputStream());
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.outgoingMessages = new LinkedBlockingQueue<Object>();
        this.incomingMessages = new LinkedBlockingQueue<Object>();
        this.sendThread = new SendThread();
        this.receiveThread = new ReceiveThread();
        this.pingThread = new PingThread();
        this.closed = false;
        this.client = client;
        output.writeObject("Hello Server.");
        output.flush();
        try {
            Object resp = input.readObject();
            playerID = (Integer) resp;
        } catch (ClassNotFoundException e) {
            throw new IOException("Illegal response from server.");
        }
    }

    @Override
    public void run() {
        this.sendThread.start();
        this.receiveThread.start();
        this.pingThread.start();
    }

    public void send(Object message) {
        while (!outgoingMessages.offer(message));
    }

    public void shutdown() {
        System.out.println("\nShutting down");
        sendThread.interrupt();
        receiveThread.interrupt();
        pingThread.interrupt();
        try {
            socket.close();
        }
        catch (Exception e) {
        }
    }

    private class SendThread extends Thread {
        @Override
        public void run() {
            try {
                while(!closed) {
                    Object message = outgoingMessages.take();
                    output.writeObject(message);
                    output.flush();
                    //System.out.print(message.getClass().toString() + " sent by client" + "\n"); //TODO delete after tests
                }
            } catch (Exception e) {
                System.out.print(e.getMessage());
                shutdown();
            }
        }
    }

    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            try {
                while(!closed) {
                    Object message = input.readObject();
                    //System.out.print(message.getClass().toString() + " received from server" + "\n"); //TODO delete after tests
                    if (message instanceof DisconnectMessage) {
                        shutdown();
                    } else if (message instanceof DropConnectionMessage) {
                        System.out.println("Player " + ((DropConnectionMessage) message).getNickname() + " lost connection. Game over.");
                        client.gameOver("\nShutting down");
                        shutdown();
                    }
                    client.messageReceived(message);
                }
            } catch (Exception e) {
                System.out.print(e.getMessage());
                shutdown();
            }
        }
    }

    private class PingThread extends Thread {
        @Override
        public void run() {
            try {
                while(!closed){
                    MessageToServer ping = new PingMessage();
                    outgoingMessages.put(ping);
                    sleep(5000);
                }
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
        }
    }
    public Socket getSocket() {
        return socket;
    }


    public Boolean getClosed() {
        return closed;
    }

    public SendThread getSendThread() {
        return sendThread;
    }

    public ReceiveThread getReceiveThread() {
        return receiveThread;
    }

    public ArrayList<ClientHandler> getClients() {
        return clients;
    }

    public ViewController getClient() {
        return client;
    }
}
