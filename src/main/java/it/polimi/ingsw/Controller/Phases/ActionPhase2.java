package it.polimi.ingsw.Controller.Phases;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Controller.MatchController;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.StudentColor;
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

            if (match.endedAtPhase2() == 1) {
                match.endGame("Player built all towers.");
            } else if (match.endedAtPhase2() == 2) {
                match.endGame("Only three or less islands remain.");
            } else {
                this.nextPhase();
            }

        } else {
            //Message is UseCharacterMessage
            if(match.isCharacterAvailable(((UseCharacterMessage) msg).getCharacterID(), ch.getNickname())){
                int id = ((UseCharacterMessage) msg).getCharacterID();
                if (id ==  2 || id == 4 || id == 6 || id == 8) {
                    //The character doesn't need any other message
                    new CharacterPhase(match, this, id).process(msg, ch);
                } else {
                    match.setGamePhase(new CharacterPhase(match, this, id));
                }

            } else {
                match.denyMovement(ch);
            }
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
