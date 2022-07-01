package it.polimi.ingsw.Controller.Phases;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

/**
 * This phase is set when the match is started, and all the messages received by the client handler must be redirected to the match controller.
 */
public class GameStartedPhase extends ClientHandlerPhase{
    public GameStartedPhase(ClientHandler ch) {
        super(ch);
    }

    @Override
    public void process(MessageToServer msg) {
        ch.getMatch().process(msg, ch);
    }
}
