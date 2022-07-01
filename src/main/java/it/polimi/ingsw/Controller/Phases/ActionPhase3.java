package it.polimi.ingsw.Controller.Phases;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Controller.MatchController;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.ChooseCloudMessage;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.UseCharacterMessage;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

/**
 * Last phase of the Action phase. The player must choose a valid cloud.
 * He can also use a character card, if the game is in expert mode.
 */
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
            } catch (GameException | IndexOutOfBoundsException e) {
                e.printStackTrace();
                match.denyMovement(ch);
                return;
            }
            match.broadCastCloudChoice(((ChooseCloudMessage) msg).getCloudID());

            match.nextTurn();
            if (match.getCurrentPlayerID().equals(match.getFirstOfTurn()) && (match.noMoreStudents() || match.noMoreAssistants())) {
                if (match.noMoreStudents()) match.endGame("There are no students in the bag.");
                else match.endGame("All assistants were played");
            }
            this.nextPhase();
            match.broadcastTurnChange(match.getCurrentPlayerID(), match.getGamePhase().toString());
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
        match.resetCharacterAttributes();
        if (match.getCurrentPlayerID().equals(match.getFirstOfTurn()))
            match.setGamePhase(new PlanningPhase(this.match));
        else
            match.setGamePhase(new ActionPhase1(this.match));
    }

    @Override
    public String toString() {
        return "ActionPhase3";
    }
}
