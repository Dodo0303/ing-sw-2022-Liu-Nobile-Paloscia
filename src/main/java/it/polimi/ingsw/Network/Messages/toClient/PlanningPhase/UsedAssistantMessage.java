package it.polimi.ingsw.Network.Messages.toClient.PlanningPhase;

import it.polimi.ingsw.Client.CLI.ServerHandler;
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
        ch.getClient().getGame().setAssistantOfPlayer(playerID, ch.getClient().getGame().getPlayerByNickname(playerID).getAssistants().get(assistantValue - 1));
        System.out.println(playerID + " has chosen " + assistantValue + "nd assistant.");
    }

    public void processGUI(it.polimi.ingsw.Client.GUI.ServerHandler ch) {
        ch.getClient().getGame().setAssistantOfPlayer(playerID, ch.getClient().getGame().getPlayerByNickname(playerID).getAssistants().get(assistantValue - 1));
        ch.getClient().getAssistantPlayer().put(assistantValue, playerID);
    }
}
