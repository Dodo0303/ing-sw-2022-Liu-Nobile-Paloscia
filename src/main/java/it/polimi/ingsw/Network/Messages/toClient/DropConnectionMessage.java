package it.polimi.ingsw.Network.Messages.toClient;

import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Exceptions.EmptyCloudException;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Exceptions.NotEnoughNoEntriesException;
import it.polimi.ingsw.Exceptions.WrongEffectException;

public class DropConnectionMessage extends MessageToClient{
    String nickname;

    public String getNickname() {
        return nickname;
    }

    public DropConnectionMessage(String nickname) {
        this.nickname = nickname;
    }
    @Override
    public void process(ServerHandler client) throws FullTableException, InterruptedException, EmptyCloudException, WrongEffectException, NotEnoughNoEntriesException {
    }
}
