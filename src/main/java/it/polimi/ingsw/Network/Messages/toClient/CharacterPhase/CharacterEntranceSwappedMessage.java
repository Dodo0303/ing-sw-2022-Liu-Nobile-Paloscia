package it.polimi.ingsw.Network.Messages.toClient.CharacterPhase;

import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Exceptions.EmptyCloudException;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Model.Character.CharacterCard;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.util.List;

public class CharacterEntranceSwappedMessage extends MessageToClient {
    private String playerID;
    private List<StudentColor> entranceUpdated;
    private CharacterCard characterUpdated;

    public CharacterEntranceSwappedMessage(String playerID, List<StudentColor> entranceUpdated, CharacterCard characterUpdated) {
        this.playerID = playerID;
        this.entranceUpdated = entranceUpdated;
        this.characterUpdated = characterUpdated;
    }

    public String getPlayerID() {
        return playerID;
    }

    public List<StudentColor> getEntranceUpdated() {
        return entranceUpdated;
    }

    public CharacterCard getCharacterUpdated() {
        return characterUpdated;
    }

    @Override
    public void process(ServerHandler client) throws FullTableException, InterruptedException, EmptyCloudException {

    }
}
