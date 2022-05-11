package it.polimi.ingsw.Controller.Phases;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Controller.MatchController;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Network.Messages.toClient.ActionPhase.ChangeTurnMessage;
import it.polimi.ingsw.Network.Messages.toClient.ActionPhase.ConfirmMovementMessage;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.ChooseCloudMessage;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.MoveStudentFromEntranceMessage;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

import java.io.IOException;

public class ActionPhase1 extends Phase {

    private int moves;

    public ActionPhase1(MatchController match) {
        super(match);
        this.moves = 0;
    }

    @Override
    public void process(MessageToServer msg, ClientHandler ch) {
        if (!(msg instanceof MoveStudentFromEntranceMessage || msg instanceof ChooseCloudMessage)) {
            match.denyMovement(ch);
        } else if (msg instanceof MoveStudentFromEntranceMessage) {
            switch (((MoveStudentFromEntranceMessage) msg).getDestination()) {

                // Move to dining room
                case 0:
                    try {
                        match.moveStudentToDiningRoom(((MoveStudentFromEntranceMessage) msg).getStudent());
                    } catch (FullTableException fte) {
                        match.denyMovement(ch);
                        return;
                    }
                    this.moves++;
                    //TODO: Should be broadcast

                // Move to island
                case 1:
                    match.moveStudentToIsland(((MoveStudentFromEntranceMessage) msg).getDestinationID(), ((MoveStudentFromEntranceMessage) msg).getStudent());
                    this.moves++;

                default:
                    match.denyMovement(ch);
            }
        }

        //TODO: Check whether professor should be moved

        if (this.moves == 3) {
            this.nextPhase();
        }
    }

    @Override
    public void nextPhase() {
        match.setGamePhase(new ActionPhase2(this.match));
    }

    @Override
    public String toString() {
        return "ActionPhase1";
    }
}
