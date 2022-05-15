package it.polimi.ingsw.Network.Messages.toServer.CharacterPhase;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class MoveStudentsToTableMessage extends MessageToServer {
    private int studentIndex;

    public MoveStudentsToTableMessage(int studentIndex) {
        this.studentIndex = studentIndex;
    }

    public int getIndex() {
        return studentIndex;
    }

    @Override
    public void process(ClientHandler ch) {

    }
}
