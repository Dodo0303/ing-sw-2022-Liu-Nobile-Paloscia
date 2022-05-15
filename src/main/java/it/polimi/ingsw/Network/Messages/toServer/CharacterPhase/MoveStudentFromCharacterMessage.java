package it.polimi.ingsw.Network.Messages.toServer.CharacterPhase;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class MoveStudentFromCharacterMessage extends MessageToServer {
    private int studentIndex;
    private int islandID;

    public MoveStudentFromCharacterMessage(int studentIndex, int islandID) {
        this.studentIndex = studentIndex;
        this.islandID = islandID;
    }

    public int getStudentIndex() {
        return studentIndex;
    }

    public int getIslandID() {
        return islandID;
    }

    @Override
    public void process(ClientHandler ch) {

    }
}
