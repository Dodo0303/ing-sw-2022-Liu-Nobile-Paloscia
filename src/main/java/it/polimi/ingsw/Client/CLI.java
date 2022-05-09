package it.polimi.ingsw.Client;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.util.Scanner;

public class CLI {
    private String nickName;
    private String host;
    private int port;
    private final Scanner input = new Scanner(System.in);
    private ServerHandler serverHandler;

    public void start() {
        System.out.print("Host?");
        host = input.nextLine();
        System.out.print("Port?");
        port = Integer.parseInt(input.nextLine());
        System.out.print("NickName?");
        nickName = input.nextLine();

        try {
            serverHandler = new ServerHandler(host,port, this);
        } catch (IOException e) {
        }
        Thread serverHandlerThread  = new Thread(this.serverHandler);
        serverHandlerThread.start();

        while(true) {

        }
    }

    public void disconnect() {
        //TODO
    }

    public void send(Object message) {
        //TODO
    }

    public void resetOutput() {
        //TODO
    }


    public String getNickName() {
        return nickName;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Scanner getInput() {
        return input;
    }

    public ServerHandler getServerHandler() {
        return serverHandler;
    }

    public void setNickName(String str) {
        nickName = str;
    }

}
