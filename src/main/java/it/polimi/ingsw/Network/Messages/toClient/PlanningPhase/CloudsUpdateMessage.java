package it.polimi.ingsw.Network.Messages.toClient.PlanningPhase;

import it.polimi.ingsw.Client.CLI.ServerHandler;
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
        client.getClient().getGame().set_clouds((ArrayList<Cloud>) clouds);
    }

    public void processGUI(it.polimi.ingsw.Client.GUI.ServerHandler client) {
        client.getClient().getGame().set_clouds((ArrayList<Cloud>) clouds);
        //todo update view
    }

    public List<Cloud> getClouds() {
        return clouds;
    }
}
