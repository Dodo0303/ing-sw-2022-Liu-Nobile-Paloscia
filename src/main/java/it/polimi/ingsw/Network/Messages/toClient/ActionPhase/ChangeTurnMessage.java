package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
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
        if (this.playerNickname.equals(ch.getClient().getNickname())) {
            System.out.println("Your turn");
            if (ch.getClient().menuThread != null && ch.getClient().menuThread.isAlive()) {
                ch.getClient().menuThread.interrupt();
            }
            if (ch.getClient().getCurrPhase().equals(Phase.GameJoined) && this.gamePhase.equals(Phase.Planning)) {
                ch.getClient().setPhase(Phase.Planning);
                ch.getClient().playAssistant();
            } else if (ch.getClient().getCurrPhase().equals(Phase.Planning) && this.gamePhase.equals(Phase.Action1)) {
                ch.getClient().setPhase(Phase.Action1);
                ch.getClient().moveStudentsFromEntrance();
            } else if (ch.getClient().getCurrPhase().equals(Phase.Action3) && this.gamePhase.equals(Phase.Planning)) {
                ch.getClient().setPhase(Phase.Planning);
                ch.getClient().playAssistant();
            }
        } else {
            ch.getClient().menu("");
        }
    }

    public void processGUI(it.polimi.ingsw.Client.GUI.ServerHandler ch) {
        if (this.playerNickname.equals(ch.getClient().getNickname())) {
            ch.getClient().checkBoard("Your turn.");
            if (ch.getClient().getCurrPhase().equals(Phase_GUI.GameJoined) && this.gamePhase.equals(Phase.Planning)) {
                ch.getClient().setCurrPhase(Phase_GUI.Planning);
                ch.getClient().playAssistant("");
            } else if (ch.getClient().getCurrPhase().equals(Phase_GUI.Planning) && this.gamePhase.equals(Phase.Action1)) {
                ch.getClient().setCurrPhase(Phase_GUI.Action1);
                ch.getClient().moveStudentsFromEntrance("Move a student.");
            } else if (ch.getClient().getCurrPhase().equals(Phase_GUI.Action3) && this.gamePhase.equals(Phase.Planning)) {
                ch.getClient().setCurrPhase(Phase_GUI.Planning);
                ch.getClient().playAssistant("");
            }
        } else {
            ch.getClient().checkBoard(ch.getClient().getNickname() + "'s turn.");
        }
    }

    public Object getPhase() {
        return this.gamePhase;
    }
}
