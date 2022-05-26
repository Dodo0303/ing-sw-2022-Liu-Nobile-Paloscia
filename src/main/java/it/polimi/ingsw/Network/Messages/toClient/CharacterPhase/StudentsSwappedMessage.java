package it.polimi.ingsw.Network.Messages.toClient.CharacterPhase;

import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Exceptions.EmptyCloudException;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.util.List;

public class StudentsSwappedMessage extends MessageToClient {
    private String playerID;
    private StudentColor[] tableColors;
    private List<StudentColor> entranceUpdated;

    public StudentsSwappedMessage(String playerID, StudentColor[] tableColor, List<StudentColor> entranceUpdated) {
        this.playerID = playerID;
        this.tableColors = tableColor;
        this.entranceUpdated = entranceUpdated;
    }

    public String getPlayerID() {
        return playerID;
    }

    public StudentColor[] getTableColors() {
        return tableColors;
    }

    public List<StudentColor> getEntranceUpdated() {
        return entranceUpdated;
    }

    @Override
    public void process(ServerHandler client) throws FullTableException, InterruptedException, EmptyCloudException {

    }
}
