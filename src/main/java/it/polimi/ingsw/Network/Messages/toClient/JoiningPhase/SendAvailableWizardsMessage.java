package it.polimi.ingsw.Network.Messages.toClient.JoiningPhase;

import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Model.Wizard;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.util.List;

public class SendAvailableWizardsMessage extends MessageToClient {
    private List<Wizard> wizards;

    public SendAvailableWizardsMessage(List<Wizard> wizards) {
        this.wizards = wizards;
    }

    public List<Wizard> getWizards() {
        return wizards;
    }

    @Override
    public void process(ServerHandler ch) {
        System.out.println("Available wizards:");
        for (Wizard w : wizards) {
            System.out.println(w);
        }
        ch.getClient().setWizards(this.wizards);
        ch.getClient().setPhase(Phase.JoiningGame2);
   }
}
