package it.polimi.ingsw.Network.Messages.toClient.JoiningPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

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
        CLI cliClient = (CLI) ch.getClient();
        cliClient.setGame(gameModel);
    }

    public void processGUI(ServerHandler ch) {
        GUI guiClient = (GUI) ch.getClient();
        guiClient.setGame(gameModel);
    }
}
