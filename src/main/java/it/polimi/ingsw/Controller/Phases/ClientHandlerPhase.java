package it.polimi.ingsw.Controller.Phases;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Controller.MatchController;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.SendMatchesMessage;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

import java.util.ArrayList;
import java.util.List;

public abstract class ClientHandlerPhase {
    protected ClientHandler ch;

    public ClientHandlerPhase(ClientHandler ch) {
        this.ch = ch;
    }

    public abstract void process(MessageToServer msg);

    protected void sendAvailableMatchesToClient() throws GameException {
        List<MatchController> availableMatches = ch.getServer().getMatchmakingMatches();
        List<Integer> matchesID = new ArrayList<>();
        List<List<String>> players = new ArrayList<>();
        List<String> matchPlayers = new ArrayList<>();

        for (MatchController matchController:
                availableMatches) {
            matchesID.add(matchController.getID());
            for (ClientHandler client :
                    matchController.getClients()) {
                matchPlayers.add(client.getNickname());
            }
            players.add(matchPlayers);
            matchPlayers.clear();
        }
        if (ch.getMatch() == null) {
            MessageToClient msg = new SendMatchesMessage(matchesID, players);
            ch.send(msg);
            if (matchesID.isEmpty())
                throw new GameException("No available matches");
        }
    }
}
