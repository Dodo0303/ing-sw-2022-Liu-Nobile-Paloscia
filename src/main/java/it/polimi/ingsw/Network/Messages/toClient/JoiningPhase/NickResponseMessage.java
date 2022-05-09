package it.polimi.ingsw.Network.Messages.toClient.JoiningPhase;

import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class NickResponseMessage extends MessageToClient {
    private boolean response;

    public NickResponseMessage(boolean response) {
        this.response = response;
    }

    public boolean isResponse() {
        return response;
    }

    @Override
    public void process(ServerHandler sh){
        return;
    }
}
