package it.polimi.ingsw.Network.Messages.toClient.JoiningPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.util.List;

public class SendMatchesMessage extends MessageToClient {
    private List<Integer> matchesID;
    private List<List<String>> players;

    public List<Integer> getMatchesID() {
        return matchesID;
    }

    public List<String> getPlayersByMatchId(int id) {
        return players.get(id);
    }

    public void SendStartInfoMessage(List<Integer> matchesID, List<List<String>> players) {
        this.matchesID = matchesID;
        this.players = players;
    }

    @Override
    public void process(ServerHandler client) {
        client.getClient().setMatchesID(matchesID);
        client.getClient().setPlayers(players);
    }

}
