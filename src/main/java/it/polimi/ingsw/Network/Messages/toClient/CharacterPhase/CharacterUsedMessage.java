package it.polimi.ingsw.Network.Messages.toClient.CharacterPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.ServerHandler;
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
        CLI cliClient = (CLI) client.getClient();
        if (cliClient.getNickname().equals(playerID)) {
            cliClient.getGame().useEffectOfCharacter(characterID);
            cliClient.setPhase(cliClient.getPrevPhase());
        } else {
            cliClient.getGame().useEffectOfCharacter(characterID);
            System.out.println(playerID + " used character" + characterID);
        }
        cliClient.getGame().getPlayerByNickname(playerID).setCoins(coins);
        cliClient.setCurrCharacter(characterID);
    }

    public void processGUI(ServerHandler client) throws WrongEffectException, NotEnoughNoEntriesException {
        GUI guiClient = (GUI) client.getClient();
        if (guiClient.getNickname().equals(playerID)) {
            guiClient.getGame().useEffectOfCharacter(characterID);
            guiClient.setCurrPhase(guiClient.getPrevPhase());
        }
        guiClient.getGame().getPlayerByNickname(playerID).setCoins(coins);
        guiClient.setCurrCharacter(characterID);
        guiClient.viewSchoolBoard("", false);
    }

    public String getPlayerID() {
        return playerID;
    }

    public int getCharacterID() {
        return characterID;
    }
}
