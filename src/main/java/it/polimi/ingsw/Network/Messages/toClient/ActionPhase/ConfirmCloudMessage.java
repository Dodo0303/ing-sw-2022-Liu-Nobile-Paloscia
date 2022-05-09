package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class ConfirmCloudMessage extends MessageToClient {
    //TODO
    private int playerID;
    private int cloudID;

    public ConfirmCloudMessage(int playerID, int cloudID) {
        this.playerID = playerID;
        this.cloudID = cloudID;

    }

    public int getPlayerID() {
        return this.playerID;
    }

    public int getCloudID() {
        return this.cloudID;
    }
    @Override
    public void process(ServerHandler client) {

    }
}
