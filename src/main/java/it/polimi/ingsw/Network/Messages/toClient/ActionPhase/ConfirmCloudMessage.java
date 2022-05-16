package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
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
        StudentColor color;
        int num = (client.getClient().getGame().getPlayers().size() == 3)? 4 : 3;
        for (int i = 0; i < num; i++) {
            color = client.getClient().getGame().getClouds().get(cloudID).extractStudent();
            client.getClient().getGame().getPlayers().get(client.getClient().getGame().getPlayerIndexFromNickname(playerID)).addStudentToEntrance(color);
        }
    }
}
