package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Controller.MatchController;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class ConfirmCloudMessage extends MessageToClient {
    //TODO
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
    public void process(ServerHandler ch) {

    }
}
