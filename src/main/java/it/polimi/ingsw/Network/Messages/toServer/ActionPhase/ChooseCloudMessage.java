package it.polimi.ingsw.Network.Messages.toServer.ActionPhase;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class ChooseCloudMessage extends MessageToServer {
    private int cloudID;

    public ChooseCloudMessage(int cloudID) {
        this.cloudID = cloudID;
    }

    public int getCloudID() {
        return cloudID;
    }

    @Override
    public void process(ClientHandler ch) {
    }
}
