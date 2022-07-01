package it.polimi.ingsw.Network.Messages.toServer.JoiningPhase;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class CreateMatchMessage extends MessageToServer {
    private Boolean newMatch;

    public CreateMatchMessage(Boolean newMatch) {
        this.newMatch = newMatch;
    }

    public Boolean getNewMatch() {
        return newMatch;
    }

}
