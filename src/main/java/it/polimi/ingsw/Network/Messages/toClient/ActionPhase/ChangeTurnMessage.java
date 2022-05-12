package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Controller.Phases.Phase;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class ChangeTurnMessage extends MessageToClient {
    //TODO
    private String playerNickname;
    private String gamePhase;

    public ChangeTurnMessage(String playerNickname, String gamePhase) {//TODO Phase gamePhase
        this.playerNickname = playerNickname;
        this.gamePhase = gamePhase;
    }

    public String getPlayerID() {
        return this.playerNickname;
    }

    public String getGamePhase() {
        return this.gamePhase;
    }

    @Override
    public void process(ServerHandler ch) {

    }
}
