package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class MoveProfessorMessage extends MessageToClient {
    private StudentColor color;
    private boolean remove;
    private String playerID;

    public MoveProfessorMessage(StudentColor color, boolean remove, String playerID) {
        this.color = color;
        this.remove = remove;
        this.playerID = playerID;
    }

    public StudentColor getColor() {
        return color;
    }


    @Override
    public void process(ServerHandler client) {
        CLI cliClient = (CLI) client.getClient();
        if (!remove) {
            cliClient.getGame().getPlayerByNickname(playerID).addProfessor(color);
        } else {
            cliClient.getGame().getPlayerByNickname(playerID).removeProfessor(color);
        }

    }

    public void processGUI(ServerHandler client) {
        GUI guiClient = (GUI) client.getClient();
        if (!remove) {
            guiClient.getGame().getPlayerByNickname(playerID).addProfessor(color);
        } else {
            guiClient.getGame().getPlayerByNickname(playerID).removeProfessor(color);
        }
    }
}
