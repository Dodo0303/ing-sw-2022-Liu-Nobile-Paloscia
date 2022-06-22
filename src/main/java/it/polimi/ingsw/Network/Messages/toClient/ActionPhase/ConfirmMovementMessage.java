package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.util.HashMap;

public class ConfirmMovementMessage extends MessageToClient {
    private final int islandIndex;
    private HashMap<Integer, Island> islands;
    private final String playerNickname;
    private final int towerNum;

    public ConfirmMovementMessage(int islandIndex, HashMap<Integer, Island> islands, String playerNickname, int towerNum) {
        this.islandIndex = islandIndex;
        this.islands = islands;
        this.playerNickname = playerNickname;
        this.towerNum = towerNum;
    }

    public int getIslandIndex() {
        return islandIndex;
    }

    public HashMap<Integer, Island> getIslands() {
        return this.islands;
    }
    @Override
    public void process(ServerHandler client) {
        client.getClient().getGame().set_islands(islands);
        client.getClient().getGame().setMothernature(islandIndex);
        //client.getClient().getGame().getPlayerByNickname(playerNickname).setTowers(towerNum);
        client.getClient().getGame().calculateNumIslandsForPlayers();
        if (client.getClient().getCurrPhase().equals(Phase.Action2)) {
            client.getClient().setPhase(Phase.Action3);
            client.getClient().chooseCloud();
        } else {
            System.out.println(playerNickname + " moved the mother nature to island" + islandIndex);
        }
    }

    public void processGUI(it.polimi.ingsw.Client.GUI.ServerHandler client) {
        client.getClient().getGame().set_islands(islands);
        client.getClient().getGame().setMothernature(islandIndex);
        //client.getClient().getGame().getPlayerByNickname(playerNickname).setTowers(towerNum);
        client.getClient().getGame().calculateNumIslandsForPlayers();
        if (client.getClient().getCurrPhase().equals(Phase_GUI.Action2)) {
            client.getClient().setCurrPhase(Phase_GUI.Action3);
            client.getClient().checkBoard("Choose a cloud");
        } else {
            client.getClient().checkBoard("Mother nature is at" + islandIndex);
        }
    }
}