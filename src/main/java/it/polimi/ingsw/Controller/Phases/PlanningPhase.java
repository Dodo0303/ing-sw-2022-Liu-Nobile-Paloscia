package it.polimi.ingsw.Controller.Phases;

import it.polimi.ingsw.Controller.MatchController;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;
import it.polimi.ingsw.Network.Messages.toServer.PlanningPhase.SendAssistantMessage;

public class PlanningPhase extends Phase{

    public PlanningPhase(MatchController match) {
        super(match);
    }

    public void process(MessageToServer msg) {
        if (!(msg instanceof SendAssistantMessage)) {
            //TODO match.sendIllegalMovement();
        } else {
            try {
                match.setAssistantOfCurrentPlayer(((SendAssistantMessage) msg).getAssistant());
            } catch (GameException e) {
                //TODO match.sendIllegalMovement();
            }

        }
    }


}
