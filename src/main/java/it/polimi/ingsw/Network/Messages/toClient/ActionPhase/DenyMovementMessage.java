package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.util.concurrent.TimeUnit;

public class DenyMovementMessage extends MessageToClient {
    @Override
    public void process(ServerHandler client) {
        System.out.print("Your movement has been denied, please try again.\n");
        if (client.getClient().getCurrPhase().equals(Phase.Action2)) {
            client.getClient().moveMotherNature();
        } else if (client.getClient().getCurrPhase().equals(Phase.Action3)) {
            client.getClient().chooseCloud();
        }
    }
}
