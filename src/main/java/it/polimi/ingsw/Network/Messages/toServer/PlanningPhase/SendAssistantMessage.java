package it.polimi.ingsw.Network.Messages.toServer.PlanningPhase;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class SendAssistantMessage extends MessageToServer {
    private int assistantValue;

    public SendAssistantMessage(int assistantValue) {
        this.assistantValue = assistantValue;
    }

    public int getAssistant() {
        return assistantValue;
    }

    @Override
    public void process(ClientHandler ch) {
        //TODO
    }
}
