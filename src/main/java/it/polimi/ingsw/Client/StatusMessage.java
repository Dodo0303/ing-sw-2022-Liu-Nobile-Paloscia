package it.polimi.ingsw.Client;

import it.polimi.ingsw.Controller.ClientHandler;

import java.io.Serializable;
import java.util.ArrayList;

public class StatusMessage implements Serializable {
    public final ArrayList<ClientHandler> clients;
    public String nickname;

    public StatusMessage(ArrayList<ClientHandler> clients, String nickname) {
        this.clients = clients;
        this.nickname = nickname;
    }

}
