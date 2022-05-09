package it.polimi.ingsw.Network.Messages.toClient.JoiningPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.util.List;

public class SendMatchesMessage extends MessageToClient {
    private List<Integer> matchesID;
    private String message;

    public void SendStartInfoMessage(List<Integer> matchesID, String message) {
        this.matchesID = matchesID;
        this.message = message;
    }

    @Override
    public void process(ServerHandler client) {

    }
}
