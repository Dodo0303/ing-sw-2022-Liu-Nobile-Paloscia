package it.polimi.ingsw.Network.Messages.toClient;

import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Exceptions.EmptyCloudException;
import it.polimi.ingsw.Exceptions.FullTableException;

import java.io.Serializable;

public abstract class MessageToClient implements Serializable {
    public abstract void process(ServerHandler client) throws FullTableException, InterruptedException, EmptyCloudException;
}
