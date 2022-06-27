package it.polimi.ingsw.Network.Messages.toClient.CharacterPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Exceptions.EmptyCloudException;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

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
        CLI cliClient = (CLI) client.getClient();
        cliClient.setCurrCharacter(-1);
        for (int i = 0; i < cliClient.getGame().getPlayers().size(); i++) {
            if (cliClient.getGame().getPlayers().get(i).getNickName().equals(playerID)) {
                cliClient.getGame().getPlayers().set(i, playerUpdated);
            }
        }
        if (cliClient.getNickname().equals(playerID)) {
            cliClient.setPhase(cliClient.getPrevPhase());
        }
    }

    public void processGUI(ServerHandler client) throws FullTableException, InterruptedException, EmptyCloudException {
        GUI guiClient = (GUI) client.getClient();
        guiClient.setCurrCharacter(-1);
        for (int i = 0; i < guiClient.getGame().getPlayers().size(); i++) {
            if (guiClient.getGame().getPlayers().get(i).getNickName().equals(playerID)) {
                guiClient.getGame().getPlayers().set(i, playerUpdated);
            }
        }
        if (guiClient.getNickname().equals(playerID)) {
            guiClient.setCurrPhase(guiClient.getPrevPhase());
            guiClient.viewSchoolBoard("Students swapped", false);
        }
    }
}
