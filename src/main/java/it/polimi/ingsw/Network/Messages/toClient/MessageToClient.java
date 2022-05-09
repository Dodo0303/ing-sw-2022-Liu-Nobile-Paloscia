package it.polimi.ingsw.Network.Messages.toClient;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Controller.ClientHandler;

import java.io.Serializable;

public abstract class MessageToClient implements Serializable {
    public abstract void process(ServerHandler client);
}
