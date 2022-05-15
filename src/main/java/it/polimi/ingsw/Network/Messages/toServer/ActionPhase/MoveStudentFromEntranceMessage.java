package it.polimi.ingsw.Network.Messages.toServer.ActionPhase;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class MoveStudentFromEntranceMessage extends MessageToServer {
    private final int studentIndex;
    private final int destination;
    private final int destinationID;

    public MoveStudentFromEntranceMessage(int studentIndex, int destination, int destinationID) {
        this.studentIndex = studentIndex;
        this.destination = destination;
        this.destinationID = destinationID;
    }

    public int getStudentIndex() {
        return this.studentIndex;
    }

    public int getDestination() {
        return destination;
    }

    public int getDestinationID() {
        return destinationID;
    }

    @Override
    public void process(ClientHandler ch) {
    }
}
