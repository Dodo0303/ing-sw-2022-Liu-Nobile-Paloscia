package it.polimi.ingsw.Network.Messages.toClient.CharacterPhase;

import it.polimi.ingsw.Client.CLI.ServerHandler;
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

    }
}
