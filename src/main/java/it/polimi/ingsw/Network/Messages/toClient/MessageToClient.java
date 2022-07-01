package it.polimi.ingsw.Network.Messages.toClient;

import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Exceptions.EmptyCloudException;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Exceptions.NotEnoughNoEntriesException;
import it.polimi.ingsw.Exceptions.WrongEffectException;

import java.io.Serializable;

/**
 * This class represents the messages that the server can send to the client
 */
public abstract class MessageToClient implements Serializable {
    /**
     * This method is called by the client when the message is received.
     * It contains the actions that the client must do receiving this message.
     * @param client server handler of the client that is processing the message
     * @throws FullTableException when a table is full, and it's impossible to add a student to it
     * @throws InterruptedException when a thread waiting has been interrupted
     * @throws EmptyCloudException when a cloud is empty, and it's impossible to take students from it
     * @throws WrongEffectException if it's trying to use the effect of a character without using the needed parameters
     * @throws NotEnoughNoEntriesException if it's impossible to use the effect of character 5, because there are no no-entry tiles left on it
     */
    public abstract void process(ServerHandler client) throws FullTableException, InterruptedException, EmptyCloudException, WrongEffectException, NotEnoughNoEntriesException;
}
