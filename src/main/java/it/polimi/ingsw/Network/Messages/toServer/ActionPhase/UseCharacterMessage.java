package it.polimi.ingsw.Network.Messages.toServer.ActionPhase;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class UseCharacterMessage extends MessageToServer {
    private int characterID;

    public UseCharacterMessage(int characterID) {
        this.characterID = characterID;
    }

    public int getCharacterID() {
        return characterID;
    }

    @Override
    public void process(ClientHandler ch) {

    }
}
