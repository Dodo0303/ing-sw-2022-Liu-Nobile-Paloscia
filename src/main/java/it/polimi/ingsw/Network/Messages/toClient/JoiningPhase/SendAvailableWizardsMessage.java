package it.polimi.ingsw.Network.Messages.toClient.JoiningPhase;

import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
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
        if (ch.getClient().getCurrPhase().equals(Phase.JoiningGame1)) {
            ch.getClient().setPhase(Phase.JoiningGame2);
        }
        int i = 1;
        ch.getClient().setWizards(this.wizards);
        System.out.println("Available wizards:");
        for (Wizard w : wizards) {
            System.out.println(i++ + ". " + w);
        }
   }

    public void processGUI(it.polimi.ingsw.Client.GUI.ServerHandler ch) {
        ch.getClient().setWizards(this.wizards);
        if (ch.getClient().getCurrPhase().equals(Phase_GUI.JoiningGame1)) {
            ch.getClient().setCurrPhase(Phase_GUI.JoiningGame2);
            ch.getClient().chooseWizard(false);
        }
    }
}
