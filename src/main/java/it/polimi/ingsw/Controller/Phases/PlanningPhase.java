package it.polimi.ingsw.Controller.Phases;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Controller.MatchController;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;
import it.polimi.ingsw.Network.Messages.toServer.PlanningPhase.SendAssistantMessage;

public class PlanningPhase extends Phase{

    public PlanningPhase(MatchController match) {
        super(match);
    }

    public void process(MessageToServer msg, ClientHandler ch) {
        if (!(msg instanceof SendAssistantMessage)) {
            match.denyMovement(ch);
        } else {
            try {
                match.setAssistantOfCurrentPlayer(((SendAssistantMessage) msg).getAssistant());
            } catch (GameException e) {
                match.denyMovement(ch);
            }
            this.match.notify();
        }
    }
}
