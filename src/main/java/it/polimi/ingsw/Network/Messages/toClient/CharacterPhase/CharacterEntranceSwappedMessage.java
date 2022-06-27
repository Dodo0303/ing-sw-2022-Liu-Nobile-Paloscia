package it.polimi.ingsw.Network.Messages.toClient.CharacterPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Exceptions.EmptyCloudException;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Model.Character.CharacterCard;
import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.Player;
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
        CLI cliClient = (CLI) client.getClient();
        GameModel game = cliClient.getGame();
        Player player = game.getPlayerByNickname(playerID);
        game.updateCharacterById(characterUpdated);
        player.clearEntrance();
        for (StudentColor color :
                entranceUpdated) {
            player.addStudentToEntrance(color);
        }
        if (cliClient.getNickname().equals(playerID)) {
            cliClient.setPhase(cliClient.getPrevPhase());
        }
    }

    public void processGUI(ServerHandler client) throws FullTableException, InterruptedException, EmptyCloudException {
        GUI guiClient = (GUI) client.getClient();
        GameModel game = guiClient.getGame();
        Player player = game.getPlayerByNickname(playerID);
        game.updateCharacterById(characterUpdated);
        player.clearEntrance();
        for (StudentColor color :
                entranceUpdated) {
            player.addStudentToEntrance(color);
        }
        if (guiClient.getNickname().equals(playerID)) {
            guiClient.setCurrPhase(guiClient.getPrevPhase());
        }
        guiClient.viewSchoolBoard("", false);
    }
}
