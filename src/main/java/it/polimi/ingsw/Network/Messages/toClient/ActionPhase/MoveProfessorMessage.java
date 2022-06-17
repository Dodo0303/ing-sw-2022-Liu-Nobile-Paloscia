package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.ServerHandler;
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
        if (!remove) {
            client.getClient().getGame().getPlayerByNickname(playerID).addProfessor(color);
        } else {
            client.getClient().getGame().getPlayerByNickname(playerID).removeProfessor(color);
        }

    }

    public void processGUI(it.polimi.ingsw.Client.GUI.ServerHandler client) {
        if (!remove) {
            client.getClient().getGame().getPlayerByNickname(playerID).addProfessor(color);
        } else {
            client.getClient().getGame().getPlayerByNickname(playerID).removeProfessor(color);
        }
    }
}
