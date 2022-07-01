package it.polimi.ingsw.Network.Messages.toServer.JoiningPhase;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Model.Wizard;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class SendChosenWizardMessage extends MessageToServer {
    private Wizard wizard;

    public SendChosenWizardMessage(Wizard wizard) {
        this.wizard = wizard;
    }

    public Wizard getWizard() {
        return wizard;
    }

}
