package it.polimi.ingsw.Network.Messages.toServer.ActionPhase;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class MoveStudentFromEntranceMessage extends MessageToServer {
    private final StudentColor student;
    private final int destination;
    private final int destinationID;

    public MoveStudentFromEntranceMessage(StudentColor student, int destination, int destinationID) {
        this.student = student;
        this.destination = destination;
        this.destinationID = destinationID;
    }

    public StudentColor getStudent() {
        return this.student;
    }

    public int getDestination() {
        return destination;
    }

    public int getDestinationID() {
        return destinationID;
    }

    @Override
    public void process(ClientHandler ch) {
        //TODO
    }
}
