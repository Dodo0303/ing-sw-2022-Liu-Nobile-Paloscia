package it.polimi.ingsw.Network.Messages.toServer.ActionPhase;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class MoveStudentFromEntranceMessage extends MessageToServer {
    private int studentPosition;
    private int destination;
    private int destinationID;

    public MoveStudentFromEntranceMessage(int studentPosition, int destination, int destinationID) {
        this.studentPosition = studentPosition;
        this.destination = destination;
        this.destinationID = destinationID;
    }

    public int getStudentPosition() {
        return studentPosition;
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
