package it.polimi.ingsw.Network.Messages.toClient.CharacterPhase;

import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
import it.polimi.ingsw.Exceptions.EmptyCloudException;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Model.Character.CharacterCard;
import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class NoEntryMovedMessage extends MessageToClient {
    private int islandID;
    private CharacterCard characterUpdated;

    public NoEntryMovedMessage(int islandID, CharacterCard characterUpdated) {
        this.islandID = islandID;
        this.characterUpdated = characterUpdated;
    }

    public int getIslandID() {
        return islandID;
    }

    public CharacterCard getCharacterUpdated() {
        return characterUpdated;
    }

    @Override
    public void process(ServerHandler client) throws FullTableException, InterruptedException, EmptyCloudException {
        client.getClient().getGame().updateCharacterById(characterUpdated);
        client.getClient().getGame().addNoEntry(client.getClient().getGame().getIslands().get(islandID));
        if (client.getClient().getCurrPhase().equals(Phase.Character5)) {
            client.getClient().setPhase(client.getClient().getPrevPhase());
            client.getClient().setCurrCharacter(-1);
        }
    }

    public void processGUI(it.polimi.ingsw.Client.GUI.ServerHandler client) throws FullTableException, InterruptedException, EmptyCloudException {
        client.getClient().getGame().updateCharacterById(characterUpdated);
        client.getClient().getGame().addNoEntry(client.getClient().getGame().getIslands().get(islandID));
        if (client.getClient().getCurrPhase().equals(Phase_GUI.Character5)) {
            client.getClient().setCurrPhase(client.getClient().getPrevPhase());
            client.getClient().setCurrCharacter(-1);
            client.getClient().viewSchoolBoard("", false);
        }
    }
}
