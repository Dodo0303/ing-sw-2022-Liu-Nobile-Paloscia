package it.polimi.ingsw.Network.Messages.toClient.CharacterPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.Phase;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Exceptions.EmptyCloudException;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Model.Character.CharacterCard;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class NoEntryMovedMessage extends MessageToClient {
    private int islandID;
    private CharacterCard characterUpdated;
    private boolean toIsland;

    public NoEntryMovedMessage(int islandID, CharacterCard characterUpdated, boolean toIsland) {
        this.islandID = islandID;
        this.characterUpdated = characterUpdated;
        this.toIsland = toIsland;
    }

    public int getIslandID() {
        return islandID;
    }

    public CharacterCard getCharacterUpdated() {
        return characterUpdated;
    }

    @Override
    public void process(ServerHandler client) throws FullTableException, InterruptedException, EmptyCloudException {
        CLI cliClient = (CLI) client.getClient();
        System.out.println("Processing message");
        cliClient.getGame().updateCharacterById(characterUpdated);
        if (toIsland)
            cliClient.getGame().addNoEntry(cliClient.getGame().getIslands().get(islandID));
        if (cliClient.getCurrPhase().equals(Phase.Character5)) {
            cliClient.setPhase(cliClient.getPrevPhase());
            cliClient.setCurrCharacter(-1);
        }
    }

    public void processGUI(ServerHandler client) throws FullTableException, InterruptedException, EmptyCloudException {
        GUI guiClient = (GUI) client.getClient();
        guiClient.getGame().updateCharacterById(characterUpdated);
        if (toIsland)
            guiClient.getGame().addNoEntry(guiClient.getGame().getIslands().get(islandID));
        if (guiClient.getCurrPhase().equals(Phase.Character5)) {
            guiClient.setCurrPhase(guiClient.getPrevPhase());
            guiClient.setCurrCharacter(-1);
            guiClient.viewSchoolBoard("", false);
        }
    }
}
