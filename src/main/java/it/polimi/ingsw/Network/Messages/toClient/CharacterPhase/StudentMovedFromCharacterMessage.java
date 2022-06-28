package it.polimi.ingsw.Network.Messages.toClient.CharacterPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
import it.polimi.ingsw.Exceptions.EmptyCloudException;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Model.Character.CharacterCard;
import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.util.HashMap;

public class StudentMovedFromCharacterMessage extends MessageToClient {
    private CharacterCard characterUpdated;
    private HashMap<Integer, Island> islands;

    public StudentMovedFromCharacterMessage(CharacterCard characterUpdated, HashMap<Integer, Island> islands) {
        this.characterUpdated = characterUpdated;
        this.islands = islands;
    }

    public CharacterCard getCharacterUpdated() {
        return characterUpdated;
    }

    public HashMap<Integer, Island> getIslands() {
        return islands;
    }

    @Override
    public void process(ServerHandler client) throws FullTableException, InterruptedException, EmptyCloudException {
        CLI cliClient = (CLI) client.getClient();
        cliClient.getGame().updateCharacterById(characterUpdated);
        cliClient.getGame().set_islands(islands);
        if (cliClient.getCurrPhase().equals(Phase.Character1)) {
            cliClient.setPhase(cliClient.getPrevPhase());
        }
        cliClient.getGame().getPlayerByNickname(cliClient.getNickname()).setCoins(cliClient.getGame().getPlayerByNickname(cliClient.getNickname()).getCoins() - 1);
    }

    public void processGUI(ServerHandler client) throws FullTableException, InterruptedException, EmptyCloudException {
        GUI guiClient = (GUI) client.getClient();
        guiClient.getGame().updateCharacterById(characterUpdated);
        guiClient.getGame().set_islands(islands);
        if (guiClient.getCurrPhase().equals(Phase_GUI.Character1)) {
            guiClient.setCurrPhase(guiClient.getPrevPhase());
        }
        guiClient.getGame().getPlayerByNickname(guiClient.getNickname()).setCoins(guiClient.getGame().getPlayerByNickname(guiClient.getNickname()).getCoins() - 1);
        guiClient.viewSchoolBoard("Character used", false);
    }
}
