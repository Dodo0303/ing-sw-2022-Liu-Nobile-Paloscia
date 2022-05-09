package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.util.HashMap;

public class ConfirmMovementMessage extends MessageToClient {
    private int islandIndex;
    private HashMap<Integer, Island> islands;

    public ConfirmMovementMessage(int islandIndex) {
        this.islandIndex = islandIndex;
    }

    public HashMap<Integer, Island> getIslands() {
        return this.islands;
    }
}
