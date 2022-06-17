package it.polimi.ingsw.Network.Messages.toClient.CharacterPhase;

import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
import it.polimi.ingsw.Exceptions.EmptyCloudException;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Model.Character.CharacterCard;
import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        client.getClient().getGame().updateCharacterById(characterUpdated);
        client.getClient().getGame().set_islands(islands);
        client.getClient().getGame().set_players(playersUpdated);
        if (client.getClient().getCurrPhase().equals(Phase.Character3)) {
            client.getClient().setPhase(client.getClient().getPrevPhase());
        }
    }

    public void processGUI(it.polimi.ingsw.Client.GUI.ServerHandler client) throws FullTableException, InterruptedException, EmptyCloudException {
        client.getClient().getGame().updateCharacterById(characterUpdated);
        client.getClient().getGame().set_islands(islands);
        client.getClient().getGame().set_players(playersUpdated);
        if (client.getClient().getCurrPhase().equals(Phase_GUI.Character3)) {
            client.getClient().setCurrPhase(client.getClient().getPrevPhase());
            client.getClient().viewSchoolBoard("", false);
        }
    }
}
