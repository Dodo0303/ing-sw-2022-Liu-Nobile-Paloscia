package it.polimi.ingsw.Network.Messages.toClient.CharacterPhase;

import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Exceptions.EmptyCloudException;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Model.Character.CharacterCard;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class StudentMovedToTableMessage extends MessageToClient {
    private String playerID;
    private StudentColor tableColor;
    private CharacterCard characterUpdated;

    public StudentMovedToTableMessage(String playerID, StudentColor tableColor, CharacterCard characterUpdated) {
        this.playerID = playerID;
        this.tableColor = tableColor;
        this.characterUpdated = characterUpdated;
    }

    public String getPlayerID() {
        return playerID;
    }

    public StudentColor getTableColor() {
        return tableColor;
    }

    public CharacterCard getCharacterUpdated() {
        return characterUpdated;
    }

    @Override
    public void process(ServerHandler client) throws FullTableException, InterruptedException, EmptyCloudException {
        client.getClient().getGame().getPlayerByNickname(playerID).addToDiningTable(tableColor);
        client.getClient().getGame().updateCharacterById(characterUpdated);
    }
}
