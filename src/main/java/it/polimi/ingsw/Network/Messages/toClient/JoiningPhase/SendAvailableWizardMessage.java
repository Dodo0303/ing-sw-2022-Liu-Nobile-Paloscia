package it.polimi.ingsw.Network.Messages.toClient.JoiningPhase;

import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Model.Wizard;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.util.List;

public class SendAvailableWizardMessage extends MessageToClient {
    private List<Wizard> wizards;

    public void SendMatchesMessage(List<Wizard> wizards) {
        this.wizards = wizards;
    }

    public List<Wizard> getWizards() {
        return wizards;
    }


    @Override
    public void process(ServerHandler client) {
        client.getClient().setWizards(wizards);
    }
}
