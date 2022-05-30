package it.polimi.ingsw.Network.Messages.toClient.CharacterPhase;

import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Exceptions.EmptyCloudException;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class CharacterUsedMessage extends MessageToClient {
    private String playerID;
    private int characterID;

    public CharacterUsedMessage(String playerID, int characterID) {
        this.playerID = playerID;
        this.characterID = characterID;
    }

    @Override
    public void process(ServerHandler client) {

    }

    public void processGUI(it.polimi.ingsw.Client.GUI.ServerHandler client) {

    }

    public String getPlayerID() {
        return playerID;
    }

    public int getCharacterID() {
        return characterID;
    }
}
