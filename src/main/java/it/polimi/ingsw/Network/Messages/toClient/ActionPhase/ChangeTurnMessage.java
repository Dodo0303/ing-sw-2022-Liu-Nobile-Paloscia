package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;
import it.polimi.ingsw.Utilities;

import java.util.ArrayList;

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
            ch.getClient().setMyTurn(true);
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
            ch.getClient().setMyTurn(false);
            ch.getClient().setCurrCharacter(-1);
        }
    }

    public void processGUI(it.polimi.ingsw.Client.GUI.ServerHandler ch) {
        ch.getClient().setCurrCharacter(-1);
        if (this.playerNickname.equals(ch.getClient().getNickname())) {
            ch.getClient().setChangeTurnNums(0);
            ch.getClient().setMyTurn(true);
            ch.getClient().checkBoard("Your round.");
            if (ch.getClient().getCurrPhase().equals(Phase_GUI.GameJoined) && this.gamePhase.equals(Phase.Planning)) {
                ch.getClient().setCurrPhase(Phase_GUI.Planning);
                ch.getClient().playAssistant("");
            } else if (ch.getClient().getCurrPhase().equals(Phase_GUI.Planning) && this.gamePhase.equals(Phase.Action1)) {
                ch.getClient().setCurrPhase(Phase_GUI.Action1);
                ch.getClient().viewSchoolBoard("Move a student.", false);
            } else if (ch.getClient().getCurrPhase().equals(Phase_GUI.Action3) && this.gamePhase.equals(Phase.Planning)) {
                ch.getClient().setCurrPhase(Phase_GUI.Planning);
                /*
                if (ch.getClient().getPlayerPlayedAssistant() != null) {
                    for (int i = 0; i < ch.getClient().getGame().getPlayers().size(); i++) {
                        if (!ch.getClient().getPlayerPlayedAssistant().contains(ch.getClient().getGame().getPlayers().get(i).getNickName())) {
                            ch.getClient().getGame().getPlayers().get(i).clearAssistant();
                        }
                    }
                } else {
                    for (int i = 0; i < ch.getClient().getGame().getPlayers().size(); i++) {
                        ch.getClient().getGame().getPlayers().get(i).clearAssistant();
                    }
                    ch.getClient().setPlayerPlayedAssistant(new ArrayList<>());
                    ch.getClient().getPlayerPlayedAssistant().add(ch.getClient().getNickname());
                }

                 */

                ch.getClient().playAssistant("");
            }
        } else {
            ch.getClient().setMyTurn(false);
            ch.getClient().checkBoard(playerNickname + "'s round.");
        }
    }

    public Object getPhase() {
        return this.gamePhase;
    }
}
