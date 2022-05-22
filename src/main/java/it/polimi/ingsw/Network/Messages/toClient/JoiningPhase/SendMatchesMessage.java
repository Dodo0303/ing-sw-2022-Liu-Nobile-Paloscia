package it.polimi.ingsw.Network.Messages.toClient.JoiningPhase;

import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.util.List;

public class SendMatchesMessage extends MessageToClient {
    private List<Integer> matchesID;
    private List<List<String>> players;

    public SendMatchesMessage(List<Integer> matchesID, List<List<String>> players) {
        this.matchesID = matchesID;
        this.players = players;
    }

    public List<Integer> getMatchesID() {
        return matchesID;
    }

    public List<String> getPlayersByMatchId(int id) {
        return players.get(id);
    }

    public List<List<String>> getPlayers() {
        return players;
    }

    @Override
    public void process(ServerHandler client) {
        if (matchesID.isEmpty()) {
            System.out.println("There is no available match.");
            client.getClient().setPhase(Phase.ChoosingGameMode);
            client.getClient().chooseGameMode();
        } else {
            System.out.println("Available matches:");
            for (int m : matchesID) {
                System.out.println(m);
            }
            client.getClient().setPhase(Phase.JoiningGame1);
            client.getClient().joinGame();
        }
    }

    public void processGUI(it.polimi.ingsw.Client.GUI.ServerHandler client) {
        if (matchesID.isEmpty()) {
            client.getClient().setCurrPhase(Phase_GUI.ChoosingGameMode);
            client.getClient().chooseGameMode("There is no available match. Please try again.");
        } else {
            client.getClient().joinGame("", matchesID);
            client.getClient().setCurrPhase(Phase_GUI.JoiningGame1);
        }
    }
}
