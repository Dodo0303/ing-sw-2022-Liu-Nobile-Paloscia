package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.InfluenceCalculators.InfluenceCalculator;
import it.polimi.ingsw.Controller.InfluenceCalculators.StandardInfluenceCalculator;
import it.polimi.ingsw.Controller.Phases.Phase;
import it.polimi.ingsw.Controller.Phases.PlanningPhase;
import it.polimi.ingsw.Controller.ProfessorChecker.ProfessorChecker;
import it.polimi.ingsw.Controller.ProfessorChecker.StandardProfessorChecker;
import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Character.CharacterCard;
import it.polimi.ingsw.Network.Messages.toClient.ActionPhase.*;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.GameModelUpdateMessage;
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
    private Wizard[] wizards;
    private Phase gamePhase;
    private boolean expert;

    private GameModel game;

    //NEEDED FOR CHARACTER USAGE
    private InfluenceCalculator influenceCalculator;
    private ProfessorChecker professorChecker;
    private int additionalMoves;


    private boolean lastRound;


    public MatchController(int ID, int totalMatchPlayers) {
        this.ID = ID;
        this.totalMatchPlayers = totalMatchPlayers;
        this.clients = new ArrayList<>(this.totalMatchPlayers);
        this.wizards = new Wizard[this.totalMatchPlayers];
        this.matchStatus = MatchStatus.MATCHMAKING;
        this.currentPlayersNumber = 0;
        this.lastRound = false;
        this.influenceCalculator = new StandardInfluenceCalculator();
        this.professorChecker = new StandardProfessorChecker();
        this.additionalMoves = 0;
    }

    // GETTERS AND SETTERS

    public MatchStatus getStatus() { return this.matchStatus; }

    public Wizard[] getWizards() { return this.wizards.clone(); }

    public GameModel getGame() {
        return this.game;
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

    public void setInfluenceCalculator(InfluenceCalculator influenceCalculator){
        this.influenceCalculator = influenceCalculator;
    }

    public void setProfessorChecker(ProfessorChecker professorChecker){
        this.professorChecker = professorChecker;
    }

    public void setAdditionalMoves() {
        this.additionalMoves = 1;
    }

    public void resetCharacterAttributes() {
        this.influenceCalculator = new StandardInfluenceCalculator();
        this.additionalMoves = 0;
        this.professorChecker = new StandardProfessorChecker();
    }

    public boolean isExpert() {
        return expert;
    }

    public void setExpert(boolean expert) {
        this.expert = expert;
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

        while(!allWizardsAvailable()) {
            try {
                wait(500);
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

        this.game = new GameModel(this.totalMatchPlayers,nicknames.toArray(nickArray), wizards);

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

        //Here the server must send the game model to all the clients
        broadcastGameModel();

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
     * Send to a player a message that denies its last movement
     * @param ch handler of the player
     */
    public void denyMovement(ClientHandler ch){
        MessageToClient msg = new DenyMovementMessage();
        ch.send(msg);
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
    public void moveStudentToDiningRoom(int studentIndex) throws FullTableException {

        Player p = this.getCurrentPlayer();

        StudentColor color = game.removeStudentFromEntrance(p, studentIndex);
        game.addToDiningTable(p, color);
        int numStudents = game.getPlayerByNickname(getCurrentPlayer().getNickName()).getDiningTables().get(color).getNumOfStudents();
        if (numStudents == 3 || numStudents == 6 || numStudents == 9) {
            addCoinsToPlayer(getCurrentPlayer().getNickName(), 1);
        }

        moveProfessorIfNeeded(p, color);

    }

    private void moveProfessorIfNeeded(Player p, StudentColor color) {

        if (this.game.getProfessors().contains(color)) {
            this.game.removeSpareProfessor(color);
            p.addProfessor(color);
            broadCastProfessorsChange(p.getNickName(), color, false);
        }

        if (!p.hasProfessor(color)) {
            for (Player p_other : this.game.getPlayers()) {

                boolean isOther = !p_other.getNickName().equals(p.getNickName());
                boolean hasProf = p_other.hasProfessor(color);
                boolean shouldNotHaveProf = professorChecker.shouldSwapProfessor(game, p, p_other, color);

                if (isOther && hasProf && shouldNotHaveProf) {
                    p_other.removeProfessor(color);
                    p.addProfessor(color);
                    broadCastProfessorsChange(p_other.getNickName(), color, true);
                    broadCastProfessorsChange(p.getNickName(), color, false);
                }
            }
        }

    }
    /**
     * Method called when a player uses a character. It removes the price of the character from the money of the player
     * @param nickname of the player that is using the character
     * @param characterID ID of the character used
     */
    private void removeCoinsToPlayer(String nickname, int characterID){
        game.removeCoinsToPlayer(nickname, characterID);
    }

    /**
     * Remove the coins from the player that is using the character and, if necessary, increments the price of the character
     * @param nickname nickname of the player that is using the character
     * @param characterID character used
     * @throws WrongEffectException if the method is trying to use an incorrect effect
     * @throws NotEnoughNoEntriesException in case the character is the number 5 and there are no no-entry tiles left
     */
    public void useCharacter(String nickname, int characterID) throws WrongEffectException, NotEnoughNoEntriesException {
        removeCoinsToPlayer(nickname, characterID);
        game.useEffectOfCharacter(characterID);
    }

    /**
     * Remove the coins from the player that is using the character and returns the color of the student extracted from the character. Then replace that student with a random one from the bag.
     * @param nickname nickname of the player that is using the character
     * @param characterId ID of the character to be used
     * @param studentIndex index of the student that has to be extracted from the character
     * @param removeCoins true if the method should remove the coins from the player
     * @return the color of the student extracted
     * @throws EmptyBagException if there are no more students in the bag and the character can't be filled again
     * @throws WrongEffectException if the method is trying to call the wrong effect
     */
    public StudentColor useCharacter(String nickname, int characterId, int studentIndex, boolean removeCoins) throws EmptyBagException, WrongEffectException {
        if (removeCoins)
            removeCoinsToPlayer(nickname, characterId);
        return game.useEffectOfCharacter(characterId, studentIndex, game.drawStudentFromBag());
    }

    /**
     * Remove the coins from the player that is using the character and returns the color of the student extracted from the character. Then replace that student with a given one.
     * @param nickname nickname of the player that is using the character
     * @param characterId ID of the character to be used
     * @param studentIndex index of the student that has to be extracted from the character
     * @param removeCoins true if the method should remove the coins from the player
     * @param studentToAdd student that will replace the one extracted
     * @return the color of the student extracted
     * @throws EmptyBagException if there are no more students in the bag and the character can't be filled again
     * @throws WrongEffectException if the method is trying to call the wrong effect
     */
    public StudentColor useCharacter (String nickname, int characterId, int studentIndex, boolean removeCoins, StudentColor studentToAdd) throws WrongEffectException {
        if (removeCoins)
            removeCoinsToPlayer(nickname, characterId);
        return game.useEffectOfCharacter(characterId, studentIndex, studentToAdd);
    }


    public void addCoinsToPlayer(String nickname, int coins){
        game.addCoinsToPlayer(nickname, coins);
    }

    public void addNoEntryToIsland(int islandID) throws GameException, NotEnoughNoEntriesException {
        CharacterCard character = game.getCharacterById(5);
        try {
            character.useEffect();
            game.addNoEntry(game.getIslands().get(islandID));
        } catch (NotEnoughNoEntriesException e) {
            throw new NotEnoughNoEntriesException();
        } catch (WrongEffectException e) {
            e.printStackTrace();
        }
    }

    /**
     * Methods used by the 3nd character. Computes the influence on an island without moving mother nature
     * @param islandID island in which computing the influence
     */
    public void conquerAndJoinIslands(int islandID) {
        controlIsland(islandID);
        unifyIslands(islandID);
    }

    /**
     * Method used by 10th character. It swaps two students between the entrance and the table
     * @param nickname nickname of the player using the character
     * @param entrancePosition index of the student that the player wants to remove from the entrance
     * @param table color of the student that the player wants to remove from the table
     * @throws EmptyTableException if the table chosen is empty
     * @throws FullTableException if the table chosen is full
     */
    public void swapStudentsEntranceAndTable(String nickname, int entrancePosition, StudentColor table) throws EmptyTableException, FullTableException {
        Player player = game.getPlayerByNickname(nickname);
        StudentColor color = game.removeStudentFromEntrance(player, entrancePosition );
        game.removeStudentFromTable(player, table);
        game.addStudentToEntrance(player, table);
        game.addToDiningTable(player, color);
    }

    /**
     * Remove the student from the entrance, given its index
     * @param nickname player that wants to remove a student
     * @param entrancePosition index of the student to be removed
     * @return the color of the student removed
     */
    public StudentColor removeStudentFromEntrance(String nickname, int entrancePosition) {
        return game.removeStudentFromEntrance(game.getPlayerByNickname(nickname), entrancePosition);
    }

    public void addStudentToEntrance(String nickname, StudentColor student){
        game.addStudentToEntrance(game.getPlayerByNickname(nickname), student);
    }

    public void addStudentToTable(String nickname, StudentColor student) throws FullTableException {
        game.addToDiningTable(game.getPlayerByNickname(nickname), student);
    }

    /**
     * Method used by 12th character. It removes three student of the chosen color from the tables of every player
     * @param color color of the students to be removed
     */
    public void removeThreeStudentsFromTables(StudentColor color){
        for (ClientHandler client :
                clients) {
            try {
                for (int i = 0; i < 3; i++) {
                    game.removeStudentFromTable(game.getPlayerByNickname(client.getNickname()), color);
                }

            } catch (EmptyTableException ignored) {}
        }
    }

    /** This is a method for the Action phase.
     * The player PLAYER moves a student to the island ISLAND.
     */
    public void moveStudentFromEntranceToIsland(int islandID, int studentIndex) throws IllegalArgumentException {

        Player p = this.getCurrentPlayer();
        Island island = this.game.getIslands().get(islandID);

        if (island == null) {
            throw new IllegalArgumentException();
        }
        StudentColor color = game.removeStudentFromEntrance(p, studentIndex);

        game.addStudentToIsland(color, island);
    }

    public void moveStudentToIsland(int islandID, StudentColor color) {
        Island island = this.game.getIslands().get(islandID);
        game.addStudentToIsland(color, island);
    }


    /** This is a method for the Action phase.
     * Player PLAYER moves mothernature to xth island and tries to control/conquer the xth island. */
    public void moveMotherNature(int x) {
        int distance;
        Player p = this.getCurrentPlayer();

        if (x > this.game.getNumIslands() - 1) {
            throw error("The chosen island does not exist.");
        } else if (x < this.game.getMotherNatureIndex()) {
            distance = x + this.game.getNumIslands() - this.game.getMotherNatureIndex();
        } else if (x > this.game.getMotherNatureIndex()) {
            distance = x - this.game.getMotherNatureIndex();
        } else {
            distance = 0;
        }

        if (distance > p.getUsedAssistant().getMaxSteps() + additionalMoves*2 || distance == 0) {
            throw error("The chosen island is too far away!");
        } else {
            this.game.setMothernature(x);
            controlIsland(x);
            unifyIslands(x);
            this.game.calculateNumIslandsForPlayers();
        }
    }

    /** This is a method for the Action phase.
     * The player PLAYER takes 3/4 students from the cloud CLOUD, and then place them on his entrance.
     */
    public void takeStudentsFromCloud(int cloudID) {

        Cloud cloud = this.game.getClouds().get(cloudID);
        Player p = this.getCurrentPlayer();

        int x = (this.totalMatchPlayers == 3)? 4 : 3;
        try {
            for (int i = 0; i < x; i++) {
                p.addStudentToEntrance(cloud.extractStudent());
            }
        } catch (EmptyCloudException e) {
            throw error("The cloud is empty");
        }
    }

    /** First, check if the xth island can be controlled/conquered.
     * If positive, then the color with most influence controls the island ISLAND.
     * If negative, do nothing. */
    private synchronized void controlIsland(int x) {
        Island island = this.game.getIslands().get(x);
        int influence;
        int maxInfluence = 0;
        Player maxInfluencer = null;
        for (int i = 0; i < this.game.getPlayers().size(); i++) {
            influence = this.influenceCalculator.calculateInfluence(this.game.getPlayers().get(i), this.game.getIslands().get(x));
            if (influence > maxInfluence) {
                maxInfluencer = this.game.getPlayers().get(i);
                maxInfluence = influence;
            }
        }
        if (maxInfluencer != null) {
            island.setTowerColor(maxInfluencer.getColor());
            System.out.println("Island " + x + " conquered by " + maxInfluencer.getColor());
            System.out.println("New color: " + this.game.getIslands().get(x).getTowerColor());
        }
    }

    /** First, check if the xth island can merge any adjacent island.
     * If positive, then call mergeIslands().
     * If negative, do nothing. */
    private synchronized void unifyIslands(int islandIndex) {
        Island island = this.game.getIslands().get(islandIndex);
        int left = (islandIndex > 0) ? islandIndex - 1 : this.game.getNumIslands() - 1;
        System.out.println("Checking the color of island " + left);//todo delete after tested
        if (island.getTowerColor() != Color.VOID && this.game.getIslands().get(left).getTowerColor().equals(island.getTowerColor())) {
            mergeIslands(left, islandIndex);
            if (islandIndex > 0) islandIndex--;
        }
        int right = (islandIndex < this.game.getNumIslands() - 1) ? islandIndex + 1 : 0;
        if (island.getTowerColor() != Color.VOID && this.game.getIslands().get(right).getTowerColor().equals(island.getTowerColor())) {
            mergeIslands(islandIndex, right);
        }
    }

    /** In the case that x == numIslands - 1 (e.g. x = 11, y = 0), use the yth island to merge the xth island, just like deleting the tail node of a linked list.
     * @param x the index of one of the islands to be merged.
     * @param y the index of one of the islands to be merged.
     */
    private synchronized void mergeIslands(int x, int y) {
        if (x == this.game.getNumIslands() - 1) {
            this.game.getIslands().get(y).copyFrom(this.game.getIslands().get(x));
            if (x == this.game.getMotherNatureIndex()) {
                this.game.setMothernature(y);
            }
            System.out.println(y + " merged " + x);//todo delete after tested
        } else {
            this.game.getIslands().get(x).copyFrom(this.game.getIslands().get(y));
            if (y == this.game.getMotherNatureIndex()) {
                this.game.setMothernature(x);
            }
            for (int i = y; i < this.game.getNumIslands() - 1; i++) {
                this.game.getIslands().put(i, this.game.getIslands().get(i + 1)); // move islands after the yth forward by 1.
            }
            System.out.println(x + " merged " + y);//todo delete after tested
        }
        this.game.getIslands().remove(this.game.getNumIslands() - 1);
    }

    /** This is a method for the Planning phase.
     * Player PLAYER plays the assistant card ASSISTANT when other players are not playing the same card.
     */
    public void setAssistantOfCurrentPlayer(Assistant assistant) throws GameException {

        for (Player p : this.game.getPlayers()) {
            if (!this.currentPlayerID.equals(p.getNickName())) {
                if  (p.getUsedAssistant() != null && assistant.getValue() == p.getUsedAssistant().getValue() && !this.getCurrentPlayer().lastAssistant()) {
                    throw new GameException("Card was already used.");
                }
            } else break;
        }
        game.setAssistantOfPlayer(this.currentPlayerID, assistant);
    }

    public void broadcastClouds() {
        for (ClientHandler client : this.clients) {
            client.send(new CloudsUpdateMessage(new ArrayList<>(this.game.getClouds())));
        }
    }

    public void broadcastTurnChange(String playerNickname, String nextPhase) {
        for (ClientHandler client : this.clients) {
            client.send(new ChangeTurnMessage(playerNickname, nextPhase));
        }
    }

    private void broadcastGameModel(){
        MessageToClient gameModelMessage = new GameModelUpdateMessage(this.game);
        for (ClientHandler client :
                clients) {
            client.send(gameModelMessage);
        }
    }

    public void broadCastProfessorsChange(String playerID, StudentColor color, boolean remove) {
        for (ClientHandler client : this.clients) {
            client.send(new MoveProfessorMessage(color, remove, playerID));
        }
    }

    public List<Wizard> getAvailableWizards(){
        List<Wizard> availableWizards = new ArrayList<>(Arrays.asList(Wizard.values()));
        availableWizards.removeAll(Arrays.asList(wizards));
        return availableWizards;
    }

    public void setWizardOfPlayer(ClientHandler player, Wizard wizard) throws GameException{
        if (isWizardAvailable(wizard)) {
            int index = clients.indexOf(player);
            wizards[index] = wizard;

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
            throw new GameException("Wizard " + wizard.toString() + " not available");
        }
    }

    public void broadcastMovementFromEntrance(int studentIndex, String playerID, int destination, int destinationID) {
        for (ClientHandler client : this.clients) {
            client.send(new ConfirmMovementFromEntranceMessage(studentIndex, playerID, destination, destinationID, game.getPlayerByNickname(playerID).getCoins()));
        }
    }

    public void broadcastAssistant(String playerID, int assistantValue) {
        for (ClientHandler client : this.clients) {
            client.send(new UsedAssistantMessage(playerID, assistantValue));
        }
    }

    public void nextTurn() {
        int nextPlayerIndex = (this.game.getPlayerIndexFromNickname(this.currentPlayerID) + 1) % this.totalMatchPlayers;
        this.currentPlayerID = this.game.getPlayers().get(nextPlayerIndex).getNickName();
    }

    /**
     * Checks if the current player has enough money to use that character
     * @param characterID character to check
     * @param player player to check
     * @return true if the player can use that character, false if not
     */
    public boolean isCharacterAvailable(int characterID, String player){
         if (game.canAffordCharacter(player, characterID)){
             return true;
         }
         return false;
    }

    // UTILS

    private boolean allWizardsAvailable(){
        for (Wizard wizard :
                wizards) {
            if (wizard == null)
                return false;
        }
        return true;
    }

    public boolean isWizardAvailable(Wizard wizard) {
        for (Wizard wizardChecked : wizards) {
            if (wizardChecked == wizard) {
                System.out.println(wizardChecked.toString() + " already taken");
                return false;
            }
        }
        return true;
    }

    /**
     * @param ch handler of the player that has to be checked
     * @return whether the player is the current one or not
     */
    private boolean isCurrent(ClientHandler ch){
        return ch.getNickname().equals(this.currentPlayerID);
    }

    /** This is a method for the Planning phase.
     * Draw 3/4 students from _bag and then place them on ONLY ONE cloud tile. Repeat this method for the other cloud tiles.
     */
    protected void addStudentsToCloud(Cloud cloud, int numOfPlayers){
        int x = (numOfPlayers == 3)? 4 : 3;
        try {
            for (int i = 0; i < x; i++) {
                cloud.addStudent(game.getBag().extractStudent());
            }
        } catch (FullCloudException e1) {
            throw error("Cloud is full.");
        } catch (EmptyBagException e2) {
            throw error("Bag is empty.");
        }
    }

    public void sortPlayers() {
        this.game.sortPlayers();
        this.currentPlayerID = game.getPlayers().get(0).getNickName();
        this.firstOfTurn = this.game.getPlayers().get(0).getNickName();
    }

    public void broadcastMovement(int islandIndex) {
        for (ClientHandler client : this.clients) {
            client.send(new ConfirmMovementMessage(islandIndex, game.getIslands(), currentPlayerID, getCurrentPlayer().getTowerNum()));
        }
    }

    public void broadCastCloudChoice(int cloudID) {
        for (ClientHandler client : this.clients) {
            client.send(new ConfirmCloudMessage(this.currentPlayerID, cloudID));
        }
    }

    /**
     * Broadcast any message to all the clients of this match
     * @param message message to be sent
     */
    public void broadcastMessage(MessageToClient message) {
        for (ClientHandler client :
                clients) {
            client.send(message);
        }
    }

    public Player getWinner() {

        Player winner = null;
        int maxTow = 0;
        int maxProf = 0;

        for (Player p : this.game.getPlayers()) {
            if (p.getTowerNum() > maxTow) {
                maxTow = p.getMaxTowerNum();
                maxProf = p.getProfessors().size();
                winner = p;
            } else if (p.getTowerNum() == maxTow) {
                if (p.getProfessors().size() > maxProf) {
                    maxProf = p.getProfessors().size();
                    winner = p;
                }
            }
        }

        return winner;
    }

    public int endedAtPhase2() {
        if (this.getCurrentPlayer().getTowerNum() == 0) return 1;
        if (this.game.getIslands().size() <= 3) return 2;
        return 0;
    }

    public void endGame(String reason) {
        Player winner = this.getWinner();

        for (ClientHandler client : this.clients) {
            client.send(new EndMessage(winner.getNickName(), reason));

            //TODO: Remove from server.
        }
    }

    public boolean noMoreStudents() {
        return this.game.getBag().isEmpty();
    }

    public boolean noMoreAssistants() {
        for (Player p : this.game.getPlayers()) {
            if (p.getAssistants().size() == 0) return true;
        }
        return false;
    }
}