package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.GUI.Controllers.*;
import it.polimi.ingsw.Client.GUI.Controllers.Joining.*;
import it.polimi.ingsw.Exceptions.EmptyCloudException;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Exceptions.NotEnoughNoEntriesException;
import it.polimi.ingsw.Exceptions.WrongEffectException;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Network.Messages.toClient.ActionPhase.*;
import it.polimi.ingsw.Network.Messages.toClient.CharacterPhase.*;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.*;
import it.polimi.ingsw.Network.Messages.toClient.PlanningPhase.CloudsUpdateMessage;
import it.polimi.ingsw.Network.Messages.toClient.PlanningPhase.UsedAssistantMessage;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GUI {
    private GameModel game;
    private final Stage stage;
    private ServerHandler serverHandler;
    private Phase_GUI currPhase;
    private String nickname;
    private String host;
    private int port;
    private int numPlayer;
    private boolean expert;
    private List<Wizard> wizards;
    private int ap1Moves, changeTurnNums;
    private boolean myTurn;
    private Phase_GUI prevPhase;
    private int currCharacter;
    private ArrayList<String> playerPlayedAssistant;

    public GUI(Stage stage) {
        this.stage = stage;
    }

    public void start() {
        try {
            ap1Moves = 0;
            stage.getIcons().add(new Image("icon.png"));
            stage.setResizable(false);
            stage.setTitle("Eriantys");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = fxmlLoader.load();
            LoginController loginController = fxmlLoader.getController();
            loginController.setGUI(this);
            Scene scene = new Scene(root, 600, 402);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/login.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean settingUpConnection(String host, int port) {
        try {
            serverHandler = new ServerHandler(host, port, this);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public void requireNickname(boolean reset){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/nickname.fxml"));
            Parent root = fxmlLoader.load();
            NicknameController nicknameController = fxmlLoader.getController();
            nicknameController.setGUI(this);
            if (reset) {
                nicknameController.setMessage("Nickname has been taken.");
            }
            Scene scene = new Scene(root, 600, 402);
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    stage.setScene(scene);
                    stage.show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chooseGameMode(String msg) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/chooseGameMode.fxml"));
            Parent root = fxmlLoader.load();
            ChooseGameModeController chooseGameModeController = fxmlLoader.getController();
            chooseGameModeController.setGUI(this);
            if (msg != null) {
                chooseGameModeController.setMessage(msg);
            }
            Scene scene = new Scene(root, 600, 402);
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    stage.setScene(scene);
                    stage.show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newgame() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/newgame.fxml"));
            Parent root = fxmlLoader.load();
            NewgameController newgameController = fxmlLoader.getController();
            newgameController.setGUI(this);
            Scene scene = new Scene(root, 600, 402);
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    stage.setScene(scene);
                    stage.show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void joinGame(String msg, List<Integer> matches) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/JoinGame.fxml"));
            Parent root = fxmlLoader.load();
            JoinGameController joinGameController = fxmlLoader.getController();
            joinGameController.setGUI(this);
            joinGameController.setMatches(matches);
            if (msg != null) {
                joinGameController.setMessage(msg);
            }
            Scene scene = new Scene(root, 600, 402);
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    stage.setScene(scene);
                    stage.show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void completeCreateNewGame(int numPlayer, boolean expert) {
        this.numPlayer = numPlayer;
        this.expert = expert;
        chooseWizard(true);
    }

    public void chooseWizard(boolean newGame) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ChooseWizard.fxml"));
            Parent root = fxmlLoader.load();
            ChooseWizardController chooseWizardController = fxmlLoader.getController();
            chooseWizardController.setGUI(this);
            if (newGame) {
                chooseWizardController.setNewgame(true);
                chooseWizardController.setMessageForNewGame(numPlayer, expert);
            } else {
                chooseWizardController.setNewgame(false);
                if (!wizards.contains(Wizard.WIZARD1)) {
                    chooseWizardController.disableRadio(1);
                }
                if (!wizards.contains(Wizard.WIZARD2)) {
                    chooseWizardController.disableRadio(2);
                }
                if (!wizards.contains(Wizard.WIZARD3)) {
                    chooseWizardController.disableRadio(3);
                }
                if (!wizards.contains(Wizard.WIZARD4)) {
                    chooseWizardController.disableRadio(4);
                }
            }
            Scene scene = new Scene(root, 600, 402);
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    stage.setScene(scene);
                    stage.show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showGameCreated(String msg) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GameCreated.fxml"));
            Parent root = fxmlLoader.load();
            GameCreatedController gameCreatedController = fxmlLoader.getController();
            gameCreatedController.setGUI(this);
            gameCreatedController.setMessage(msg);
            Scene scene = new Scene(root, 600, 402);
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    stage.setScene(scene);
                    stage.show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playAssistant(String msg) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ChooseAssistant.fxml"));
            Parent root = fxmlLoader.load();
            ChooseAssistantController chooseAssistantController = fxmlLoader.getController();
            chooseAssistantController.setGUI(this);
            for (int i = 0; i < 10; i++) {
                if (getGame().getPlayers().get(getGame().getPlayerIndexFromNickname(getNickname())).getAssistants().get(i) == null) {
                    chooseAssistantController.disableRadio(i + 1);
                }
            }
            /*
            for (Player player : game.getPlayers()) {
                if (!player.getNickName().equals(nickname) && player.getUsedAssistant() != null) {
                    chooseAssistantController.disableRadio(player.getUsedAssistant().getValue());
                }
            }
            */
            if (!Objects.equals(msg, "")) {
                chooseAssistantController.setMessage(msg);
            }
            Scene scene = new Scene(root, 1920, 1080);
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    stage.setScene(scene);
                    stage.show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void moveStudentToIsland(String msg, int studentIndex) {//this differs from checkBoard because of int studentIndex
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GameBoard.fxml"));
            Parent root = fxmlLoader.load();
            GameBoardController gameBoardController = fxmlLoader.getController();
            gameBoardController.setGUI(this);
            gameBoardController.setMoveStudent(true);
            gameBoardController.drawIslands(getGame().getIslands().size());
            gameBoardController.drawClouds(game.getPlayers().size());
            gameBoardController.drawBag();
            if (!Objects.equals(msg, "")) {
                gameBoardController.setMessage(msg);
            }
            gameBoardController.setStudentIndex(studentIndex);
            Scene scene = new Scene(root, 1920, 1080);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/GameBoard.css")).toExternalForm());
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    stage.setScene(scene);
                    stage.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gameOver(String msg) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GameOver.fxml"));
            Parent root = fxmlLoader.load();
            GameOverController gameOverController = fxmlLoader.getController();
            gameOverController.setGUI(this);
            if (!Objects.equals(msg, "")) {
                gameOverController.setMessage(msg);
            }
            Scene scene = new Scene(root, 1920, 1080);
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    stage.setScene(scene);
                    stage.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkBoard(String msg) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GameBoard.fxml"));
            Parent root = fxmlLoader.load();
            GameBoardController gameBoardController = fxmlLoader.getController();
            gameBoardController.setGUI(this);
            if (!currPhase.equals(Phase_GUI.Planning) || game.getPlayers().get(game.getPlayerIndexFromNickname(nickname)).getUsedAssistant() != null) {
                gameBoardController.disableBack(true);
            }
            if (myTurn && currPhase.equals(Phase_GUI.Action1)) {
                gameBoardController.setMoveStudent(true);
            } else if (myTurn && currPhase.equals(Phase_GUI.Action2)) {
                gameBoardController.setMoveMotherNature(true);
                gameBoardController.disableBack(true);
            } else if (myTurn && currPhase.equals(Phase_GUI.Action3)) {
                gameBoardController.setChooseCloud(true);
                gameBoardController.disableBack(true);
            } else if (myTurn && currPhase.equals(Phase_GUI.Character3)) {
                gameBoardController.setCharacter(true);
                gameBoardController.disableBack(true);
            } else if (myTurn && currPhase.equals(Phase_GUI.Character5)) {
                gameBoardController.setCharacter(true);
                gameBoardController.disableBack(true);
            } else if (myTurn && currPhase.equals(Phase_GUI.Character9)) {
                gameBoardController.disableBack(false);
            } else if (myTurn && currPhase.equals(Phase_GUI.Character12)) {
                gameBoardController.disableBack(false);
            }
            gameBoardController.drawIslands(game.getIslands().size());
            gameBoardController.drawClouds(game.getPlayers().size());
            gameBoardController.drawBag();
            if (!Objects.equals(msg, "")) {
                gameBoardController.setMessage(msg);
            }
            Scene scene = new Scene(root, 1920, 1080);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/GameBoard.css")).toExternalForm());
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    stage.setScene(scene);
                    stage.show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewSchoolBoard(String msg, boolean other) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SchoolBoard.fxml"));
            Parent root = fxmlLoader.load();
            SchoolBoardController schoolBoardController = fxmlLoader.getController();
            schoolBoardController.setGUI(this);
            if (!Objects.equals(msg, "")) {
                schoolBoardController.setMessage(msg);
            }
            ArrayList<Player> players = new ArrayList<>();
            players.add(game.getPlayers().get(game.getPlayerIndexFromNickname(nickname)));
            for (int i = 0; i < game.getPlayers().size(); i++) {
                if (!players.contains(game.getPlayers().get(i))) {
                    players.add(game.getPlayers().get(i));
                }
            }
            if (!other) {
                for (int i = 0; i < 2; i++) {
                    schoolBoardController.drawSchoolBoard(players.get(i));
                }
                if (expert && ((currPhase.equals(Phase_GUI.Action1)) || currPhase.equals(Phase_GUI.Action2) || currPhase.equals(Phase_GUI.Action3) || !myTurn)) {
                    schoolBoardController.enableCharacterButton();
                }
                if (myTurn && currPhase.equals(Phase_GUI.Action1)) {
                    schoolBoardController.enableMoveToIslandPane(true);
                    schoolBoardController.setBackMessage("View game board");
                }
                if (currPhase.equals(Phase_GUI.Character1)|| currCharacter == 1) {
                    schoolBoardController.enableCharacter1();
                } else if (currPhase.equals(Phase_GUI.Character2) || currCharacter == 2) {
                    schoolBoardController.enableCharacter2();
                } else if (currPhase.equals(Phase_GUI.Character3)|| currCharacter == 3) {
                    schoolBoardController.enableCharacter3();
                } else if (currPhase.equals(Phase_GUI.Character4) || currCharacter == 4) {
                    schoolBoardController.enableCharacter4();
                } else if (currPhase.equals(Phase_GUI.Character5) || currCharacter == 5) {
                    schoolBoardController.enableCharacter5();
                } else if (currPhase.equals(Phase_GUI.Character6) || currCharacter == 6) {
                    schoolBoardController.enableCharacter6();
                } else if (currPhase.equals(Phase_GUI.Character7) || currCharacter == 7) {
                    schoolBoardController.enableCharacter7();
                } else if (currPhase.equals(Phase_GUI.Character8) || currCharacter == 8) {
                    schoolBoardController.enableCharacter8();
                } else if (currPhase.equals(Phase_GUI.Character9) || currCharacter == 9) {
                    schoolBoardController.enableCharacter9();
                } else if (currPhase.equals(Phase_GUI.Character10) || currCharacter == 10) {
                    schoolBoardController.enableCharacter10();
                } else if (currPhase.equals(Phase_GUI.Character11) || currCharacter == 11) {
                    schoolBoardController.enableCharacter11();
                } else if (currPhase.equals(Phase_GUI.Character12) || currCharacter == 12) {
                    schoolBoardController.enableCharacter12();
                }

            } else {
                schoolBoardController.setUpOtherBoards();
                for (int i = 2; i < players.size(); i++) {
                    schoolBoardController.drawSchoolBoard(players.get(i));
                }
            }
            Scene scene = new Scene(root, 1920, 1080);
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    stage.setScene(scene);
                    stage.show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCharacter(String msg) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/UseCharacterCard.fxml"));
            Parent root = fxmlLoader.load();
            ChooseCharacterController chooseCharacterController = fxmlLoader.getController();
            chooseCharacterController.setGUI(this);
            chooseCharacterController.setAvailableCharacters();
            chooseCharacterController.showDetails();
            if (!myTurn) {
                chooseCharacterController.disableChoose(true);
                chooseCharacterController.setMessage("You are looking at character cards.");
            }
            if (!Objects.equals(msg, "")) {
                chooseCharacterController.setMessage(msg);
            }
            Scene scene = new Scene(root, 1920, 1080);
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    stage.setScene(scene);
                    stage.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pickColor() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/UseCharacterCard.fxml"));
            Parent root = fxmlLoader.load();
            PickColorController pickColorController = fxmlLoader.getController();
            pickColorController.setGUI(this);
            Scene scene = new Scene(root, 1920, 1080);
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    stage.setScene(scene);
                    stage.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(Object message) {
        if (message == null) {
            throw new IllegalArgumentException("Null cannot be sent as a message.\n");
        } else {
            serverHandler.send(message);
        }
    }

    public void startServerHandler() {
        Thread serverHandlerThread  = new Thread(this.serverHandler);
        serverHandlerThread.start();
    }

    public void messageReceived(Object message) {
        if (message instanceof NickResponseMessage) {
            if (currPhase.equals(Phase_GUI.PickingNickname)) {
                ((NickResponseMessage) message).GUIprocess(this.serverHandler);
            }
        } else if (message instanceof SendMatchesMessage) {
            if (currPhase.equals(Phase_GUI.ChoosingGameMode) || currPhase.equals(Phase_GUI.JoiningGame1)) {
                ((SendMatchesMessage) message).processGUI(this.serverHandler);
            }
        } else if (message instanceof ConfirmJoiningMessage) {
            if (currPhase.equals(Phase_GUI.CreatingGame) ||
                    currPhase.equals(Phase_GUI.JoiningGame1) ||
                    currPhase.equals(Phase_GUI.JoiningGame2)) {
                ((ConfirmJoiningMessage) message).processGUI(this.serverHandler);
            }
        } else if (message instanceof SendAvailableWizardsMessage) {
            if (currPhase.equals(Phase_GUI.JoiningGame1) ||
                    currPhase.equals(Phase_GUI.JoiningGame2)) {
                ((SendAvailableWizardsMessage) message).processGUI(this.serverHandler);
            }
        } else if (message instanceof GameModelUpdateMessage) {
            ((GameModelUpdateMessage) message).processGUI(this.serverHandler);
        }  else if (message instanceof ChangeTurnMessage) {
            ((ChangeTurnMessage) message).processGUI(this.serverHandler);
        } else if (message instanceof CloudsUpdateMessage) {
            ((CloudsUpdateMessage) message).processGUI(this.serverHandler);
        } else if (message instanceof UsedAssistantMessage) {
            ((UsedAssistantMessage) message).processGUI(this.serverHandler);
        } else if (message instanceof ConfirmMovementFromEntranceMessage) {
            ((ConfirmMovementFromEntranceMessage) message).processGUI(this.serverHandler);
            if (currPhase.equals(Phase_GUI.Action1)) {
                ap1Moves++;
                if (ap1Moves == ((getGame().getPlayers().size() == 3)? 4 : 3)) {
                    currPhase = Phase_GUI.Action2;
                    ap1Moves = 0;
                    checkBoard("Move the mother nature");
                } else {
                    viewSchoolBoard("move a student.", false);
                }
            } else {
                viewSchoolBoard("", false);
            }
        } else if (message instanceof DenyMovementMessage) {
            ((DenyMovementMessage) message).processGUI(this.serverHandler);
        } else if (message instanceof ConfirmMovementMessage) {
            ((ConfirmMovementMessage) message).processGUI(this.serverHandler);
        } else if (message instanceof ConfirmCloudMessage) {
            try {
                ((ConfirmCloudMessage) message).processGUI(this.serverHandler);
            } catch (EmptyCloudException e){
                checkBoard("The cloud is empty");
            }
        } else if (message instanceof EndMessage) {
            ((EndMessage) message).processGUI(this.serverHandler);
        } else if (message instanceof MoveProfessorMessage) {
            ((MoveProfessorMessage) message).processGUI(this.serverHandler);
        } else if (message instanceof CharacterUsedMessage) {
            try {
                ((CharacterUsedMessage) message).processGUI(this.serverHandler);
            } catch (WrongEffectException e3) {
                viewSchoolBoard("Wrong effect", false);
            } catch (NotEnoughNoEntriesException e4) {
                viewSchoolBoard("No enough no entries", false);
            }
        } else if (message instanceof CharacterEntranceSwappedMessage){
            try {
                ((CharacterEntranceSwappedMessage) message).processGUI(this.serverHandler);
            } catch (InterruptedException ignored){

            } catch (EmptyCloudException e1) {
                checkBoard("The cloud is empty");
            } catch (FullTableException e2) {
                viewSchoolBoard("The table is full.", false);
            }

        } else if (message instanceof EntranceTableSwappedMessage){
            try {
                ((EntranceTableSwappedMessage) message).processGUI(this.serverHandler);
            } catch (InterruptedException ignored){

            } catch (EmptyCloudException e1) {
                checkBoard("The cloud is empty");
            } catch (FullTableException e2) {
                viewSchoolBoard("The table is full.", false);
            }

        } else if (message instanceof IslandChosenMessage){
            try {
                ((IslandChosenMessage) message).processGUI(this.serverHandler);
            } catch (InterruptedException ignored){

            } catch (EmptyCloudException e1) {
                checkBoard("The cloud is empty");
            } catch (FullTableException e2) {
                viewSchoolBoard("The table is full.", false);
            }

        } else if (message instanceof NoEntryMovedMessage){
            try {
                ((NoEntryMovedMessage) message).processGUI(this.serverHandler);
            } catch (InterruptedException ignored){

            } catch (EmptyCloudException e1) {
                checkBoard("The cloud is empty");
            } catch (FullTableException e2) {
                viewSchoolBoard("The table is full.", false);
            }
        } else if (message instanceof StudentColorChosenMessage){
            try {
                ((StudentColorChosenMessage) message).processGUI(this.serverHandler);
            } catch (InterruptedException ignored){

            } catch (EmptyCloudException e1) {
                checkBoard("The cloud is empty");
            } catch (FullTableException e2) {
                viewSchoolBoard("The table is full.", false);
            }
        } else if (message instanceof StudentMovedFromCharacterMessage){
            try {
                ((StudentMovedFromCharacterMessage) message).processGUI(this.serverHandler);
            } catch (InterruptedException ignored){

            } catch (EmptyCloudException e1) {
                checkBoard("The cloud is empty");
            } catch (FullTableException e2) {
                viewSchoolBoard("The table is full.", false);
            }
        } else if (message instanceof StudentMovedToTableMessage){
            try {
                ((StudentMovedToTableMessage) message).processGUI(this.serverHandler);
            } catch (InterruptedException ignored){

            } catch (EmptyCloudException e1) {
                checkBoard("The cloud is empty");
            } catch (FullTableException e2) {
                viewSchoolBoard("The table is full.", false);
            }
        }
    }

    public Phase_GUI getCurrPhase() {
        return this.currPhase;
    }

    public Stage getStage() {
        return this.stage;
    }

    public ServerHandler getServerHandler() {
        return serverHandler;
    }

    public void setServerHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    public void setCurrPhase(Phase_GUI currPhase) {
        this.currPhase = currPhase;
    }

    public Phase_GUI getPrevPhase() {
        return this.prevPhase;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getNumPlayer() {
        return numPlayer;
    }

    public boolean isExpert() {
        return expert;
    }

    public void setExpert(boolean expert) {
        this.expert = expert;
    }

    public void setWizards(List<Wizard> wizards) {
        this.wizards = wizards;
    }

    public GameModel getGame() {
        return game;
    }

    public void setGame(GameModel game) {
        this.game = game;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setPrevPhase(Phase_GUI phase) {
        this.prevPhase = phase;
    }

    public int getCurrCharacter() {
        return currCharacter;
    }

    public void setCurrCharacter(int currCharacter) {
        this.currCharacter = currCharacter;
    }

    public int getChangeTurnNums() {
        return this.changeTurnNums;
    }
    public void setChangeTurnNums(int changeTurnNums){
        this.changeTurnNums = changeTurnNums;
    }

    public ArrayList<String> getPlayerPlayedAssistant() {
        return playerPlayedAssistant;
    }

    public void setPlayerPlayedAssistant(ArrayList<String> playerPlayedAssistant) {
        this.playerPlayedAssistant = playerPlayedAssistant;
    }


}