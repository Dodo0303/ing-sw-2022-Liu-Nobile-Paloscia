package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.ServerHandler;
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
        CLI cliClient = (CLI) client.getClient();
        if (destination == 0) {
            StudentColor color = cliClient.getGame().removeStudentFromEntrance(cliClient.getGame().getPlayers().get(cliClient.getGame().getPlayerIndexFromNickname(playerID)), studentPosition);
            try {
                cliClient.getGame().addToDiningTable(cliClient.getGame().getPlayers().get(cliClient.getGame().getPlayerIndexFromNickname(playerID)), color);
                cliClient.getGame().getPlayerByNickname(playerID).setCoins(coin);
                if (!playerID.equals(cliClient.getNickname())) {
                    System.out.println(playerID + " moved a student to his dining table.");
                    cliClient.getView().printSchoolBoard(cliClient.getGame().getPlayerByNickname(playerID));
                }
            } catch (FullTableException e) {
                e.printStackTrace();
            }
        } else if (destination == 1) {
            StudentColor color = cliClient.getGame().removeStudentFromEntrance(cliClient.getGame().getPlayers().get(cliClient.getGame().getPlayerIndexFromNickname(playerID)), studentPosition);
            cliClient.getGame().addStudentToIsland(color, cliClient.getGame().getIslands().get(destinationID));
            if (!playerID.equals(cliClient.getNickname())) {
                System.out.println(playerID + " moved a student to the following island.");
                cliClient.getView().printIsland(destinationID);
            }
        }
        cliClient.setAp1Moves(cliClient.getAp1Moves() + 1);
        if (cliClient.getAp1Moves() == ((cliClient.getGame().getPlayers().size() == 3) ? 4 : 3)) {
            cliClient.setPhase(Phase.Action2);
            cliClient.setAp1Moves(0);
            cliClient.moveMotherNature();
        } else {
            cliClient.moveStudentsFromEntrance();
        }
    }

    public void processGUI(ServerHandler client) {
        GUI guiClient = (GUI) client.getClient();
        if (destination == 0) {
            StudentColor color = guiClient.getGame().removeStudentFromEntrance(guiClient.getGame().getPlayers().get(guiClient.getGame().getPlayerIndexFromNickname(playerID)), studentPosition);
            try {
                guiClient.getGame().addToDiningTable(guiClient.getGame().getPlayers().get(guiClient.getGame().getPlayerIndexFromNickname(playerID)), color);
                guiClient.getGame().getPlayerByNickname(playerID).setCoins(coin);
            } catch (FullTableException e) {
                e.printStackTrace();
            }
        } else if (destination == 1) {
            StudentColor color = guiClient.getGame().removeStudentFromEntrance(guiClient.getGame().getPlayers().get(guiClient.getGame().getPlayerIndexFromNickname(playerID)), studentPosition);
            guiClient.getGame().addStudentToIsland(color, guiClient.getGame().getIslands().get(destinationID));
        }
    }
}
