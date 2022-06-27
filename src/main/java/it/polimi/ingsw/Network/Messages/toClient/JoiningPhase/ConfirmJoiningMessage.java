package it.polimi.ingsw.Network.Messages.toClient.JoiningPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class ConfirmJoiningMessage extends MessageToClient {

    private boolean result, expert;
    private String message;
    private int matchID;

    public ConfirmJoiningMessage(boolean result, String message, int matchID, boolean expert) {
        this.result = result;
        this.message = message;
        this.matchID = matchID;
        this.expert = expert;
    }

    public boolean isResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void process(ServerHandler ch) {
        CLI cliClient = (CLI) ch.getClient();
        if (result) {
            cliClient.setExpert(expert);
            System.out.println("You've joined match " + matchID);
            if (message.equals("Game created")) {
                cliClient.setPhase(Phase.GameJoined);
            } else if (message.equals("You joined the game")){
                cliClient.setPhase(Phase.GameJoined);
            } else {
                cliClient.setPhase(Phase.JoiningGame1);
            }
        } else {
            System.out.println(this.message);
        }
    }

    public void processGUI(ServerHandler ch) {
        GUI guiClient = (GUI) ch.getClient();
        if (result) {
            guiClient.setExpert(expert);
            if (message.equals("Game created")) {
                guiClient.showGameCreated("Match: " + matchID);
                guiClient.setCurrPhase(Phase_GUI.GameJoined);
            } else if (message.equals("You joined the game")){
                guiClient.setCurrPhase(Phase_GUI.GameJoined);
                guiClient.showGameCreated("Match: " + matchID);
            } else {
                guiClient.setCurrPhase(Phase_GUI.JoiningGame1);
            }
        } else {
            System.out.println(this.message);
            if (message.equals("Wizard not available")) {
                guiClient.setCurrPhase(Phase_GUI.JoiningGame2);
                guiClient.chooseWizard(false);
            } else {
                guiClient.setCurrPhase(Phase_GUI.JoiningGame1);
            }
        }
    }
}
