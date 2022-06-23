package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;

import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class MoveMothernatureMessage extends MessageToClient {
    @Override
    public void process(ServerHandler client) {
        client.getClient().setPhase(Phase.Action2);
    }
}
