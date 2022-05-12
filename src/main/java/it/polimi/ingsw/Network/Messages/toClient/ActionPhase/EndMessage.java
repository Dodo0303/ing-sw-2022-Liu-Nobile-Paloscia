package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class EndMessage extends MessageToClient {

    private final String winnerID;
    private final String reason;

    public EndMessage(String winnerID, String reason) {
        this.winnerID = winnerID;
        this.reason = reason;
    }

    public String getWinnerID() {
        return winnerID;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public void process(ServerHandler ch) {

    }
}
