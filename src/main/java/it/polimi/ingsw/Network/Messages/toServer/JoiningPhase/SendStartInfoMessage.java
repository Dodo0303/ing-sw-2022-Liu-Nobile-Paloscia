package it.polimi.ingsw.Network.Messages.toServer.JoiningPhase;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Model.Wizard;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class SendStartInfoMessage extends MessageToServer {
    private int numOfPlayers;
    private Boolean expertMode;
    private Wizard wizard;

    public SendStartInfoMessage(int numOfPlayers, Boolean expertMode, Wizard wizard) {
        this.numOfPlayers = numOfPlayers;
        this.expertMode = expertMode;
        this.wizard = wizard;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public Boolean getExpertMode() {
        return expertMode;
    }

    public Wizard getWizard() {
        return wizard;
    }

}
