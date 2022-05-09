package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class ChangeTurnMessage extends MessageToClient {
    //TODO
    private int playerID;
    //private Phase gamePhase;

    public ChangeTurnMessage(int playerID) {//TODO Phase gamePhase
        this.playerID = playerID;
        //this gamePhase = gamePhase;
    }
    public int getPlayerID() {
        return this.playerID;
    }

    /*

    public Phase getGamePhase() {
        return this.gamePhase;
    }

    */
}
