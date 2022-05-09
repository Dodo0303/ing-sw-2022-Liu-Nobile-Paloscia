package it.polimi.ingsw.Network.Messages.toClient.ActionPhase;

import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class MoveProfessorMessage extends MessageToClient {
    private StudentColor color;
    private boolean remove;

    public MoveProfessorMessage(StudentColor color, boolean remove) {
        this.color = color;
        this.remove = remove;
    }

    public StudentColor getColor() {
        return color;
    }


    @Override
    public void process(ServerHandler client) {

    }
}
