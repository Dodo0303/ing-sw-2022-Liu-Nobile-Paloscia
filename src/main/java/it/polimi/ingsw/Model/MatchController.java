package it.polimi.ingsw.Model;

public class MatchController implements Runnable {
    private MatchStatus matchStatus;
    private final int totalMatchPlayers;
    private int currentPlayers;
    private final ClientHandler[] clients;

    public MatchController(int totalMatchPlayers) {
        this.totalMatchPlayers = totalMatchPlayers;
        this.clients = new ClientHandler[this.totalMatchPlayers];
        this.matchStatus = MatchStatus.MATCHMAKING;
        this.currentPlayers = 0;
    }

    public MatchStatus getStatus() { return this.matchStatus; }

    public void addPlayer(ClientHandler client) throws MatchMakingException {
        if (this.currentPlayers >= this.totalMatchPlayers) throw new MatchMakingException();
        clients[this.currentPlayers] = client;
        this.currentPlayers++;
        if (this.currentPlayers == this.totalMatchPlayers) {
            this.matchStatus = MatchStatus.STARTED;
            this.run();
        }

    }

    public void removePlayer() throws MatchMakingException {
        if (this.currentPlayers == 1) throw new MatchMakingException(); //!Public invariant issue: should minimum number of players be 0 or 1?
        this.currentPlayers--;
        clients[this.currentPlayers] = null; //TODO: replace with optional?
    }

    public void run() {
    }

}