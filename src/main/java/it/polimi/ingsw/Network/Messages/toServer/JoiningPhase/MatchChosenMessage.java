package it.polimi.ingsw.Network.Messages.toServer.JoiningPhase;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class MatchChosenMessage extends MessageToServer {
    private int matchID;

    public MatchChosenMessage(int matchID) {
        this.matchID = matchID;
    }

    public int getMatchID() {
        return matchID;
    }

}
