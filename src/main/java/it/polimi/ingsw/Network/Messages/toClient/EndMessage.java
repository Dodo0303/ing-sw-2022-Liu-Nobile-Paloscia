package it.polimi.ingsw.Network.Messages.toClient;

import it.polimi.ingsw.Client.CLI.ServerHandler;

/**
 *  winnerID = -1 indicates a draw.
 */
public class EndMessage extends MessageToClient{
    int winnerID;
    String reason;

    public EndMessage(int winnerID, String reason) {
        this.winnerID = winnerID;
        this.reason = reason;
    }

    @Override
    public void process(ServerHandler client) {
        if (winnerID == -1) {
            System.out.print("Game ended with a draw.\nGood bye.\n");
        } else {
            System.out.print("Player " + winnerID + " has won the game.\n");
        }
        client.getClient().setClosed(true);
        client.shutdown();
    }

    public void processGUI(it.polimi.ingsw.Client.GUI.ServerHandler client) {
        //todo
        client.shutdown();
    }
}
