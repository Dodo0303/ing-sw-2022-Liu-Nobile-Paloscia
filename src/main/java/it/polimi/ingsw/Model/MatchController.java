package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class MatchController implements Runnable {
    private MatchStatus matchStatus;
    private final int totalMatchPlayers;
    private int currentPlayersNumber;
    private final ClientHandler[] clients;
    private final Wizard[] wizards;

    private GameModel game;

    public MatchController(int totalMatchPlayers) {
        this.totalMatchPlayers = totalMatchPlayers;
        this.clients = new ClientHandler[this.totalMatchPlayers];
        this.matchStatus = MatchStatus.MATCHMAKING;
        this.currentPlayersNumber = 0;
        this.wizards = new Wizard[this.totalMatchPlayers];
    }

    public MatchStatus getStatus() { return this.matchStatus; }

    public Wizard[] getWizards() { return this.wizards.clone(); }

    public void addPlayer(ClientHandler client) throws MatchMakingException {
        if (this.currentPlayersNumber >= this.totalMatchPlayers) throw new MatchMakingException();
        clients[this.currentPlayersNumber] = client;
        this.currentPlayersNumber++;
        if (this.currentPlayersNumber == this.totalMatchPlayers) {
            this.matchStatus = MatchStatus.STARTED;
            this.run();
        }

    }

    public void removePlayer() throws MatchMakingException {
        if (this.currentPlayersNumber == 1) throw new MatchMakingException(); //!Public invariant issue: should minimum number of players be 0 or 1?
        this.currentPlayersNumber--;
        clients[this.currentPlayersNumber] = null; //TODO: replace with optional?
    }

    /**
     * Controller waits for players to provide a Wizard. Then a new GameModel is created.
     */
    private void gameSetup() {

        for (int i=0; i<this.totalMatchPlayers; i++) {
            while (!clients[i].wizardAvailable());
            wizards[i] = clients[i].getWizard();
        }

        this.game = new GameModel(wizards, this.totalMatchPlayers);
    }

    /**
     * This method handles the whole match, from initial setup to its end.
     */
    public void run() {
        gameSetup();

        //TODO
    }

}