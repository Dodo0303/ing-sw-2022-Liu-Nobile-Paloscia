package it.polimi.ingsw.Controller.Phases;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Controller.MatchController;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.MoveStudentFromEntranceMessage;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.UseCharacterMessage;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class ActionPhase1 extends Phase {

    private int moves;

    public ActionPhase1(MatchController match) {
        super(match);
        this.moves = 0;
    }

    @Override
    public void process(MessageToServer msg, ClientHandler ch) {
        if (!(msg instanceof MoveStudentFromEntranceMessage || msg instanceof UseCharacterMessage)) {
            match.denyMovement(ch);
        } else if (msg instanceof MoveStudentFromEntranceMessage) {
            System.out.println(((MoveStudentFromEntranceMessage) msg).getStudent().toString() + ((MoveStudentFromEntranceMessage) msg).getDestination() + ((MoveStudentFromEntranceMessage) msg).getDestinationID()); //TODO DEL AFTER TEST
            switch (((MoveStudentFromEntranceMessage) msg).getDestination()) {

                // Move to dining room
                case 0:
                    try {
                        match.moveStudentToDiningRoom(((MoveStudentFromEntranceMessage) msg).getStudent());
                    } catch (FullTableException fte) {
                        fte.printStackTrace();
                        match.denyMovement(ch);
                        return;
                    }
                    this.moves++;
                    match.broadcastMovementFromEntrance(((MoveStudentFromEntranceMessage) msg).getStudent(),
                                                        match.getCurrentPlayerID(),
                                                        ((MoveStudentFromEntranceMessage) msg).getDestination(),
                                                        ((MoveStudentFromEntranceMessage) msg).getDestinationID());
                    break;

                // Move to island
                case 1:
                    try {
                        match.moveStudentToIsland(((MoveStudentFromEntranceMessage) msg).getDestinationID(), ((MoveStudentFromEntranceMessage) msg).getStudent());
                    } catch (IllegalArgumentException iae) {
                        iae.printStackTrace();
                        match.denyMovement(ch);
                        return;
                    }
                    this.moves++;
                    match.broadcastMovementFromEntrance(((MoveStudentFromEntranceMessage) msg).getStudent(),
                            match.getCurrentPlayerID(),
                            ((MoveStudentFromEntranceMessage) msg).getDestination(),
                            ((MoveStudentFromEntranceMessage) msg).getDestinationID());
                    break;

                default:
                    match.denyMovement(ch);
                    break;
            }

            if (this.moves == 3) {
                this.nextPhase();
            }

        } else {
            //TODO: Handle characters
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
