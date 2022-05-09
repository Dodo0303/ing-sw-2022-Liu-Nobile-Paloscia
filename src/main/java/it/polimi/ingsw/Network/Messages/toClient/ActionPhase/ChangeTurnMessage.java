package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.CLI.ServerHandler;
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

    @Override
    public void process(ServerHandler client) {

    }

    /*

    public Phase getGamePhase() {
        return this.gamePhase;
    }

    */
}
