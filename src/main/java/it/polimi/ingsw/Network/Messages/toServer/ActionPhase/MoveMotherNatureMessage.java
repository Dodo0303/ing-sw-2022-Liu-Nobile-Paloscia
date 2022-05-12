package it.polimi.ingsw.Network.Messages.toServer.ActionPhase;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class MoveMotherNatureMessage extends MessageToServer {
    private final int islandIndex;

    public MoveMotherNatureMessage(int islandIndex) {
        this.islandIndex = islandIndex;
    }

    public int getIslandIndex() {
        return this.islandIndex;
    }

    @Override
    public void process(ClientHandler ch) {

    }
}