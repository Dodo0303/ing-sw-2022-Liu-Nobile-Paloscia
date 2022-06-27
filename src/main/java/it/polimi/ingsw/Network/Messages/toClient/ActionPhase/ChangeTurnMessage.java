package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;
import it.polimi.ingsw.Utilities;

public class ChangeTurnMessage extends MessageToClient {
    private String playerNickname;
    private Phase gamePhase;

    public ChangeTurnMessage(String playerNickname, String gamePhase) {
        this.playerNickname = playerNickname;
        this.gamePhase = Utilities.getPhase(gamePhase);
    }

    public String getPlayerID() {
        return this.playerNickname;
    }

    public Phase getGamePhase() {
        return this.gamePhase;
    }

    @Override
    public void process(ServerHandler ch) {
        CLI cliClient = (CLI) ch.getClient();
        if (this.playerNickname.equals(cliClient.getNickname())) {
            cliClient.setMyTurn(true);
            if (cliClient.getCurrPhase().equals(Phase.GameJoined) && this.gamePhase.equals(Phase.Planning)) {
                cliClient.setPhase(Phase.Planning);
                cliClient.playAssistant();
            } else if (cliClient.getCurrPhase().equals(Phase.Planning) && this.gamePhase.equals(Phase.Action1)) {
                cliClient.setPhase(Phase.Action1);
                cliClient.moveStudentsFromEntrance();
            } else if (cliClient.getCurrPhase().equals(Phase.Action3) && this.gamePhase.equals(Phase.Planning)) {
                cliClient.setPhase(Phase.Planning);
                cliClient.playAssistant();
            }
        } else {
            cliClient.setMyTurn(false);
            cliClient.setCurrCharacter(-1);
        }
    }

    public void processGUI(ServerHandler ch) {
        GUI guiClient = (GUI) ch.getClient();
        guiClient.setCurrCharacter(-1);
        if (this.playerNickname.equals(guiClient.getNickname())) {
            guiClient.setChangeTurnNums(0);
            guiClient.setMyTurn(true);
            if (guiClient.getCurrPhase().equals(Phase_GUI.GameJoined) && this.gamePhase.equals(Phase.Planning)) {
                guiClient.setCurrPhase(Phase_GUI.Planning);
                guiClient.setAssistantPicked(false);
                guiClient.playAssistant("");
            } else if (guiClient.getCurrPhase().equals(Phase_GUI.Planning) && this.gamePhase.equals(Phase.Action1)) {
                guiClient.setCurrPhase(Phase_GUI.Action1);
                guiClient.viewSchoolBoard("Move a student.", false);
            } else if (guiClient.getCurrPhase().equals(Phase_GUI.Action3) && this.gamePhase.equals(Phase.Planning)) {
                guiClient.setCurrPhase(Phase_GUI.Planning);
                guiClient.setAssistantPicked(false);
                guiClient.playAssistant("");
            }
        } else {
            guiClient.setMyTurn(false);
            guiClient.checkBoard(playerNickname + "'s round.");
        }
    }

    public Object getPhase() {
        return this.gamePhase;
    }
}
