package it.polimi.ingsw.Network.Messages.toClient.JoiningPhase;

import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class ConfirmJoiningMessage extends MessageToClient {

    private boolean result;
    private String message;
    private int matchID;

    public ConfirmJoiningMessage(boolean result, String message, int matchID) {
        this.result = result;
        this.message = message;
        this.matchID = matchID;
    }

    public boolean isResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void process(ServerHandler ch) {
        if (result) {
            System.out.println("You've joined match " + matchID);
            if (message.equals("Game created")) {
                ch.getClient().setPhase(Phase.GameJoined);
            } else if (message.equals("You joined the game")){
                ch.getClient().setPhase(Phase.GameJoined);
            } else {
                ch.getClient().setPhase(Phase.JoiningGame1);
            }
        } else {
            System.out.println(this.message);
        }
    }
    //todo
    public void processGUI(it.polimi.ingsw.Client.GUI.ServerHandler ch) {
        if (result) {
            if (message.equals("Game created")) {
                ch.getClient().showGameCreated("Match: " + matchID);
                ch.getClient().setCurrPhase(Phase_GUI.GameJoined);
            } else if (message.equals("You joined the game")){
                ch.getClient().setCurrPhase(Phase_GUI.GameJoined);
                ch.getClient().showGameCreated("Match: " + matchID);
            } else {
                ch.getClient().setCurrPhase(Phase_GUI.JoiningGame1);
            }
        } else {
            System.out.println(this.message);
            if (message.equals("Wizard not available")) {
                ch.getClient().setCurrPhase(Phase_GUI.JoiningGame2);
                ch.getClient().chooseWizard(false);
            } else {
                ch.getClient().setCurrPhase(Phase_GUI.JoiningGame1);
            }
        }
    }
}
