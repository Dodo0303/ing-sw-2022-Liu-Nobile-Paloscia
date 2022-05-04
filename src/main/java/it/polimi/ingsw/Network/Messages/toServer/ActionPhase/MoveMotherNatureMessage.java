package it.polimi.ingsw.Network.Messages.toServer.ActionPhase;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Controller.MatchController;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class MoveMotherNatureMessage extends MessageToServer {
    private int islandIndex;//TODO NOT numOfJumps. The method takes as argument the index of the chosen island.

    public MoveMotherNatureMessage(int islandIndex) {
        this.islandIndex = islandIndex;
    }

    public int getNumOfJumps() {
        return islandIndex;
    }

    @Override
    public void process(ClientHandler ch) {
        MatchController match = ch.getMatch();
        //TODO toClient message
        Player player = match.getGame().getPlayers().get(ch.getPlayerID());
        try {
            match.moveMotherNature(islandIndex, player);
            //TODO DenyMessage
        } catch (GameException e) {
            //TODO ConfirmMessage
        }
    }
}