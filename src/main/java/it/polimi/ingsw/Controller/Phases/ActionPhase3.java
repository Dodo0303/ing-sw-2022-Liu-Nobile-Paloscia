package it.polimi.ingsw.Controller.Phases;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Controller.MatchController;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class ActionPhase3 extends Phase {
    public ActionPhase3(MatchController match) {
        super(match);
    }

    @Override
    public void process(MessageToServer msg, ClientHandler ch) {

    }

    @Override
    public void nextPhase() {

    }

    @Override
    public String toString() {
        return "ActionPhase3";
    }
}
