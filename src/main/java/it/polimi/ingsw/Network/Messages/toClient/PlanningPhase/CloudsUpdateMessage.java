package it.polimi.ingsw.Network.Messages.toClient.PlanningPhase;

import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.util.ArrayList;

public class CloudsUpdateMessage extends MessageToClient {

    private ArrayList<Cloud> clouds;

    public CloudsUpdateMessage(ArrayList<Cloud> clouds) {
        this.clouds = clouds;
    }

    public ArrayList<Cloud> getClouds() {
        return new ArrayList<>(this.clouds);
    }

    @Override
    public void process(ServerHandler ch) {
        //TODO
    }

}
