package it.polimi.ingsw.Controller.Phases;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.ConfirmJoiningMessage;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.SendAvailableWizardsMessage;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;
import it.polimi.ingsw.Network.Messages.toServer.JoiningPhase.SendChosenWizardMessage;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

import java.io.IOException;

public class ReceiveWizardPhase extends ClientHandlerPhase{
    public ReceiveWizardPhase(ClientHandler ch) {
        super(ch);
    }

    @Override
    public void process(MessageToServer msg) {
        if (!(msg instanceof SendChosenWizardMessage)) {
            System.out.println("Expected SendChosenWizardMessage, received " + msg.getClass());
            return;
        }

        try {
            System.out.println("Received " + ((SendChosenWizardMessage) msg).getWizard().toString());
            ch.setWizard(((SendChosenWizardMessage) msg).getWizard());
            ch.getMatch().setWizardOfPlayer(ch, ((SendChosenWizardMessage) msg).getWizard());
            MessageToClient confirm = new ConfirmJoiningMessage(true, "You joined the game", ch.getMatch().getID(), ch.getMatch().isExpert());
            ch.send(confirm);
        } catch (GameException e) {
            System.out.println(e.getMessage());
            ch.setWizard(null);
            MessageToClient denyJoining = new ConfirmJoiningMessage(false, "Wizard not available", ch.getMatch().getID(), false);
            ch.send(denyJoining);
            try {
                ch.sendAvailableWizards();
            } catch (IOException io){
                io.printStackTrace();
                return;
            }
            return;
        }

        ch.setPhase(new GameStartedPhase(ch));


    }
}
