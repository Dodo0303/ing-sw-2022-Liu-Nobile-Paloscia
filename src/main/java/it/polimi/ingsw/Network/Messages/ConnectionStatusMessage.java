package it.polimi.ingsw.Network.Messages;

import it.polimi.ingsw.Controller.ClientHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConnectionStatusMessage implements Serializable {

    /**
     * True if the player has just connected; false if the player
     * has just disconnected.
     */

    public ConnectionStatusMessage(int playerID, boolean connecting, List<ClientHandler> players) {
    }
}
