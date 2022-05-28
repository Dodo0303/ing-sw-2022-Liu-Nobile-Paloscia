package it.polimi.ingsw.Controller.Phases;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Network.Messages.toServer.JoiningPhase.CreateMatchMessage;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class ReceiveMatchTypePhase extends ClientHandlerPhase{
    public ReceiveMatchTypePhase(ClientHandler ch) {
        super(ch);
    }

    @Override
    public void process(MessageToServer msg) {
        if (!(msg instanceof CreateMatchMessage)) {
            System.out.println("Expected CreateMatchMessage, received " + msg.getClass());
            return;
        }
        if (((CreateMatchMessage) msg).getNewMatch())
            ch.setPhase(new CreateNewMatchPhase(ch));
        else {
            try {
                sendAvailableMatchesToClient();
            } catch (GameException e) {
                //No available matches
                System.out.println(e.getMessage());
                return;
            }
            ch.setPhase(new JoinMatchPhase(ch));
        }

    }
}
