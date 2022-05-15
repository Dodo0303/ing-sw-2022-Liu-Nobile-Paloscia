package it.polimi.ingsw.Network.Messages.toServer.CharacterPhase;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class MoveNoEntryMessage extends MessageToServer {
    private int islandID;

    public MoveNoEntryMessage(int islandID) {
        this.islandID = islandID;
    }

    public int getIslandID() {
        return islandID;
    }

    @Override
    public void process(ClientHandler ch) {

    }
}
