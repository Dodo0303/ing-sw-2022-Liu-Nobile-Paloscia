package it.polimi.ingsw.Network.Messages.toClient.CharacterPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.Phase;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Exceptions.EmptyCloudException;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Model.Character.CharacterCard;
import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.util.ArrayList;
import java.util.HashMap;

public class IslandChosenMessage extends MessageToClient {
    private CharacterCard characterUpdated;
    private HashMap<Integer, Island> islands;
    private ArrayList<Player> playersUpdated;

    public IslandChosenMessage(HashMap<Integer, Island> islands, CharacterCard characterUpdated, ArrayList<Player> playersUpdated) {
        this.characterUpdated = characterUpdated;
        this.islands = islands;
        this.playersUpdated = playersUpdated;
    }

    public HashMap<Integer, Island> getIslands() {
        return islands;
    }

    @Override
    public void process(ServerHandler client) throws FullTableException, InterruptedException, EmptyCloudException {
        CLI cliClient = (CLI) client.getClient();
        cliClient.getGame().updateCharacterById(characterUpdated);
        cliClient.getGame().set_islands(islands);
        cliClient.getGame().set_players(playersUpdated);
        if (cliClient.getCurrPhase().equals(Phase.Character3)) {
            cliClient.setPhase(cliClient.getPrevPhase());
        }
    }

    public void processGUI(ServerHandler client) throws FullTableException, InterruptedException, EmptyCloudException {
        GUI guiClient = (GUI) client.getClient();
        guiClient.getGame().updateCharacterById(characterUpdated);
        guiClient.getGame().set_islands(islands);
        guiClient.getGame().set_players(playersUpdated);
        if (guiClient.getCurrPhase().equals(Phase.Character3)) {
            guiClient.setCurrPhase(guiClient.getPrevPhase());
            guiClient.viewSchoolBoard("", false);
        }
    }
}
