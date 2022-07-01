package it.polimi.ingsw.Controller.Phases;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Controller.MatchController;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

/**
 * This abstract class is the core component of a "state" pattern. We decided to implement the transition between
 * states inside the states themselves, instead of MatchController. This facilitates the validation of messages from
 * client to server, because each message received is checked by a specific phase class. After validation, the class
 * calls specific methods of MatchController to go on with the game.
 * The Phase class represents only phases of a game already started.
 */
public abstract class Phase {
    protected MatchController match;

    public Phase(MatchController match) {
        this.match = match;
    }

    /**
     * This method implements the action that follow the receiving of a specific message during a certain phase of the game.
     * @param msg message received
     * @param ch Client Handler of the player
     */
    public abstract void process(MessageToServer msg, ClientHandler ch);

    /**
     * This method is used to get the game phase that follows the current one.
     */
    public abstract void nextPhase();
}
