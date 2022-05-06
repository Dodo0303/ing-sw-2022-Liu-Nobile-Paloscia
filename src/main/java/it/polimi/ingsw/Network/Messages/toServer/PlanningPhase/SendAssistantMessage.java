package it.polimi.ingsw.Network.Messages.toServer.PlanningPhase;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Model.Assistant;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class SendAssistantMessage extends MessageToServer {
    private Assistant assistant;

    public SendAssistantMessage(Assistant assistant) {
        this.assistant = assistant;
    }

    public Assistant getAssistant() {
        return assistant;
    }

    @Override
    public void process(ClientHandler ch) {
        //TODO
    }
}
