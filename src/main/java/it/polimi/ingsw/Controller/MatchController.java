package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.*;

import static it.polimi.ingsw.Exceptions.GameException.error;

public class MatchController implements Runnable {
    //TODO Add match id
    private MatchStatus matchStatus;
    private final int totalMatchPlayers;
    private int currentPlayersNumber;
    private final ClientHandler[] clients;
    private final Wizard[] wizards;

    private GameModel game;

    private InfluenceCalculator influenceCalculator;

    public MatchController(int totalMatchPlayers) {
        this.totalMatchPlayers = totalMatchPlayers;
        this.clients = new ClientHandler[this.totalMatchPlayers];
        this.matchStatus = MatchStatus.MATCHMAKING;
        this.currentPlayersNumber = 0;
        this.wizards = new Wizard[this.totalMatchPlayers];
    }

    public MatchStatus getStatus() { return this.matchStatus; }

    public Wizard[] getWizards() { return this.wizards.clone(); }

    public GameModel getGame() {
        return this.game;
    }

    public int getTotalMatchPlayers() {
        return this.totalMatchPlayers;
    }


    public synchronized void addPlayer(ClientHandler client) throws MatchMakingException {
        if (this.currentPlayersNumber >= this.totalMatchPlayers) throw new MatchMakingException();
        clients[this.currentPlayersNumber] = client;
        this.currentPlayersNumber++;
        if (this.currentPlayersNumber == this.totalMatchPlayers) {
            this.matchStatus = MatchStatus.STARTED;
            notifyAll();
        }

    }

    public void removePlayer() throws MatchMakingException {
        if (this.currentPlayersNumber == 0) throw new MatchMakingException(); //!Public invariant issue: should minimum number of players be 0 or 1?
        this.currentPlayersNumber--;
        clients[this.currentPlayersNumber] = null; //TODO: replace with optional?
    }

    /**
     * Controller waits for players to provide a Wizard. Then a new GameModel is created.
     */
    private void gameSetup() {

        for (int i=0; i<this.totalMatchPlayers; i++) {
            while (!clients[i].wizardAvailable());//? Fare una wait?
            wizards[i] = clients[i].getWizard();
        }

        this.game = new GameModel(wizards, this.totalMatchPlayers);
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

        //TODO
    }

    /** This is a method for the Planning phase.
     * Draw 3/4 students from _bag and then place them on ONLY ONE cloud tile. Repeat this method for the 2nd and 3rd cloud tiles.
     */
    void addStudentsToCloud(Cloud cloud, int numOfPlayers){
        int x = (numOfPlayers == 3)? 4 : 3;
        try {
            for (int i = 0; i < x; i++) {
                cloud.addStudent(getGame().getBag().extractStudent());
            }
        } catch (FullCloudException e1) {
            throw error("the cloud is full.");
        } catch (EmptyBagException e2) {
            throw error("the bag is empty.");
        }
    }

    /** This is a method for the Planning phase.
     * Player PLAYER plays the assistant card ASSISTANT when other players are not playing the same card.
     */
    void playAssistant(Assistant assistant, Player player) {
        for (Player p : getGame().getPlayers()) {
            if (p != player && (p.getUsedAssistant() != null && assistant.getMaxSteps() == p.getUsedAssistant().getMaxSteps())) {
                throw error("The card is being used.");
            }
        }
        player.useAssistant(assistant);
    }

    /** This is a method for the Action phase.
     * The player PLAYER moves a student to the correspondent dining room.
     */
    public void moveStudentToDiningRoom(Player player, StudentColor student) {
        try{
            try{
                player.removeStudentFromEntrance(student);
            } catch (GameException e1) {
                throw error(e1.getMessage());
            }
            player.addToDiningTable(student);
        } catch (FullTableException e2) {
            throw error("The dining table is full.");
        }
    }

    /** This is a method for the Action phase.
     * The player PLAYER moves a student to the island ISLAND.
     */
    public void moveStudentToIsland(Player player, Island island, StudentColor student) {
        try {
            player.removeStudentFromEntrance(student);
        } catch (GameException e1) {
            throw error(e1.getMessage());
        }
        island.addStudent(student);
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

}