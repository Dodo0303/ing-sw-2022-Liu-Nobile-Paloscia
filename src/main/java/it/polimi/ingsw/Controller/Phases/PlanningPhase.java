package it.polimi.ingsw.Controller.Phases;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Controller.MatchController;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Network.Messages.toClient.PlanningPhase.CloudsUpdateMessage;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;
import it.polimi.ingsw.Network.Messages.toServer.PlanningPhase.SendAssistantMessage;

import java.util.ArrayList;

public class PlanningPhase extends Phase {

    public PlanningPhase(MatchController match) {
        super(match);

        this.match.fillClouds();
        this.match.broadcastClouds();

    }

    @Override
    public void process(MessageToServer msg, ClientHandler ch) {
        if (!(msg instanceof SendAssistantMessage)) {
            match.denyMovement(ch);
        } else {
            try {
                match.setAssistantOfCurrentPlayer(((SendAssistantMessage) msg).getAssistant());
            } catch (GameException e) {
                match.denyMovement(ch);
                return;
            }

            match.broadcastAssistant(match.getCurrentPlayerID(), ((SendAssistantMessage) msg).getAssistant().getValue());
            match.nextTurn();
        }
    }

    @Override
    public void nextPhase() {
        match.setGamePhase(new ActionPhase1(this.match));
    }

    @Override
    public String toString() {
        return "PlanningPhase";
    }
}
