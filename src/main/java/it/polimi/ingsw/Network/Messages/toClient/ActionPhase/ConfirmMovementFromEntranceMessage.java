package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class ConfirmMovementFromEntranceMessage extends MessageToClient {

    private final int studentPosition;
    private final String playerID;
    private final int destination;
    private final int destinationID;
    private final int coin;

    public ConfirmMovementFromEntranceMessage(int studentPosition, String playerID, int destination, int destinationID, int coin) {
        this.studentPosition = studentPosition;
        this.playerID = playerID;
        this.destination = destination;
        this.destinationID = destinationID;
        this.coin = coin;
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
                client.getClient().getGame().getPlayerByNickname(playerID).setCoins(coin);
                if (!playerID.equals(client.getClient().getNickname())) {
                    System.out.println(playerID + " moved a student to his dining table.");
                    client.getClient().getView().printSchoolBoard(client.getClient().getGame().getPlayerByNickname(playerID));
                }
            } catch (FullTableException e) {
                e.printStackTrace();
            }
        } else if (destination == 1) {
            StudentColor color = client.getClient().getGame().removeStudentFromEntrance(client.getClient().getGame().getPlayers().get(client.getClient().getGame().getPlayerIndexFromNickname(playerID)), studentPosition);
            client.getClient().getGame().addStudentToIsland(color, client.getClient().getGame().getIslands().get(destinationID));
            if (!playerID.equals(client.getClient().getNickname())) {
                System.out.println(playerID + " moved a student to the following island.");
                client.getClient().getView().printIsland(destinationID);
            }
        }
        if (playerID.equals(client.getClient().getNickname())) {
            client.getClient().setAp1Moves(client.getClient().getAp1Moves() + 1);
            if (client.getClient().getAp1Moves() == ((client.getClient().getGame().getPlayers().size() == 3) ? 4 : 3)) {
                client.getClient().setPhase(Phase.Action2);
                client.getClient().setAp1Moves(0);
                client.getClient().moveMotherNature();
            } else {
                client.getClient().moveStudentsFromEntrance();
            }
        }
    }

    public void processGUI(it.polimi.ingsw.Client.GUI.ServerHandler client) {
        if (destination == 0) {
            StudentColor color = client.getClient().getGame().removeStudentFromEntrance(client.getClient().getGame().getPlayers().get(client.getClient().getGame().getPlayerIndexFromNickname(playerID)), studentPosition);
            try {
                client.getClient().getGame().addToDiningTable(client.getClient().getGame().getPlayers().get(client.getClient().getGame().getPlayerIndexFromNickname(playerID)), color);
                client.getClient().getGame().getPlayerByNickname(playerID).setCoins(coin);
            } catch (FullTableException e) {
                e.printStackTrace();
            }
        } else if (destination == 1) {
            StudentColor color = client.getClient().getGame().removeStudentFromEntrance(client.getClient().getGame().getPlayers().get(client.getClient().getGame().getPlayerIndexFromNickname(playerID)), studentPosition);
            client.getClient().getGame().addStudentToIsland(color, client.getClient().getGame().getIslands().get(destinationID));
        }
    }
}
