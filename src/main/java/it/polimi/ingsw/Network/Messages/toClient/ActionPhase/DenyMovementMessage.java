package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class DenyMovementMessage extends MessageToClient {
    @Override
    public void process(ServerHandler client) {
        System.out.print("Your movement has been denied, please try again.\n");
        if (client.getClient().getCurrPhase().equals(Phase.Action2)) {
            client.getClient().moveMotherNature();
        } else if (client.getClient().getCurrPhase().equals(Phase.Action3)) {
            client.getClient().chooseCloud();
        } else if (client.getClient().getCurrPhase().equals(Phase.Planning)) {
            client.getClient().playAssistant();
        } else if (client.getClient().getCurrPhase().equals(Phase.Action1)) {
            client.getClient().moveStudentsFromEntrance();
        } else {
            client.getClient().setPhase(client.getClient().getPrevPhase());
            client.getClient().setCurrCharacter(-1);
            System.out.println("Failed to use character cards.");
        }
    }

    public void processGUI(it.polimi.ingsw.Client.GUI.ServerHandler client) {
        if (!client.getClient().getCurrPhase().equals(Phase_GUI.Planning) &&
                !client.getClient().getCurrPhase().equals(Phase_GUI.Action1) &&
                !client.getClient().getCurrPhase().equals(Phase_GUI.Action2) &&
                !client.getClient().getCurrPhase().equals(Phase_GUI.Action3)) {
            client.getClient().setCurrPhase(client.getClient().getPrevPhase());
            client.getClient().setCurrCharacter(-1);
            client.getClient().viewSchoolBoard("Character not available", false);
        } else {
            if (client.getClient().getCurrPhase().equals(Phase_GUI.Planning)) {
                client.getClient().playAssistant("Assistant not available.");
            } else {
                client.getClient().viewSchoolBoard("Movement denied", false);
            }
        }
    }
}
