package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.util.ArrayList;
import java.util.HashMap;

public class ConfirmMovementMessage extends MessageToClient {
    private int islandIndex;
    private HashMap<Integer, Island> islands;
    private ArrayList<Cloud> clouds;//prep for AP3

    public ConfirmMovementMessage(int islandIndex, HashMap<Integer, Island> islands, ArrayList<Cloud> clouds) {
        this.islandIndex = islandIndex;
        this.islands = islands;
        this.clouds = clouds;
    }

    public HashMap<Integer, Island> getIslands() {
        return this.islands;
    }
    @Override
    public void process(ServerHandler client) {
        client.getClient().setIslands(islands);
        client.getClient().setClouds(clouds);
    }
}
