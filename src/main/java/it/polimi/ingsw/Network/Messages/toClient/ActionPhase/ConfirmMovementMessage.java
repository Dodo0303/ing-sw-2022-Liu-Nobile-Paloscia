package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.util.HashMap;

public class ConfirmMovementMessage extends MessageToClient {
    private final int islandIndex;
    private HashMap<Integer, Island> islands;

    public ConfirmMovementMessage(int islandIndex, HashMap<Integer, Island> islands) {
        this.islandIndex = islandIndex;
        this.islands = islands;
    }

    public int getIslandIndex() {
        return islandIndex;
    }

    public HashMap<Integer, Island> getIslands() {
        return this.islands;
    }
    @Override
    public void process(ServerHandler client) {
        client.getClient().setPhase(Phase.Action3);
        client.getClient().getGame().set_islands(islands);
        client.getClient().chooseCloud();
    }
}
