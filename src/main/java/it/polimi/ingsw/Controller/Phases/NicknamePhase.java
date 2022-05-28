package it.polimi.ingsw.Controller.Phases;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.NickResponseMessage;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;
import it.polimi.ingsw.Network.Messages.toServer.JoiningPhase.SendNickMessage;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class NicknamePhase extends ClientHandlerPhase{

    public NicknamePhase(ClientHandler ch) {
        super(ch);
    }

    @Override
    public void process(MessageToServer msg) {
        if (!(msg instanceof SendNickMessage))
            return;
        if (!ch.getServer().isNicknameAvailable(((SendNickMessage) msg).getNickname())) {
            //Send the refuse
            MessageToClient refuse = new NickResponseMessage(null);
            ch.send(refuse);
            return;
        }
        //Received valid nickname
        ch.setNickname(((SendNickMessage) msg).getNickname());
        MessageToClient response = new NickResponseMessage(((SendNickMessage) msg).getNickname());
        ch.send(response);
        ch.setPhase(new ReceiveMatchTypePhase(ch));
    }
}
