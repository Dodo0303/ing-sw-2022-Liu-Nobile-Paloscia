package it.polimi.ingsw.Network.Messages.toClient.PlanningPhase;

import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Model.Assistant;
import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.util.HashMap;

public class ChangeTurnMessage extends MessageToClient {
    private Assistant[] assistants;
    private Phase phase;
    private int numStudentsToMove;
    private HashMap<StudentColor, Integer> entrance;
    private int numIslands;
    private HashMap<Integer, Island> islands;

    public ChangeTurnMessage(Phase phase) {
        this.phase = phase;
    }

    public ChangeTurnMessage(Assistant[] assistants, Phase phase) {//PlanningPhase
        this.assistants = assistants;
        this.phase = phase;
    }

    public ChangeTurnMessage(int numStudentsToMove, HashMap<StudentColor, Integer> entrance, int numIslands, Phase phase, HashMap<Integer, Island> islands) {//ActionPhase1
        this.numStudentsToMove = numStudentsToMove;
        this.phase = phase;
        this.entrance = entrance;
        this.numIslands = numIslands;
        this.islands = islands;
    }

    public ChangeTurnMessage(Phase phase, int numIslands, HashMap<Integer, Island> islands) {//ACtionphase 2
        this.phase = phase;
        this.numIslands = numIslands;
        this.islands = islands;
    }


    @Override
    public void process(ServerHandler client) {
        if(phase.equals(Phase.Planning)) {
            client.getClient().setAssistants(assistants);
        } else if(phase.equals((Phase.Action1))) {
            client.getClient().setNumStudentToMove(numStudentsToMove);
            client.getClient().setEntrance(entrance);
            client.getClient().setNumIslands(numIslands);
            client.getClient().setIslands(islands);
        }

    }

    public Phase getPhase() {
        return phase;
    }
}
