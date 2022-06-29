package it.polimi.ingsw.Network.Messages.toClient.JoiningPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.Phase;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Model.Wizard;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

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
        CLI cliClient = (CLI) ch.getClient();
        cliClient.setPhase(Phase.JoiningGame2);
        int i = 1;
        cliClient.setWizards(this.wizards);
        if (numPlayers == 4) {
            cliClient.setNicknames(nicknames);
            System.out.println("Players with WIZARD1 and WIZARD2 will be assigned to one team, players with WIZARD3 and WIZARD4 will be assigned to another team.");
            System.out.println("For the current round, ");
            for (int j = 0; j < 4; j++) {
                if (nicknames[j] != null) {
                    System.out.println("Player " + nicknames[j] + " owns wizard" + (j + 1));
                }
            }
        }
        System.out.println("Available wizards:");
        for (Wizard w : wizards) {
            System.out.println(i++ + ". " + w);
        }
        cliClient.chooseWizard();
   }

    public void processGUI(ServerHandler ch) {
        GUI guiClient = (GUI) ch.getClient();
        guiClient.setWizards(this.wizards);
        guiClient.setNumPlayer(this.numPlayers);
        if (numPlayers == 4) {
            guiClient.setNicknames(nicknames);
        }
        if (guiClient.getCurrPhase().equals(Phase.JoiningGame1)) {
            guiClient.setCurrPhase(Phase.JoiningGame2);
            guiClient.chooseWizard(false);
        }
    }
}
