package it.polimi.ingsw.Network.Messages.toClient.JoiningPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.Phase;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.util.List;

public class SendMatchesMessage extends MessageToClient {
    private List<Integer> matchesID;
    private List<String[]> players;

    public SendMatchesMessage(List<Integer> matchesID, List<String[]> players) {
        this.matchesID = matchesID;
        this.players = players;
    }

    public List<Integer> getMatchesID() {
        return matchesID;
    }

    /*
    public List<String> getPlayersByMatchId(int id) {
        return players.get(id);
    }

     */

    public List<String[]> getPlayers() {
        return players;
    }

    @Override
    public void process(ServerHandler client) {
        CLI cliClient = (CLI) client.getClient();
        if (matchesID.isEmpty()) {
            System.out.println("There is no available match.");
            cliClient.setPhase(Phase.ChoosingGameMode);
            cliClient.chooseGameMode();
        } else {
            System.out.println("Available matches:");
            for (int i = 0; i < matchesID.size(); i++) {
                System.out.println(i+1 + ") Match " + matchesID.get(i));
                for (int j = 0; j < players.get(i).length; j++) {
                    System.out.println("    Joined players: " + players.get(i)[j]);
                }
            }
            cliClient.setPhase(Phase.JoiningGame1);
            cliClient.joinGame(matchesID);
        }
    }

    public void processGUI(ServerHandler client) {
        GUI guiClient = (GUI) client.getClient();
        if (matchesID.isEmpty()) {
            guiClient.setCurrPhase(Phase.ChoosingGameMode);
            guiClient.chooseGameMode("There is no available match. Please try again.");
        } else {
            guiClient.joinGame("", matchesID);
            guiClient.setCurrPhase(Phase.JoiningGame1);
        }
    }
}
