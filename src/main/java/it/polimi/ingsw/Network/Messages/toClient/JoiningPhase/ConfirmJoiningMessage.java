package it.polimi.ingsw.Network.Messages.toClient.JoiningPhase;

import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class ConfirmJoiningMessage extends MessageToClient {

    private boolean result;
    private String message;
    private int matchID;

    public ConfirmJoiningMessage(boolean result, String message, int matchID) {
        this.result = result;
        this.message = message;
        this.matchID = matchID;
    }

    public boolean isResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void process(ServerHandler ch) {
        if (result) {
            System.out.println("You've joined match " + matchID);
            if (ch.getClient().getCurrPhase().equals(Phase.ChoosingGameMode)) {
                ch.getClient().setPhase(Phase.GameJoined);
            } else {
                ch.getClient().setPhase(Phase.JoiningGame2);
            }
        } else {
            System.out.println(this.message);
            ch.getClient().setPhase(Phase.JoiningGame1);
            ch.getClient().joinGame();
        }
    }
}
