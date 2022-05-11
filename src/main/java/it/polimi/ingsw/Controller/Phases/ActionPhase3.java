package it.polimi.ingsw.Controller.Phases;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Controller.MatchController;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.ChooseCloudMessage;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.UseCharacterMessage;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class ActionPhase3 extends Phase {
    public ActionPhase3(MatchController match) {
        super(match);
    }

    @Override
    public void process(MessageToServer msg, ClientHandler ch) {
        if (!(msg instanceof ChooseCloudMessage || msg instanceof UseCharacterMessage)) {
            match.denyMovement(ch);
        } else if (msg instanceof ChooseCloudMessage) {
            try {
                match.takeStudentsFromCloud(((ChooseCloudMessage) msg).getCloudID());
            } catch (GameException e) {
                e.printStackTrace();
                match.denyMovement(ch);
                return;
            }
            match.broadCastCloudChoice(((ChooseCloudMessage) msg).getCloudID());

            match.nextTurn();
            if (match.getCurrentPlayerID().equals(match.getFirstOfTurn()) && (match.noMoreStudents() || match.noMoreAssistants())) {
                if (match.noMoreStudents()) match.endGame("There are no students in the bag.");
                else if (match.noMoreAssistants()) match.endGame("All assistants were played");
            } else {
                this.nextPhase();
            }
        } else {
            //TODO: Character handling
        }
    }

    @Override
    public void nextPhase() {
        match.setGamePhase(new PlanningPhase(this.match));
    }

    @Override
    public String toString() {
        return "ActionPhase3";
    }
}
