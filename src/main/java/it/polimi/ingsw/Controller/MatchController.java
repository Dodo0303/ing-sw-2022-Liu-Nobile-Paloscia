package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.InfluenceCalculators.InfluenceCalculator;
import it.polimi.ingsw.Controller.InfluenceCalculators.StandardInfluenceCalculator;
import it.polimi.ingsw.Controller.Phases.Phase;
import it.polimi.ingsw.Controller.Phases.PlanningPhase;
import it.polimi.ingsw.Controller.ProfessorChecker.ProfessorChecker;
import it.polimi.ingsw.Controller.ProfessorChecker.StandardProfessorChecker;
import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Network.Messages.toClient.ActionPhase.*;
import it.polimi.ingsw.Network.Messages.toClient.CharacterPhase.NoEntryMovedMessage;
import it.polimi.ingsw.Network.Messages.toClient.DropConnectionMessage;
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

/**
 * This class controls a whole match
 */
public class MatchController implements Runnable {
    /**
     * ID of the match
     */
    private final int ID;

    /**
     * Status of the match
     */
    private MatchStatus matchStatus;

    /**
     * Amount of players that will play this match
     */
    private final int totalMatchPlayers;

    /**
     * Number of players currently in the match
     */
    private int currentPlayersNumber;

    /**
     * Nickname of the current player
     */
    private String currentPlayerID;

    /**
     * Nickname of the first player of the turn
     */
    private String firstOfTurn;

    /**
     * List of clients connected to this match
     */
    private final ArrayList<ClientHandler> clients;

    /**
     * Wizards chosen in this match
     */
    private Wizard[] wizards;

    /**
     * Phase of the match
     */
    private Phase gamePhase;

    /**
     * True if the match is in expert mode. False if not.
     */
    private boolean expert;

    /**
     * Instance of the server
     */
    private EriantysServer server;

    /**
     * Instance of the GameModel of this match
     */
    private GameModel game;

    //NEEDED FOR CHARACTER USAGE
    /**
     * Instance of the influence calculator to be used
     */
    private InfluenceCalculator influenceCalculator;

    /**
     * Instance of the professor checker to be used
     */
    private ProfessorChecker professorChecker;

    /**
     * Additional moves that the current player is allowed to take during this turn
     */
    private int additionalMoves;


    public MatchController(int ID, int totalMatchPlayers, EriantysServer server) {
        this.ID = ID;
        this.totalMatchPlayers = totalMatchPlayers;
        this.clients = new ArrayList<>(this.totalMatchPlayers);
        this.wizards = new Wizard[this.totalMatchPlayers];
        this.matchStatus = MatchStatus.MATCHMAKING;
        this.currentPlayersNumber = 0;
        this.influenceCalculator = new StandardInfluenceCalculator();
        this.professorChecker = new StandardProfessorChecker();
        this.additionalMoves = 0;
        this.server = server;
    }

    // GETTERS AND SETTERS

    /**
     *
     * @return the status of the match
     */
    public MatchStatus getStatus() { return this.matchStatus; }

    /**
     *
     * @return the wizards chosen by the players
     */
    public Wizard[] getWizards() { return this.wizards.clone(); }

    /**
     *
     * @return the instance of the game model of this match
     */
    public GameModel getGame() {
        return this.game;
    }

    /**
     *
     * @return amount of players that will play this match
     */
    public int getTotalMatchPlayers() {
        return this.totalMatchPlayers;
    }

    /**
     *
     * @return nickname of the current player
     */
    public String getCurrentPlayerID() {
        return currentPlayerID;
    }

    /**
     *
     * @return instance of the current player
     */
    Player getCurrentPlayer() {
        for (Player p : this.game.getPlayers()) {
            if (p.getNickName().equals(this.currentPlayerID)) return p;
        }
        throw new MatchException("No current player.");
    }

    /**
     *
     * @return ID of the match
     */
    public int getID(){
        return ID;
    }

    /**
     *
     * @return the list of clients connected to this match
     */
    public List<ClientHandler> getClients() {
        return new ArrayList<>(clients);
    }

    /**
     *
     * @return the current phase of the match
     */
    public Phase getGamePhase() {
        return gamePhase;
    }

    /**
     * Set a new phase for this match
     * @param gamePhase phase to be set
     */
    public void setGamePhase(Phase gamePhase) {
        this.gamePhase = gamePhase;
    }

    /**
     *
     * @return nickname of the first player of the current turn
     */
    public String getFirstOfTurn() {
        return firstOfTurn;
    }

    /**
     * Set the instance of influence calculator to be used
     * @param influenceCalculator influence calculator to be set
     */
    public void setInfluenceCalculator(InfluenceCalculator influenceCalculator){
        this.influenceCalculator = influenceCalculator;
    }

    /**
     * Set the instance of professor checker to be used
     * @param professorChecker professor checker to be set
     */
    public void setProfessorChecker(ProfessorChecker professorChecker){
        this.professorChecker = professorChecker;
    }

    /**
     * Set the number of additional moves to 1
     */
    public void setAdditionalMoves() {
        this.additionalMoves = 1;
    }

    /**
     * Reset the attributes of the match related to character' effects to their default value.
     * This method is called every time a turn ends.
     */
    public void resetCharacterAttributes() {
        this.influenceCalculator = new StandardInfluenceCalculator();
        this.additionalMoves = 0;
        this.professorChecker = new StandardProfessorChecker();
    }

    /**
     * Checks whether the match is in expert mode or not.
     * @return true if the match is in expert mode. False if not.
     */
    public boolean isExpert() {
        return expert;
    }

    /**
     * Set the expert mode for this match
     * @param expert true if the match should be in expert mode. False if not.
     */
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


    /**
     * Moves a student to the dining table of the current player
     * @param studentIndex index of the player to be taken
     * @throws FullTableException if the table is already full
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

    /**
     * Checks whether a professor should be moved from its current owner
     * @param p player to be checked
     * @param color color fo the professor to check
     */
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
     * @throws WrongEffectException if the method is trying to call the wrong effect
     */
    public StudentColor useCharacter (String nickname, int characterId, int studentIndex, boolean removeCoins, StudentColor studentToAdd) throws WrongEffectException {
        if (removeCoins)
            removeCoinsToPlayer(nickname, characterId);
        return game.useEffectOfCharacter(characterId, studentIndex, studentToAdd);
    }


    /**
     * Add coins to a player
     * @param nickname nickname of the player that will receive the coins
     * @param coins amount of coins to be added
     */
    public void addCoinsToPlayer(String nickname, int coins){
        game.addCoinsToPlayer(nickname, coins);
    }

    /**
     * Add a no-entry tile to an island
     * @param islandID island that will receive the no-entry tile
     */
    public void addNoEntryToIsland(int islandID) {

        game.addNoEntry(game.getIslands().get(islandID));

    }

    /**
     * Methods used by the 3rd character. Computes the influence on an island without moving mother nature
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

        Player newOwnerOfTable = professorChecker.getNewOwnerOfProfessor(this.game, game.getPlayers(), table);
        Player newOwnerOfColor = professorChecker.getNewOwnerOfProfessor(this.game, game.getPlayers(), color);


        for (Player playerToCheck :
                game.getPlayers()) {
            if (playerToCheck.hasProfessor(color)) {
                playerToCheck.removeProfessor(color);
                broadCastProfessorsChange(playerToCheck.getNickName(), color, true);
                System.out.println("removed " + color + " to " + playerToCheck.getNickName());
            }
            if (playerToCheck.hasProfessor(table)) {
                playerToCheck.removeProfessor(table);
                broadCastProfessorsChange(playerToCheck.getNickName(), table, true);
                System.out.println("removed " + table + " to " + playerToCheck.getNickName());
            }
        }
        System.out.println("Il colore " + color + " deve averlo " + newOwnerOfColor);
        if(newOwnerOfColor != null) {
            newOwnerOfColor.addProfessor(color);
            broadCastProfessorsChange(newOwnerOfColor.getNickName(), color, false);
            System.out.println("added " + color + " to " + newOwnerOfColor.getNickName());
        }
        if(newOwnerOfTable != null) {
            newOwnerOfTable.addProfessor(table);
            broadCastProfessorsChange(newOwnerOfTable.getNickName(), table, false);
            System.out.println("added " + table + " to " + newOwnerOfTable.getNickName());
        }

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

    /**
     * Add a student to the entrance of the player
     * @param nickname nickname of the player that will receive the student
     * @param student student to be added
     */
    public void addStudentToEntrance(String nickname, StudentColor student){
        game.addStudentToEntrance(game.getPlayerByNickname(nickname), student);
    }

    /**
     * Add a student to a table
     * @param nickname nickname of the player that will receive the student
     * @param student student to be added
     * @throws FullTableException if the table is already full
     */
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

    /**
     * Move a student from the entrance of the current player to an island
     * @param islandID ID of the island that will receive the student
     * @param studentIndex index of the student to be moved
     * @throws IllegalArgumentException if the island ID is not valid
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

    /**
     * Move a student to an island
     * @param islandID ID of the island that will receive the student
     * @param color color of the student to be moved
     */
    public void moveStudentToIsland(int islandID, StudentColor color) {
        Island island = this.game.getIslands().get(islandID);
        game.addStudentToIsland(color, island);
    }


    /**
     * Move Mother Nature  to an island
     * @param x index of the island that will receive mother nature
     */
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
        }
    }

    /**
     * Take the students from a cloud and add them to the entrance of the current player
     * @param cloudID id of the cloud
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

    /**
     * Checks if an island can be owned by a player/team and set the tower color of the island.
     * @param x index of the island to be checked
     */
    private synchronized void controlIsland(int x) {
        Island island = this.game.getIslands().get(x);
        int influence;
        int whiteInfluence = 0;
        int blackInfluence = 0;
        int greyInfluence = 0;
        Color currentOwner = island.getTowerColor();
        int maxInfluence = 0;
        Color maxInfluencer = null;
        if (island.getNoEntries() == 0) {
            for (int i = 0; i < this.game.getPlayers().size(); i++) {
                if (!island.getTowerColor().equals(Color.VOID)) {
                    this.game.addTowersToColor(island.getNumTower(), island.getTowerColor());
                }
                influence = this.influenceCalculator.calculateInfluence(this.game.getPlayers().get(i), this.game.getIslands().get(x));
                if (this.game.getPlayers().get(i).getColor().equals(Color.WHITE)) {
                    whiteInfluence += influence;
                    if ((whiteInfluence > maxInfluence) || (whiteInfluence == maxInfluence && currentOwner.equals(Color.WHITE))) {
                        maxInfluence = whiteInfluence;
                        maxInfluencer = Color.WHITE;
                    } else if (whiteInfluence == maxInfluence && currentOwner.equals(Color.VOID)) {
                        maxInfluencer = null;
                    }
                } else if (this.game.getPlayers().get(i).getColor().equals(Color.GRAY)){
                    greyInfluence += influence;
                    if ((greyInfluence > maxInfluence) || (greyInfluence == maxInfluence && currentOwner.equals(Color.GRAY))) {
                        maxInfluence = greyInfluence;
                        maxInfluencer = Color.GRAY;
                    } else if (greyInfluence == maxInfluence && currentOwner.equals(Color.VOID)) {
                        maxInfluencer = null;
                    }
                } else {
                    blackInfluence += influence;
                    if ((blackInfluence > maxInfluence) || (blackInfluence == maxInfluence && currentOwner.equals(Color.BLACK))) {
                        maxInfluence = blackInfluence;
                        maxInfluencer = Color.BLACK;
                    } else if (blackInfluence == maxInfluence && currentOwner.equals(Color.VOID)) {
                        maxInfluencer = null;
                    }
                }
            }

            if (maxInfluencer != null) {
                island.setTowerColor(maxInfluencer);
                this.game.removeTowersToColor(island.getNumTower(), maxInfluencer);
                System.out.println("Island " + x + " conquered by " + maxInfluencer);
                System.out.println("New color: " + this.game.getIslands().get(x).getTowerColor());
            }


        } else {
            System.out.println("The island " + x + " had a no-entry on it");
            island.removeNoEntry();
            System.out.println("The island has now " + island.getNoEntries() + " no-entry tiles.");
            try {
                game.getCharacterById(5).addNoEntries();
                System.out.println(game.getCharacterById(5).getNumberOfNoEntries());
                MessageToClient msg = new NoEntryMovedMessage(1, game.getCharacterById(5), false);
                broadcastMessage(msg);
            } catch (WrongEffectException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks if the island can be merged with the adjacent ones
     * @param islandIndex index of the island to be checked
     */
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

    /** Merge two adjacent islands
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

    /**
     * Set the assistant chosen by the current player
     * @param assistant asisstant chosen by the current player
     * @throws GameException if the card was already used during the turn
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

    /**
     * Broadcast the updated clouds
     */
    public void broadcastClouds() {
        for (ClientHandler client : this.clients) {
            client.send(new CloudsUpdateMessage(new ArrayList<>(this.game.getClouds())));
        }
    }

    /**
     * Broadcast the change of turn
     * @param playerNickname nickname of the new current player
     * @param nextPhase phase that will follow
     */
    public void broadcastTurnChange(String playerNickname, String nextPhase) {
        for (ClientHandler client : this.clients) {
            client.send(new ChangeTurnMessage(playerNickname, nextPhase));
        }
    }

    /**
     * Broadcast the updated game model
     */
    private void broadcastGameModel(){
        MessageToClient gameModelMessage = new GameModelUpdateMessage(this.game);
        for (ClientHandler client :
                clients) {
            client.send(gameModelMessage);
        }
    }

    /**
     * Broadcast a professor change
     * @param playerID nickname of the target player
     * @param color color of the professor
     * @param remove true if the professor has to be removed from the target player. False if it has to be added
     */
    public void broadCastProfessorsChange(String playerID, StudentColor color, boolean remove) {
        for (ClientHandler client : this.clients) {
            client.send(new MoveProfessorMessage(color, remove, playerID));
        }
    }

    /**
     * Broadcast the disconnection of a player from the game
     * @param nickname nickname of the disconnected player
     */
    public void broadCastDisconnection(String nickname) {
        for (ClientHandler client : this.clients) {
            if (!nickname.equals(client.getNickname())) {
                client.send(new DropConnectionMessage(nickname));
            }
        }
        //TODO Delete the match from the server. Look at EndGame
    }

    /**
     *
     * @return the list of available wizards
     */
    public List<Wizard> getAvailableWizards(){
        List<Wizard> availableWizards = new ArrayList<>(Arrays.asList(Wizard.values()));
        availableWizards.removeAll(Arrays.asList(wizards));
        return availableWizards;
    }

    /**
     * Set a wizard to a player
     * @param player target player
     * @param wizard wizard to be set
     * @throws GameException if the wizard chosen is not available
     */
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

    /**
     * Broadcast the movement of a student from the entrance to an island/table
     * @param studentIndex index of the student ot be moved
     * @param playerID nickname of the player that has moved the student
     * @param destination '0' if the student has been moved to a table. '1' if it has been moved to an island
     * @param destinationID ID of the island chosen
     */
    public void broadcastMovementFromEntrance(int studentIndex, String playerID, int destination, int destinationID) {
        for (ClientHandler client : this.clients) {
            client.send(new ConfirmMovementFromEntranceMessage(studentIndex, playerID, destination, destinationID, game.getPlayerByNickname(playerID).getCoins()));
        }
    }

    /**
     * Broadcast the assistant chosen by a player
     * @param playerID nickname of the player that has chosen the assistant
     * @param assistantValue value of the chosen assistant
     */
    public void broadcastAssistant(String playerID, int assistantValue) {
        for (ClientHandler client : this.clients) {
            client.send(new UsedAssistantMessage(playerID, assistantValue));
        }
    }

    /**
     * This method handles the changing of the turn
     */
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

    /**
     *
     * @return true if all the players have successfully chosen a wizard. False if not
     */
    private boolean allWizardsAvailable(){
        for (Wizard wizard :
                wizards) {
            if (wizard == null)
                return false;
        }
        return true;
    }

    /**
     *
     * @param wizard wizard to be checked
     * @return true if the chosen wizard is available. False if not
     */
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

    /**
     * Draw and add 3/4 students from the bag and add them to the cloud
     * @param cloud cloud that will receive the students
     * @param numOfPlayers number of players playing this match
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

    /**
     * Sort the players based on the last assistant used
     */
    public void sortPlayers() {
        this.game.sortPlayers();
        this.currentPlayerID = game.getPlayers().get(0).getNickName();
        this.firstOfTurn = this.game.getPlayers().get(0).getNickName();
    }

    /**
     * Broadcast the movement of mother nature
     * @param islandIndex index of the island that has received mother nature
     */
    public void broadcastMovement(int islandIndex) {
        for (ClientHandler client : this.clients) {
            client.send(new ConfirmMovementMessage(islandIndex, game.getIslands(), currentPlayerID, getCurrentPlayer().getTowerNum()));
        }
    }

    /**
     * Broadcast the cloud chosen by the player
     * @param cloudID id of the cloud chosen
     */
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

    /**
     * Get the winner of this match, if exists
     * @return the player that won the game. Null if there is no winner
     */
    public Player getWinner() {

        Player winner = null;
        int maxTow = 10;
        int maxProf = 0;

        for (Player p : this.game.getPlayers()) {
            if (p.getTowerNum() < maxTow) {
                maxTow = p.getTowerNum();
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

    /**
     *
     * @return whether the game has ended during the phase 2 of the match
     */
    public int endedAtPhase2() {
        this.game.calculateNumIslandsForPlayers();
        for (Player player : game.getPlayers()) {
            if (player.getTowerNum() <= 0) {
                return 1;
            }
        }
        if (this.game.getIslands().size() <= 3) return 2;
        return 0;
    }

    /**
     * Broadcast the end of the game and the reason
     * @param reason reason that ended the game
     */
    public void endGame(String reason) {
        Player winner = this.getWinner();

        for (ClientHandler client : this.clients) {
            client.send(new EndMessage(winner.getNickName(), reason));
            server.removeClient(client);
        }
        server.removeMatch(this);
    }

    /**
     *
     * @return whether there are no students left in the bag
     */
    public boolean noMoreStudents() {
        return this.game.getBag().isEmpty();
    }

    /**
     *
     * @return whether there are no more assistants left
     */
    public boolean noMoreAssistants() {
        for (Player p : this.game.getPlayers()) {
            if (p.assistantsLeft() == 0) return true;
        }
        return false;
    }
}