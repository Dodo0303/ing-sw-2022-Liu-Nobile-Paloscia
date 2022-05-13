package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;
import it.polimi.ingsw.Utilities;

public class ChangeTurnMessage extends MessageToClient {
    //TODO
    private String playerNickname;
    private Phase gamePhase;

    public ChangeTurnMessage(String playerNickname, String gamePhase) {//TODO Phase gamePhase
        this.playerNickname = playerNickname;
        this.gamePhase = Utilities.getPhase(gamePhase);
    }

    public String getPlayerID() {
        return this.playerNickname;
    }

    public Phase getGamePhase() {
        return this.gamePhase;
    }

    @Override
    public void process(ServerHandler ch) {
        if (this.playerNickname.equals(ch.getClient().getNickname())) {
            if (ch.getClient().getCurrPhase().equals(Phase.GameJoined) && this.gamePhase.equals(Phase.Planning)) {
                ch.getClient().setPhase(Phase.Planning);
                ch.getClient().playAssistant();
            } else if (ch.getClient().getCurrPhase().equals(Phase.Planning) && this.gamePhase.equals(Phase.Action1)) {

            } else if (ch.getClient().getCurrPhase().equals(Phase.Action3) && this.gamePhase.equals(Phase.Planning)) {

            }
            System.out.println(this.getClass().toString() + " processed.");
        }
    }

    public Object getPhase() {
        return this.gamePhase;
    }
}
