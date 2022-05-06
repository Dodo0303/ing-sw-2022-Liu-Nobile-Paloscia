package it.polimi.ingsw.Controller.Phases;

import it.polimi.ingsw.Controller.MatchController;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public abstract class Phase {
    MatchController match;

    public Phase(MatchController match) {
        this.match = match;
    }

    public abstract void process(MessageToServer msg);
}
