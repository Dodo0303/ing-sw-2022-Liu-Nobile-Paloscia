package it.polimi.ingsw.Network.Messages.toServer;

import it.polimi.ingsw.Controller.ClientHandler;

public abstract class MessageToServer {
    //TODO Do we need an ID for each message?
    public abstract void process(ClientHandler ch);
}
