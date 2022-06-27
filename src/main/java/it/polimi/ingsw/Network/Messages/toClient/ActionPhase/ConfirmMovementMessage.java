package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.ServerHandler;
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
        CLI cliClient = (CLI) client.getClient();
        cliClient.getGame().set_islands(islands);
        cliClient.getGame().setMothernature(islandIndex);
        //cliClient.getGame().getPlayerByNickname(playerNickname).setTowers(towerNum);
        cliClient.getGame().calculateNumIslandsForPlayers();
        if (cliClient.getCurrPhase().equals(Phase.Action2)) {
            cliClient.setPhase(Phase.Action3);
            cliClient.chooseCloud();
        } else {
            System.out.println(playerNickname + " moved the mother nature to island" + islandIndex);
        }
    }

    public void processGUI(ServerHandler client) {
        GUI guiClient = (GUI) client.getClient();
        guiClient.getGame().set_islands(islands);
        guiClient.getGame().setMothernature(islandIndex);
        //guiClient.getGame().getPlayerByNickname(playerNickname).setTowers(towerNum);
        guiClient.getGame().calculateNumIslandsForPlayers();
        if (guiClient.getCurrPhase().equals(Phase_GUI.Action2)) {
            guiClient.setCurrPhase(Phase_GUI.Action3);
            guiClient.checkBoard("Choose a cloud");
        } else {
            guiClient.checkBoard("Mother nature is at" + islandIndex);
        }
    }
}