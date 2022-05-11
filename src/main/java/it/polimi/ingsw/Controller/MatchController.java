package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Phases.Phase;
import it.polimi.ingsw.Controller.Phases.PlanningPhase;
import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Network.Messages.toClient.ActionPhase.ChangeTurnMessage;
import it.polimi.ingsw.Network.Messages.toClient.ActionPhase.ConfirmMovementFromEntranceMessage;
import it.polimi.ingsw.Network.Messages.toClient.ActionPhase.DenyMovementMessage;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;
import it.polimi.ingsw.Network.Messages.toClient.PlanningPhase.CloudsUpdateMessage;
import it.polimi.ingsw.Network.Messages.toClient.PlanningPhase.UsedAssistantMessage;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static it.polimi.ingsw.Exceptions.GameException.error;

public class MatchController implements Runnable {
    private final int ID;

    private MatchStatus matchStatus;
    private final int totalMatchPlayers;
    private int currentPlayersNumber;
    private String currentPlayerID;
    private String firstOfTurn;
    private final ArrayList<ClientHandler> clients;
    private final List<Wizard> wizards;
    private Phase gamePhase;

    private GameModel game;

    private InfluenceCalculator influenceCalculator;



    public MatchController(int ID, int totalMatchPlayers) {
        this.ID = ID;
        this.totalMatchPlayers = totalMatchPlayers;
        this.clients = new ArrayList<>(this.totalMatchPlayers);
        this.wizards = new ArrayList<>(this.totalMatchPlayers);
        this.matchStatus = MatchStatus.MATCHMAKING;
        this.currentPlayersNumber = 0;
    }


    // GETTERS AND SETTERS

    public MatchStatus getStatus() { return this.matchStatus; }

    public GameModel getGame() {
        return this.game; //!Rep exposed
    }

    public int getTotalMatchPlayers() {
        return this.totalMatchPlayers;
    }

    public String getCurrentPlayerID() {
        return currentPlayerID;
    }

    Player getCurrentPlayer() {
        for (Player p : this.game.getPlayers()) {
            if (p.getNickName().equals(this.currentPlayerID)) return p;
            break;
        }
        throw new MatchException("No current player.");
    }

    public int getID(){
        return ID;
    }

    public List<ClientHandler> getClients() {
        return new ArrayList<>(clients);
    }

    public Phase getGamePhase() {
        return gamePhase;
    }

    public void setGamePhase(Phase gamePhase) {
        this.gamePhase = gamePhase;
    }

    public String getFirstOfTurn() {
        return firstOfTurn;
    }

    // PLAYERS

    /**
     * Adds a new player to the match. The new player <u>has no wizard assigned yet</u>. Player will be added only if
     * the new match is not full already.
     * @param client is the reference to the ClientHandler of the player to be added.
     * @throws MatchMakingException in case match is full.
     */
    public synchronized void addPlayer(ClientHandler client) throws MatchMakingException {
        if (this.currentPlayersNumber >= this.totalMatchPlayers) throw new MatchMakingException(); //TODO: should we add "|| this.status != MATCHMAKING"? Notice that if a player disconnects he should be the only one able to enter the match again.
        clients.add(client);
        this.currentPlayersNumber++;
        if (this.currentPlayersNumber == this.totalMatchPlayers) {
            this.matchStatus = MatchStatus.STARTED;
            notifyAll();
        }

    }

    public void removePlayer(ClientHandler client) throws MatchMakingException {
        if (this.currentPlayersNumber == 0) throw new MatchMakingException("Match has no players."); //!Public invariant issue: should minimum number of players be 0 or 1?
        if (!clients.remove(client)) throw new MatchMakingException("Match has no player named " + client.getNickname());
    }


    // GAME SETUP

    /**
     * Controller waits for players to provide a Wizard. Then a new GameModel is created.
     */
    private void gameSetup() {

        List<String> nicknames = new ArrayList<>(this.totalMatchPlayers);

        while(wizards.size() < totalMatchPlayers) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //Every player has selected a wizard

        //Create nicknames list
        for (ClientHandler client :
                clients) {
            nicknames.add(client.getNickname());
        }

        String[] nickArray = new String[totalMatchPlayers];
        Wizard[] wizardArray = new Wizard[totalMatchPlayers];


        this.game = new GameModel(this.totalMatchPlayers,nicknames.toArray(nickArray), wizards.toArray(wizardArray));

    }

    /**
     * This method handles the whole match, from initial setup to its end.
     */
    public synchronized void run() {
        while(matchStatus == MatchStatus.MATCHMAKING) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        gameSetup();

        int randomFirst = new Random().nextInt(this.totalMatchPlayers);
        this.currentPlayerID = this.game.getPlayers().get(randomFirst).getNickName();
        this.firstOfTurn = this.currentPlayerID;

        broadcastTurnChange(this.currentPlayerID, "PlanningPhase");

        this.gamePhase = new PlanningPhase(this); // Match now enters planning phase.

    }

    /**
     * This method checks whether the sender of a message is the current player, and sends the message to the correct handler
     * @param msg message sent by a player
     * @param sender handler of the player who sent the message
     */
    public void process(MessageToServer msg, ClientHandler sender){
        //Check that the sender is the current player
        if (isCurrent(sender)) {
            gamePhase.process(msg, sender);
        }
    }

    /**
     * @param ch handler of the player that has to be checked
     * @return whether the player is the current one or not
     */
    private boolean isCurrent(ClientHandler ch){
        return ch.getNickname().equals(this.currentPlayerID);
    }

    /**
     * Send to a player a message that denies its last movement
     * @param ch handler of the player
     */
    public void denyMovement(ClientHandler ch){
        MessageToClient msg = new DenyMovementMessage();
        try {
            ch.send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** This is a method for the Planning phase.
     * Draw 3/4 students from _bag and then place them on ONLY ONE cloud tile. Repeat this method for the other cloud tiles.
     */
    void addStudentsToCloud(Cloud cloud, int numOfPlayers) {
        int x = (numOfPlayers == 3)? 4 : 3;
        try {
            for (int i = 0; i < x; i++) {
                cloud.addStudent(getGame().getBag().extractStudent());
            }
        } catch (FullCloudException e1) {
            throw error("Cloud is full.");
        } catch (EmptyBagException e2) {
            throw error("Bag is empty.");
        }
    }

    /**
     * This is a method for Planning Phase.
     * It fills each cloud with students.
     */
    public void fillClouds() {
        for (Cloud cloud : this.game.getClouds()) {
            addStudentsToCloud(cloud, this.totalMatchPlayers);
        }
    }


    /** This is a method for the Action phase.
     * The player PLAYER moves a student to the correspondent dining room.
     */
    public void moveStudentToDiningRoom(StudentColor student) throws FullTableException {

        Player p = this.getCurrentPlayer();

        game.addToDiningTable(p, student);
        game.removeStudentFromEntrance(p, student);

        moveProfessorIfNeeded(p, student);

    }

    private void moveProfessorIfNeeded(Player p, StudentColor color) {

        if (this.game.getProfessors().contains(color)) {
            this.game.removeSpareProfessor(color);
            p.addProfessor(color);
        }

        if (!p.hasProfessor(color)) {
            for (Player p_other : this.game.getPlayers()) {

                boolean isOther = !p_other.getNickName().equals(p.getNickName());
                boolean hasProf = p_other.hasProfessor(color);
                boolean shouldNotHaveProf = this.game.getTableNumber(p_other, color) < this.game.getTableNumber(p, color);

                if (isOther && hasProf && shouldNotHaveProf) {
                    p_other.removeProfessor(color);
                    p.addProfessor(color);
                }
            }
        }
    }

    /** This is a method for the Action phase.
     * The player PLAYER moves a student to the island ISLAND.
     */
    public void moveStudentToIsland(int islandID, StudentColor student) throws IllegalArgumentException {

        Player p = this.getCurrentPlayer();
        Island island = this.game.getIslands().get(islandID);

        if (island == null) {
            throw new IllegalArgumentException();
        }

        game.addStudentToIsland(student, island);
        game.removeStudentFromEntrance(p, student);
    }


    /** This is a method for the Action phase.
     * Player PLAYER moves mothernature to xth island and try to control/conquer the xth island. */

    public void moveMotherNature(int x, Player player) {
        int distance;
        if (x > getGame().getNumIslands() - 1) {
            throw error("The chosen island does not exist.");
        } else if (x < getGame().getMotherNatureIndex()) {
            distance = x + getGame().getNumIslands() - getGame().getMotherNatureIndex();
        } else if (x > getGame().getMotherNatureIndex()) {
            distance = x - getGame().getMotherNatureIndex();
        } else {
            distance = 0;
        }
        if (distance > player.getUsedAssistant().getMaxSteps() || distance == 0) {
            throw error("The chosen island is too far away!");
        } else {
            getGame().setMothernature(x);
            controlIsland(x);
            unifyIslands(x);
        }
    }

    /** This is a method for the Action phase.
     * The player PLAYER takes 3/4 students from the cloud CLOUD, and then place them on his entrance.
     */
    public void takeStudentsFromCloud(Player player, Cloud cloud, int numOfPlayers) {
        int x = (numOfPlayers == 3)? 4 : 3;
        try {
            for (int i = 0; i < x; i++) {
                player.addStudentToEntrance(cloud.extractStudent());
            }
        } catch (EmptyCloudException e) {
            throw error("The cloud is empty");
        }
    }

    /** First, check if the xth island can be controlled/conquered.
     * If positive, then the color with most influence controls the island ISLAND.
     * If negative, do nothing. */
    public void controlIsland(int x) {
        Island island = getGame().getIslands().get(x);
        int influence;
        int maxInfluence = 0;
        Player maxInfluencer = null;
        for (int i = 0; i < getGame().getPlayers().size(); i++) {
            influence = influenceCalculator.calculateInfluence(getGame().getPlayers().get(i), getGame().getIslands().get(x));
            if (influence > maxInfluence) {
                maxInfluencer = getGame().getPlayers().get(i);
                maxInfluence = influence;
            }
        }
        if (maxInfluencer != null) {
            island.setTowerColor(maxInfluencer.getColor());
        }

    }

    /** First, check if the xth island can merge any adjacent island.
     * If positive, then call mergeIslands().
     * If negative, do nothing. */
    public void unifyIslands(int x) {
        Island island = getGame().getIslands().get(x);
        int left = (x > 0) ? x - 1 : getGame().getNumIslands() - 1;
        if (island.getTowerColor() != Color.VOID && getGame().getIslands().get(left).getTowerColor().equals(island.getTowerColor())) {
            mergeIslands(left, x--);
        }
        int right = (x < getGame().getNumIslands() - 1) ? x + 1 : 0;
        if (island.getTowerColor() != Color.VOID && getGame().getIslands().get(right).getTowerColor().equals(island.getTowerColor())) {
            mergeIslands(x, right);
        }
    }


    /** In the case that x == numIslands - 1(ex. x = 11, y = 0), use the yth island to merge the xth island, just like deleting the tail node of a linked list.
     * @param x the index of one of the islands to be merged.
     * @param y the index of one of the islands to be merged.
     */
    public void mergeIslands(int x, int y) {
        if (x == getGame().getNumIslands() - 1) {
            getGame().getIslands().get(y).copyFrom(getGame().getIslands().get(x));
            if (x == getGame().getMotherNatureIndex()) {
                getGame().setMothernature(y);
            }
        }
        else {
            getGame().getIslands().get(x).copyFrom(getGame().getIslands().get(y));
            if (y == getGame().getMotherNatureIndex()) {
                getGame().setMothernature(x);
            }
            for (int i = y; i < getGame().getNumIslands() - 1; i++) {
                getGame().getIslands().put(i, getGame().getIslands().get(i + 1)); // move islands after the yth forward by 1.
            }
        }
        getGame().getIslands().remove(getGame().setNumIslands(getGame().getNumIslands()) - 1);
    }

    /** This is a method for the Planning phase.
     * Player PLAYER plays the assistant card ASSISTANT when other players are not playing the same card.
     */
    public void setAssistantOfCurrentPlayer(Assistant assistant) throws GameException {

        for (Player p : this.game.getPlayers()) {
            if (!this.currentPlayerID.equals(p.getNickName())) {
                if  (p.getUsedAssistant() != null && assistant.getMaxSteps() == p.getUsedAssistant().getMaxSteps() && !p.lastAssistant()) {
                    throw new GameException("Card was already used.");
                }
            } else break;
        }
        game.setAssistantOfPlayer(this.currentPlayerID, assistant);
    }

    public void broadcastClouds() {
        for (ClientHandler client : this.clients) {
            try {
                client.send(new CloudsUpdateMessage(new ArrayList<>(this.game.getClouds())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastTurnChange(String playerNickname, String nextPhase) {
        for (ClientHandler client : this.clients) {
            try {
                client.send(new ChangeTurnMessage(playerNickname, nextPhase));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public List<Wizard> getAvailableWizards(){
        List<Wizard> availableWizards = new ArrayList<>(Arrays.asList(Wizard.values()));
        availableWizards.removeAll(wizards);
        return availableWizards;
    }

    public boolean isWizardAvailable(Wizard wizard) {
        for (Wizard wizardChecked : wizards) {
            if (wizardChecked == wizard)
                return false;
        }
        return true;
    }

    public void nextTurn() {
        int nextPlayerIndex = (this.game.getPlayerIndexFromNickname(this.currentPlayerID) + 1) % this.totalMatchPlayers;
        this.currentPlayerID = this.game.getPlayers().get(nextPlayerIndex).getNickName();
    }



    public void setWizardOfPlayer(ClientHandler player, Wizard wizard) throws GameException{
        if (isWizardAvailable(wizard)) {
            int index = clients.indexOf(player);
            wizards.set(index, wizard);

            //Broadcast the change in wizards available
            for (ClientHandler client :
                    clients) {
                try {
                    client.sendAvailableWizards();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            throw new GameException("Wizard not available");
        }
    }

    public void broadcastMovementFromEntrance(StudentColor student, String playerID, int destination, int destinationID) {
        for (ClientHandler client : this.clients) {
            try {
                client.send(new ConfirmMovementFromEntranceMessage(student, playerID, destination, destinationID));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastAssistant(String playerID, int assistantValue) {
        for (ClientHandler client : this.clients) {
            try {
                client.send(new UsedAssistantMessage(playerID, assistantValue));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}