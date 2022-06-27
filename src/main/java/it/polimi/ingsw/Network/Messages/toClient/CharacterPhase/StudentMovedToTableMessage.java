package it.polimi.ingsw.Network.Messages.toClient.CharacterPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
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
        CLI cliClient = (CLI) client.getClient();
        cliClient.getGame().getPlayerByNickname(playerID).addToDiningTable(tableColor);
        cliClient.getGame().updateCharacterById(characterUpdated);
        if (cliClient.getCurrPhase().equals(Phase.Character11)) {
            cliClient.setPhase(cliClient.getPrevPhase());
            cliClient.getGame().getPlayerByNickname(cliClient.getNickname()).setCoins(cliClient.getGame().getPlayerByNickname(cliClient.getNickname()).getCoins() - 2);
        }
    }

    public void processGUI(ServerHandler client) throws FullTableException, InterruptedException, EmptyCloudException {
        GUI guiClient = (GUI) client.getClient();
        guiClient.getGame().getPlayerByNickname(playerID).addToDiningTable(tableColor);
        guiClient.getGame().updateCharacterById(characterUpdated);
        if (guiClient.getCurrPhase().equals(Phase_GUI.Character11)) {
            guiClient.setCurrPhase(guiClient.getPrevPhase());
            guiClient.viewSchoolBoard("Student moved successfully", false);
            guiClient.getGame().getPlayerByNickname(guiClient.getNickname()).setCoins(guiClient.getGame().getPlayerByNickname(guiClient.getNickname()).getCoins() - 2);
        }
    }
}
