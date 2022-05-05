package it.polimi.ingsw.Network.Messages.toServer;

import it.polimi.ingsw.Controller.ClientHandler;

import java.io.Serializable;

public abstract class MessageToServer implements Serializable {
    //TODO Do we need an ID for each message?
    public abstract void process(ClientHandler ch);
}
