package it.polimi.ingsw.Network.Messages.toClient.CharacterPhase;

import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Exceptions.EmptyCloudException;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Exceptions.NotEnoughNoEntriesException;
import it.polimi.ingsw.Exceptions.WrongEffectException;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

public class CharacterUsedMessage extends MessageToClient {
    private String playerID;
    private int characterID, coins;

    public CharacterUsedMessage(String playerID, int characterID, int coins) {
        this.playerID = playerID;
        this.characterID = characterID;
        this.coins = coins;
    }

    @Override
    public void process(ServerHandler client) throws WrongEffectException, NotEnoughNoEntriesException {
        if (client.getClient().getNickname().equals(playerID)) {
            client.getClient().getGame().useEffectOfCharacter(characterID);
        }
    }

    public void processGUI(it.polimi.ingsw.Client.GUI.ServerHandler client) throws WrongEffectException, NotEnoughNoEntriesException {
        if (client.getClient().getNickname().equals(playerID)) {
            client.getClient().getGame().useEffectOfCharacter(characterID);
            client.getClient().setCurrPhase(client.getClient().getPrevPhase());
        }
        client.getClient().getGame().getPlayerByNickname(playerID).setCoins(coins);
        client.getClient().setCurrCharacter(characterID);
        client.getClient().viewSchoolBoard("", false);
    }

    public String getPlayerID() {
        return playerID;
    }

    public int getCharacterID() {
        return characterID;
    }
}
