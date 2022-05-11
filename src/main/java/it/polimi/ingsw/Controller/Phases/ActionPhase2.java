package it.polimi.ingsw.Controller.Phases;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Controller.MatchController;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.MoveMotherNatureMessage;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.UseCharacterMessage;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class ActionPhase2 extends Phase {
    public ActionPhase2(MatchController match) {
        super(match);
    }

    @Override
    public void process(MessageToServer msg, ClientHandler ch) {
        //TODO: Check character rules: can I use 2 character same turn?
        if (!(msg instanceof MoveMotherNatureMessage || msg instanceof UseCharacterMessage)) {
            match.denyMovement(ch);
        } else if (msg instanceof MoveMotherNatureMessage) {
            try {
                match.moveMotherNature(((MoveMotherNatureMessage) msg).getIslandIndex());
            } catch (GameException e) {
                e.printStackTrace();
                match.denyMovement(ch);
                return;
            }
            match.broadcastMovement(((MoveMotherNatureMessage) msg).getIslandIndex());

            if (match.hasWinner()) {
                //TODO: Handle end of match
            } else {
                this.nextPhase();
            }

        } else {
            //TODO: Character handling
        }
    }

    @Override
    public void nextPhase() {
        match.setGamePhase(new ActionPhase3(this.match));
    }

    @Override
    public String toString() {
        return "ActionPhase2";
    }
}
