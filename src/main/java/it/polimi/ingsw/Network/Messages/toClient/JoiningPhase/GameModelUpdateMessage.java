package it.polimi.ingsw.Network.Messages.toClient.JoiningPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.util.ArrayList;

public class GameModelUpdateMessage extends MessageToClient {
    private GameModel gameModel;

    public GameModelUpdateMessage(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    @Override
    public void process(ServerHandler ch) {
        try {
            CLI cliClient = (CLI) ch.getClient();
            cliClient.setGame(gameModel);
        } catch (NullPointerException e) {
            process(ch);
        }
    }

    public void processGUI(ServerHandler ch) {
        GUI guiClient = (GUI) ch.getClient();
        guiClient.setGame(gameModel);
    }
}
