package it.polimi.ingsw.Network.Messages.toClient.JoiningPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
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
        System.out.println("Available matches:");
        for (int m : matchesID) {
            System.out.println(m);
        }
    }
}
