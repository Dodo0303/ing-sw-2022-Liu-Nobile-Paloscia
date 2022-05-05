package it.polimi.ingsw.Network.Messages.toClient;

import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Controller.ClientHandler;

import java.io.Serializable;

public abstract class MessageToClient implements Serializable {
    //TODO
    public abstract void process(ServerHandler ch);
}
