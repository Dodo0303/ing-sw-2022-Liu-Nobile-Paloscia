package it.polimi.ingsw.Network.Messages.toClient.CharacterPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
import it.polimi.ingsw.Exceptions.EmptyCloudException;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.util.ArrayList;

public class StudentColorChosenMessage extends MessageToClient {
    private StudentColor colorChosen;
    private boolean isCharacter12;
    private ArrayList<Player> playersUpdated;

    public StudentColorChosenMessage(boolean isCharacter12, StudentColor colorChosen, ArrayList<Player> playersUpdated) {
        this.colorChosen = colorChosen;
        this.isCharacter12 = isCharacter12;
        this.playersUpdated = playersUpdated;
    }

    public StudentColor getColorChosen() {
        return colorChosen;
    }

    public boolean isCharacter12() {
        return isCharacter12;
    }

    public ArrayList<Player> getPlayersUpdated() {
        return playersUpdated;
    }

    @Override
    public void process(ServerHandler client) throws FullTableException, InterruptedException, EmptyCloudException {
        CLI cliClient = (CLI) client.getClient();
        cliClient.getGame().getPlayerByNickname(cliClient.getNickname()).setCoins(cliClient.getGame().getPlayerByNickname(cliClient.getNickname()).getCoins() - 3);
        if (!isCharacter12) {
            System.out.println("Color " + colorChosen.toString() + " is not considered for the influence in this turn");
            if (cliClient.getCurrPhase().equals(Phase.Character9)) {
                cliClient.setPhase(cliClient.getPrevPhase());
            }
        } else {
            if (cliClient.getCurrPhase().equals(Phase.Character12)) {
                cliClient.setPhase(cliClient.getPrevPhase());
                System.out.println("Character12 is in use");
            }
            cliClient.getGame().set_players(playersUpdated);
        }
    }

    public void processGUI(ServerHandler client) throws FullTableException, InterruptedException, EmptyCloudException {
        GUI guiClient = (GUI) client.getClient();
        guiClient.getGame().getPlayerByNickname(guiClient.getNickname()).setCoins(guiClient.getGame().getPlayerByNickname(guiClient.getNickname()).getCoins() - 3);
        if (!isCharacter12) {
            if (guiClient.getCurrPhase().equals(Phase_GUI.Character9)) {
                guiClient.setCurrPhase(guiClient.getPrevPhase());
                guiClient.viewSchoolBoard("Color " + colorChosen.toString() + " is not considered for the influence in this turn", false);
            }
        } else {
            if (guiClient.getCurrPhase().equals(Phase_GUI.Character12)) {
                guiClient.setCurrPhase(guiClient.getPrevPhase());
            }
            guiClient.getGame().set_players(playersUpdated);
            guiClient.viewSchoolBoard("Character12 is in use!", false);
        }
    }
}
