package it.polimi.ingsw.Controller.Phases;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Controller.MatchController;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.SendMatchesMessage;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the phases of the game before the match is started.
 * It manages the connection and the creation of a match.
 */
public abstract class ClientHandlerPhase {
    protected ClientHandler ch;

    public ClientHandlerPhase(ClientHandler ch) {
        this.ch = ch;
    }

    /**
     * This method is called when a message has been received.
     * It defines the actions to follow during a specific phase.
     * @param msg message received
     */
    public abstract void process(MessageToServer msg);

    /**
     * This method gets and sends all the available matches to the client handled by the Client Handler
     * @throws GameException if there are no available matches
     */
    protected void sendAvailableMatchesToClient() throws GameException {
        List<MatchController> availableMatches = ch.getServer().getMatchmakingMatches();
        List<Integer> matchesID = new ArrayList<>();
        List<String[]> players = new ArrayList<>();
        List<String> matchPlayers = new ArrayList<>();

        for (MatchController matchController:
                availableMatches) {
            matchesID.add(matchController.getID());
            for (ClientHandler client :
                    matchController.getClients()) {
                matchPlayers.add(client.getNickname());
            }
            players.add(matchPlayers.toArray(new String[matchPlayers.size()]));
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
