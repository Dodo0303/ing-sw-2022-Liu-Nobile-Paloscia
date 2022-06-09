package it.polimi.ingsw.Network.Messages.toClient.CharacterPhase;

import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Exceptions.EmptyCloudException;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.util.List;

public class EntranceTableSwappedMessage extends MessageToClient {
    private String playerID;
    private Player playerUpdated;

    public EntranceTableSwappedMessage(String playerID, Player playerUpdated) {
        this.playerID = playerID;
        this.playerUpdated = playerUpdated;
    }

    public String getPlayerID() {
        return playerID;
    }

    @Override
    public void process(ServerHandler client) throws FullTableException, InterruptedException, EmptyCloudException {
        for (int i = 0; i < client.getClient().getGame().getPlayers().size(); i++) {
            if (client.getClient().getGame().getPlayers().get(i).getNickName().equals(playerID)) {
                client.getClient().getGame().getPlayers().set(i, playerUpdated);
            }
        }
    }

    public void processGUI(it.polimi.ingsw.Client.GUI.ServerHandler client) throws FullTableException, InterruptedException, EmptyCloudException {
        for (int i = 0; i < client.getClient().getGame().getPlayers().size(); i++) {
            if (client.getClient().getGame().getPlayers().get(i).getNickName().equals(playerID)) {
                client.getClient().getGame().getPlayers().set(i, playerUpdated);
            }
        }
    }
}
