package it.polimi.ingsw.Network.Messages.toServer.CharacterPhase;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class SwapStudentsCharacterEntranceMessage extends MessageToServer {
    private int[] characterIndexes;
    private int[] entranceIndexes;

    public SwapStudentsCharacterEntranceMessage(int[] characterIndexes, int[] entranceIndexes) {
        this.characterIndexes = characterIndexes;
        this.entranceIndexes = entranceIndexes;
    }

    public int[] getCharacterIndexes() {
        return characterIndexes;
    }

    public int[] getEntranceIndexes() {
        return entranceIndexes;
    }

    @Override
    public void process(ClientHandler ch) {

    }
}
