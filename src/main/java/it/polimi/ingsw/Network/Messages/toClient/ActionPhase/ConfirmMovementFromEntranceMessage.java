package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class ConfirmMovementFromEntranceMessage extends MessageToClient {

    private final int studentPosition;
    private final String playerID;
    private final int destination;
    private final int destinationID;

    public ConfirmMovementFromEntranceMessage(int studentPosition, String playerID, int destination, int destinationID) {
        this.studentPosition = studentPosition;
        this.playerID = playerID;
        this.destination = destination;
        this.destinationID = destinationID;
    }

    public int getStudentPosition() {
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
    public void process(ServerHandler client) {
        if (destination == 0) {
            StudentColor color = client.getClient().getGame().removeStudentFromEntrance(client.getClient().getGame().getPlayers().get(client.getClient().getGame().getPlayerIndexFromNickname(playerID)), studentPosition);
            try {
                client.getClient().getGame().addToDiningTable(client.getClient().getGame().getPlayers().get(client.getClient().getGame().getPlayerIndexFromNickname(playerID)), color);
            } catch (FullTableException e) {
                e.printStackTrace();
            }
        } else if (destination == 1) {
            StudentColor color = client.getClient().getGame().removeStudentFromEntrance(client.getClient().getGame().getPlayers().get(client.getClient().getGame().getPlayerIndexFromNickname(playerID)), studentPosition);
            client.getClient().getGame().addStudentToIsland(color, client.getClient().getGame().getIslands().get(destinationID));
        }
    }
}
