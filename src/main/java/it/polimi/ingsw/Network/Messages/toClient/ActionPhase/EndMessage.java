package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.Phase;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.ServerHandler;
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
        CLI cliClient = (CLI) client.getClient();
        cliClient.setPhase(Phase.Ending);
        if (winnerID == null) {
            System.out.print("Game ended with a draw.\nGood bye.\n");
        } else {
            System.out.print("Player " + winnerID + " won the game. For " + reason);
            System.out.print("\nGood bye.\n");
        }
        System.exit(0);
    }

    public void processGUI(ServerHandler client) {
        GUI guiClient = (GUI) client.getClient();
        guiClient.setCurrPhase(Phase.Ending);
        if (winnerID == null) {
            guiClient.gameOver("Game ended with a draw.\nGood bye.\n");
        } else {
            if (guiClient.getGame().getPlayers().size() == 4) {
                String teamColor = guiClient.getGame().getPlayerByNickname(winnerID).getColor().toString();
                guiClient.gameOver("Team " + teamColor + " won the game: " + reason);
            } else {
                guiClient.gameOver("Player " + winnerID + " won the game: " + reason);
            }

        }
    }
}
