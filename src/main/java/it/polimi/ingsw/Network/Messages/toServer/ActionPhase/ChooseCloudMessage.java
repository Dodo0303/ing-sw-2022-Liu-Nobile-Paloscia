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
        /*
        MatchController match = ch.getMatch();
        MessageToClient message;
        Player player = match.getGame().getPlayers().get(ch.getPlayerID());
        try {
            match.takeStudentsFromCloud(player, match.getGame().getClouds().get(cloudID), match.getTotalMatchPlayers());
            message = new ConfirmCloudMessage(ch.getPlayerID(), this.cloudID);
            //TODO SEND MESSAGE
            //message = new ChangeTurnMessage(Phase.XXX);// TODO
        } catch (GameException e) {
            message = new DenyMovementMessage();
        }
        //TODO send message method in ClientHandler
        */

    }
}
