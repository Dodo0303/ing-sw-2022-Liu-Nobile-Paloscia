package it.polimi.ingsw.Network.Messages.toClient.PlanningPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class UsedAssistantMessage extends MessageToClient {
    private final String playerID;
    private final int assistantValue;

    public UsedAssistantMessage(String playerID, int assistantValue) {
        this.playerID = playerID;
        this.assistantValue = assistantValue;
    }

    public String getPlayerID() {
        return playerID;
    }

    public int getAssistantValue() {
        return assistantValue;
    }

    @Override
    public void process(ServerHandler ch) {
        CLI cliClient = (CLI) ch.getClient();
        cliClient.getGame().setAssistantOfPlayer(playerID, cliClient.getGame().getPlayerByNickname(playerID).getAssistants().get(assistantValue - 1));
        if (assistantValue == 1) {
            System.out.println(playerID + " has chosen " + assistantValue + "st assistant.");
        } else if (assistantValue == 2){
            System.out.println(playerID + " has chosen " + assistantValue + "nd assistant.");
        } else if (assistantValue == 3){
            System.out.println(playerID + " has chosen " + assistantValue + "rd assistant.");
        } else {
            System.out.println(playerID + " has chosen " + assistantValue + "th assistant.");
        }
        if (cliClient.getNickname().equals(playerID)) {
            cliClient.setAssistantPicked(true);
            cliClient.getAssistantPlayer().clear();
        }
        if (!cliClient.isAssistantPicked()){
            cliClient.getAssistantPlayer().add(assistantValue);
        }
    }

    public void processGUI(ServerHandler ch) {
        GUI guiClient = (GUI) ch.getClient();
        guiClient.getGame().setAssistantOfPlayer(playerID, guiClient.getGame().getPlayerByNickname(playerID).getAssistants().get(assistantValue - 1));
        if (!guiClient.isAssistantPicked()){
            guiClient.getAssistantPlayer().put(assistantValue, playerID);
        }
    }
}
