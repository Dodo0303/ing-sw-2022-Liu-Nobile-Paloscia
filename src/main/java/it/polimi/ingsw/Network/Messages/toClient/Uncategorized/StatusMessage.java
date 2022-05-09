package it.polimi.ingsw.Network.Messages.toClient.Uncategorized;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.util.ArrayList;

public class StatusMessage extends MessageToClient {
    public final ArrayList<ClientHandler> clients;

    public StatusMessage(ArrayList<ClientHandler> clients) {
        this.clients = clients;
    }

    @Override
    public void process(ServerHandler client) {

    }
}
