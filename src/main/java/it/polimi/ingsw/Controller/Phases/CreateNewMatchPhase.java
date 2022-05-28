package it.polimi.ingsw.Controller.Phases;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.ConfirmJoiningMessage;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;
import it.polimi.ingsw.Network.Messages.toServer.JoiningPhase.SendStartInfoMessage;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class CreateNewMatchPhase extends ClientHandlerPhase{
    public CreateNewMatchPhase(ClientHandler ch) {
        super(ch);
    }

    @Override
    public void process(MessageToServer msg) {
        if (!(msg instanceof SendStartInfoMessage)) {
            System.out.println("Expected SendStartInfoMessage, received " + msg.getClass());
            return;
        } //TODO Check that the parameter for creating a match are correct
        ch.createMatchController(((SendStartInfoMessage) msg).getNumOfPlayers(), ((SendStartInfoMessage) msg).getWizard(), ((SendStartInfoMessage) msg).getExpertMode());
        MessageToClient confirm = new ConfirmJoiningMessage(true, "Game created", ch.getMatch().getID());
        ch.send(confirm);
        ch.setPhase(new GameStartedPhase(ch));
    }
}
