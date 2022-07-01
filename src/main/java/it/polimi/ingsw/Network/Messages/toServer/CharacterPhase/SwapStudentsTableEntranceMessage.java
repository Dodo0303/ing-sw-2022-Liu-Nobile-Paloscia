package it.polimi.ingsw.Network.Messages.toServer.CharacterPhase;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class SwapStudentsTableEntranceMessage extends MessageToServer {
    private StudentColor[] table;
    private int[] entrancePosition;

    public SwapStudentsTableEntranceMessage(StudentColor[] table, int[] entrancePosition) {
        this.table = table;
        this.entrancePosition = entrancePosition;
    }

    public StudentColor[] getTable() {
        return table;
    }

    public int[] getEntrancePosition() {
        return entrancePosition;
    }

}
