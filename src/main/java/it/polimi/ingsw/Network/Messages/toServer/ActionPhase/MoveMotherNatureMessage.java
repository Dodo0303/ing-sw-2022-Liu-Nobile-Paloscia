package it.polimi.ingsw.Network.Messages.toServer.ActionPhase;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class MoveMotherNatureMessage extends MessageToServer {
    private int numOfJumps;

    public MoveMotherNatureMessage(int numOfJumps) {
        this.numOfJumps = numOfJumps;
    }

    public int getNumOfJumps() {
        return numOfJumps;
    }

    @Override
    public void process(ClientHandler ch) {

    }
}
