package it.polimi.ingsw.Network.Messages.toServer;

import it.polimi.ingsw.Controller.ClientHandler;

import java.io.Serializable;

public abstract class MessageToServer implements Serializable {
    public abstract void process(ClientHandler ch);
}
