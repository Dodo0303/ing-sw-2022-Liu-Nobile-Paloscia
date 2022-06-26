package it.polimi.ingsw.Network.Messages.toClient.JoiningPhase;

import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
import it.polimi.ingsw.Model.Wizard;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.util.ArrayList;
import java.util.List;

public class SendAvailableWizardsMessage extends MessageToClient {
    private List<Wizard> wizards;
    private int numPlayers;
    private String[] nicknames;

    public SendAvailableWizardsMessage(List<Wizard> wizards) {
        this.wizards = wizards;
    }

    public SendAvailableWizardsMessage(List<Wizard> wizards, int numPlayers, String[] nicknames) {
        this.wizards = wizards;
        this.numPlayers = numPlayers;
        this.nicknames = nicknames;
    }

    public List<Wizard> getWizards() {
        return wizards;
    }

    @Override
    public void process(ServerHandler ch) {
        ch.getClient().setPhase(Phase.JoiningGame2);
        int i = 1;
        ch.getClient().setWizards(this.wizards);
        ch.getClient().setNumPlayers(this.numPlayers);
        if (numPlayers == 4) {
            ch.getClient().setNicknames(nicknames);
        }
        System.out.println("Available wizards:");
        for (Wizard w : wizards) {
            System.out.println(i++ + ". " + w);
        }
        ch.getClient().chooseWizard();
   }

    public void processGUI(it.polimi.ingsw.Client.GUI.ServerHandler ch) {
        ch.getClient().setWizards(this.wizards);
        ch.getClient().setNumPlayer(this.numPlayers);
        if (numPlayers == 4) {
            ch.getClient().setNicknames(nicknames);
        }
        if (ch.getClient().getCurrPhase().equals(Phase_GUI.JoiningGame1)) {
            ch.getClient().setCurrPhase(Phase_GUI.JoiningGame2);
            ch.getClient().chooseWizard(false);
        }
    }
}
