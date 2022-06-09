package it.polimi.ingsw.Controller.Phases;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Controller.MatchController;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Exceptions.MatchMakingException;
import it.polimi.ingsw.Exceptions.NoSuchMatchException;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.ConfirmJoiningMessage;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.SendAvailableWizardsMessage;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.SendMatchesMessage;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;
import it.polimi.ingsw.Network.Messages.toServer.JoiningPhase.MatchChosenMessage;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JoinMatchPhase extends ClientHandlerPhase{
    public JoinMatchPhase(ClientHandler ch) {
        super(ch);
    }

    @Override
    public void process(MessageToServer msg) {
        if (!(msg instanceof MatchChosenMessage)) {
            System.out.println("Expected MatchChosenMessage, received " + msg.getClass());
            return;
        }
        try {
            ch.setMatch(ch.getServer().getMatchById(((MatchChosenMessage) msg).getMatchID()));
            ch.getMatch().addPlayer(ch);
        } catch (NoSuchMatchException e) {
            MessageToClient denyJoining = new ConfirmJoiningMessage(false, "Match doesn't exists", -1, false);
            ch.send(denyJoining);
            try {
                sendAvailableMatchesToClient();
            } catch (GameException ge) {
                ch.setPhase(new ReceiveMatchTypePhase(ch));
            }
            return;
        } catch (MatchMakingException e) {
            MessageToClient denyJoining = new ConfirmJoiningMessage(false, "Match full", ch.getMatch().getID(), false);
            ch.send(denyJoining);
            try {
                sendAvailableMatchesToClient();
            } catch (GameException ge) {
                ch.setPhase(new ReceiveMatchTypePhase(ch));
            }
            return;
        }

        try {
            ch.sendAvailableWizards();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ch.setPhase(new ReceiveWizardPhase(ch));

    }

}
