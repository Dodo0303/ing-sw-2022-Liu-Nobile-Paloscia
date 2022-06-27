package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Exceptions.EmptyCloudException;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class ConfirmCloudMessage extends MessageToClient {
    private final String playerID;
    private final int cloudID;

    public ConfirmCloudMessage(String playerID, int cloudID) {
        this.playerID = playerID;
        this.cloudID = cloudID;

    }

    public String getPlayerID() {
        return this.playerID;
    }

    public int getCloudID() {
        return this.cloudID;
    }
    
    @Override
    public void process(ServerHandler client) throws EmptyCloudException {
        CLI cliClient = (CLI) client.getClient();
        StudentColor color;
        int num = (cliClient.getGame().getPlayers().size() == 3)? 4 : 3;
        for (int i = 0; i < num; i++) {
            color = cliClient.getGame().getClouds().get(cloudID).extractStudent();
            cliClient.getGame().getPlayers().get(cliClient.getGame().getPlayerIndexFromNickname(playerID)).addStudentToEntrance(color);
        }
    }

    public void processGUI(ServerHandler client) throws EmptyCloudException {
        GUI guiClient = (GUI) client.getClient();
        StudentColor color;
        int num = (guiClient.getGame().getPlayers().size() == 3)? 4 : 3;
        for (int i = 0; i < num; i++) {
            color = guiClient.getGame().getClouds().get(cloudID).extractStudent();
            guiClient.getGame().getPlayers().get(guiClient.getGame().getPlayerIndexFromNickname(playerID)).addStudentToEntrance(color);
        }
    }
}
