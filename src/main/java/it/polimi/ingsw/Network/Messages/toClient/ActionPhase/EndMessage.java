package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class EndMessage extends MessageToClient {

    private final String winnerID;
    private final String reason;

    public EndMessage(String winnerID, String reason) {
        this.winnerID = winnerID;
        this.reason = reason;
    }

    public String getWinnerID() {
        return winnerID;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public void process(ServerHandler client) {
        client.getClient().setPhase(Phase.Ending);
        if (winnerID == null) {
            System.out.print("Game ended with a draw.\nGood bye.\n");
        } else {
            System.out.print("Player " + winnerID + " won the game. For" + reason);
            System.out.print("\nGood bye.\n");
        }
        System.exit(0);
    }

    public void processGUI(it.polimi.ingsw.Client.GUI.ServerHandler client) {
        client.getClient().setCurrPhase(Phase_GUI.Ending);
        if (winnerID == null) {
            client.getClient().gameOver("Game ended with a draw.\nGood bye.\n");
        } else {
            if (client.getClient().getGame().getPlayers().size() == 4) {
                String teamColor = client.getClient().getGame().getPlayerByNickname(winnerID).getColor().toString();
                client.getClient().gameOver("Team " + teamColor + " won the game, for" + reason);
            } else {
                client.getClient().gameOver("Player " + winnerID + " won the game, for" + reason);
            }

        }
    }
}
