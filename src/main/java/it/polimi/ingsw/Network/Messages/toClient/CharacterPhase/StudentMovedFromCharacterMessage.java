package it.polimi.ingsw.Network.Messages.toClient.CharacterPhase;

import it.polimi.ingsw.Client.CLI.ServerHandler;
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

    }
}
