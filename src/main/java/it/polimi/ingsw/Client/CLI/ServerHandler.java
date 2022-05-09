package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;
import it.polimi.ingsw.Network.Messages.toClient.Uncategorized.DisconnectMessage;
import it.polimi.ingsw.Network.Messages.toClient.Uncategorized.ResetOutputMessage;
import it.polimi.ingsw.Network.Messages.toClient.Uncategorized.StatusMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerHandler implements Runnable {
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket socket;
    private LinkedBlockingQueue<Object> outgoingMessages;
    Boolean closed;
    private SendThread sendThread;
    private ReceiveThread receiveThread;
    private ArrayList<ClientHandler> clients; //clients who are currently connected to the server.
    private CLI client;

    ServerHandler(String host, int port, CLI client) throws IOException {
        this.socket = new Socket(host, port);
        this.input = new ObjectInputStream(socket.getInputStream());
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.outgoingMessages = new LinkedBlockingQueue<Object>();
        this.sendThread = new SendThread();
        this.receiveThread = new ReceiveThread();
        this.closed = false;
        this.client = client;
        output.writeObject("Hello Server.");
        output.flush();
        try {
            Object resp = input.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Illegal response from server.");
        }
    }

    @Override
    public void run() {
        this.sendThread.start();
        this.receiveThread.start();
    }

    void send(Object message) {
        outgoingMessages.add(message);
    }

    void shutdown() {
        sendThread.interrupt();
        receiveThread.interrupt();
        try {
            output.writeObject(new DisconnectMessage());
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
                    if (message instanceof ResetOutputMessage) {
                        output.reset();
                    } else {
                        output.writeObject(message);
                        output.flush();
                    }
                }
            } catch (Exception e) {
                System.out.print("An error occurred, which shut down the socket.");
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
                    if (message instanceof DisconnectMessage) {
                        shutdown();
                    } else if (message instanceof StatusMessage){
                        StatusMessage msg = (StatusMessage)message;
                        clients = msg.clients;
                    } else {
                        client.messageReceived(message);
                    }
                }
            }catch (Exception e) {
                System.out.print("An error occurred, which shut down the socket.");
                shutdown();
            }
        }
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public ObjectInputStream getInput() {
        return input;
    }

    public Socket getSocket() {
        return socket;
    }

    public LinkedBlockingQueue<Object> getOutgoingMessages() {
        return outgoingMessages;
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

    public CLI getClient() {
        return client;
    }

    public void setClients(ArrayList<ClientHandler> clients) {
        this.clients = clients;
    }

    public void setNickName(String str) {
        getClient().setNickName(str);
    }
}
