package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class DenyMovementMessage extends MessageToClient {
    @Override
    public void process(ServerHandler client) {
        CLI cliClient = (CLI) client.getClient();
        if (cliClient.getCurrPhase().equals(Phase.Action2)) {
            System.out.print("Island too far away, please try again.\n");
            cliClient.moveMotherNature();
        } else if (cliClient.getCurrPhase().equals(Phase.Action3)) {
            System.out.print("Your movement has been denied, please try again.\n");
            cliClient.chooseCloud();
        } else if (cliClient.getCurrPhase().equals(Phase.Planning)) {
            System.out.print("Your movement has been denied, please try again.\n");
            cliClient.playAssistant();
        } else if (cliClient.getCurrPhase().equals(Phase.Action1)) {
            System.out.print("Your movement has been denied, please try again.\n");
            cliClient.moveStudentsFromEntrance();
        } else {
            cliClient.setPhase(cliClient.getPrevPhase());
            cliClient.setCurrCharacter(-1);
            System.out.println("Failed to use character card.");
        }
    }

    public void processGUI(ServerHandler client) {
        GUI guiClient = (GUI) client.getClient();
        if (!guiClient.getCurrPhase().equals(Phase_GUI.Planning) &&
                !guiClient.getCurrPhase().equals(Phase_GUI.Action1) &&
                !guiClient.getCurrPhase().equals(Phase_GUI.Action2) &&
                !guiClient.getCurrPhase().equals(Phase_GUI.Action3)) {//character phases
            guiClient.setCurrPhase(guiClient.getPrevPhase());
            guiClient.setCurrCharacter(-1);
            guiClient.viewSchoolBoard("Failed to use character card", false);
        } else {
            if (guiClient.getCurrPhase().equals(Phase_GUI.Planning)) {
                guiClient.playAssistant("Assistant not available");
            } else if (guiClient.getCurrPhase().equals(Phase_GUI.Action2)){
                guiClient.checkBoard("Island too far away, please try again");
            } else if (guiClient.getCurrPhase().equals(Phase_GUI.Action3)){
                guiClient.checkBoard("Choose a cloud");
            } else {
                guiClient.viewSchoolBoard("Movement denied", false);
            }
        }
    }
}
