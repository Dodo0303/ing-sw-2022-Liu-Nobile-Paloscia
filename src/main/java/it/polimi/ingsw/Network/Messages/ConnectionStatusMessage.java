package it.polimi.ingsw.Network.Messages;

import it.polimi.ingsw.Controller.ClientHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConnectionStatusMessage implements Serializable {
    public final int playerID;

    /**
     * True if the player has just connected; false if the player
     * has just disconnected.
     */
    public final boolean connecting;

    public final List<ClientHandler> players;

    public ConnectionStatusMessage(int playerID, boolean connecting, List<ClientHandler> players) {
        this.playerID = playerID;
        this.connecting = connecting;
        this.players = players;
    }
}
