package it.polimi.ingsw.Network.Messages.toServer;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    public String content;
    public ChatMessage(String content) {
        this.content = content;
    }
}
