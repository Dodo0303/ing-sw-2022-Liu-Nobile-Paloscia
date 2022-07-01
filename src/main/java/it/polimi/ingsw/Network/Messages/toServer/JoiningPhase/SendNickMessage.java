package it.polimi.ingsw.Network.Messages.toServer.JoiningPhase;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

public class SendNickMessage extends MessageToServer {
    private String nickname;

    public SendNickMessage(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

}
