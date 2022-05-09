package it.polimi.ingsw.Network.Messages.toServer.ActionPhase;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class MoveStudentFromEntranceMessage extends MessageToServer {
    private StudentColor color;
    private int destination;
    private int destinationID;

    public MoveStudentFromEntranceMessage(StudentColor color, int destination, int destinationID) {
        this.color = color;
        this.destination = destination;
        this.destinationID = destinationID;
    }

    public StudentColor getStudentcolor() {
        return color;
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
