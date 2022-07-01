package it.polimi.ingsw.Network.Messages.toServer.CharacterPhase;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class ChooseStudentColorMessage extends MessageToServer {
    private StudentColor studentColor;

    public ChooseStudentColorMessage(StudentColor studentColor) {
        this.studentColor = studentColor;
    }

    public StudentColor getStudentColor() {
        return studentColor;
    }

}
