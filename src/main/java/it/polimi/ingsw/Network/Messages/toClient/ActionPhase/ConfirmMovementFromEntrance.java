package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.util.HashMap;

public class ConfirmMovementFromEntrance extends MessageToClient {
    //carry information for AP2
    private int numIslands;
    private HashMap<Integer, Island> islands;

    public ConfirmMovementFromEntrance(int numIslands,HashMap<Integer, Island> islands) {
        this.numIslands = numIslands;
        this.islands = islands;
    }
    @Override
    public void process(ServerHandler client) {
        client.getClient().setNumIslands(numIslands);
        client.getClient().setIslands(islands);
    }
}
