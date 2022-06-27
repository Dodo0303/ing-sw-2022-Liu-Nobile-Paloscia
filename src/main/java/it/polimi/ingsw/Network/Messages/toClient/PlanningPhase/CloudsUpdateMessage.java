package it.polimi.ingsw.Network.Messages.toClient.PlanningPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.util.ArrayList;
import java.util.List;

public class CloudsUpdateMessage extends MessageToClient {
    private List<Cloud> clouds;

    public CloudsUpdateMessage(List<Cloud> clouds) {
        this.clouds = clouds;
    }

    @Override
    public void process(ServerHandler client) {
        CLI cliClient = (CLI) client.getClient();
        cliClient.getGame().set_clouds((ArrayList<Cloud>) clouds);
    }

    public void processGUI(ServerHandler client) {
        GUI guiClient = (GUI) client.getClient();
        guiClient.getGame().set_clouds((ArrayList<Cloud>) clouds);
    }

    public List<Cloud> getClouds() {
        return clouds;
    }
}
