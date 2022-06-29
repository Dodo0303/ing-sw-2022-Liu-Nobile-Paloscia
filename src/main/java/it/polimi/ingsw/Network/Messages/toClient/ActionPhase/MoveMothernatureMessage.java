package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.Phase;
import it.polimi.ingsw.Client.ServerHandler;

import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class MoveMothernatureMessage extends MessageToClient {
    @Override
    public void process(ServerHandler client) {
        CLI cliClient = (CLI) client.getClient();
        cliClient.setPhase(Phase.Action2);
    }
}
