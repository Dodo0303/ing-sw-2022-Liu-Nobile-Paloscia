package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class ConfirmMovementFromEntranceMessage extends MessageToClient {

    private final StudentColor studentPosition;
    private final String playerID;
    private final int destination;
    private final int destinationID;

    public ConfirmMovementFromEntranceMessage(StudentColor studentPosition, String playerID, int destination, int destinationID) {
        this.studentPosition = studentPosition;
        this.playerID = playerID;
        this.destination = destination;
        this.destinationID = destinationID;
    }

    public StudentColor getStudentPosition() {
        return studentPosition;
    }

    public String getPlayerID() {
        return playerID;
    }

    public int getDestination() {
        return destination;
    }

    public int getDestinationID() {
        return destinationID;
    }

    @Override
    public void process(ServerHandler client) throws FullTableException {
        if (client.getClient().getCurrPhase().equals(Phase.Action1)) {
            if (destination == 0) {
                client.getClient().getGame().removeStudentFromEntrance(client.getClient().getGame().getPlayers().get(client.getClient().getGame().getPlayerIndexFromNickname(client.getClient().getNickname())), studentPosition);
                client.getClient().getGame().addToDiningTable(client.getClient().getGame().getPlayers().get(client.getClient().getGame().getPlayerIndexFromNickname(client.getClient().getNickname())), studentPosition);
            } else if (destination == 1) {
                client.getClient().getGame().removeStudentFromEntrance(client.getClient().getGame().getPlayers().get(client.getClient().getGame().getPlayerIndexFromNickname(client.getClient().getNickname())), studentPosition);
                client.getClient().getGame().addStudentToIsland(studentPosition, client.getClient().getGame().getIslands().get(destinationID));
            }
        }

    }
}
