package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.GUI.Controllers.*;
import it.polimi.ingsw.Client.GUI.Controllers.Joining.*;
import it.polimi.ingsw.Client.Phase;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Client.ViewController;
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
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import javafx.stage.WindowEvent;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GUI implements ViewController {
    private GameModel game;
    private final Stage stage;
    private ServerHandler serverHandler;
    private Phase currPhase;
    private Phase prevPhase;
    private Phase currentOtherPlayerPhase;
    private String nickname;
    private String host;
    private int port;
    private int numPlayer;
    private boolean expert;
    private List<Wizard> wizards;
    private int ap1Moves;
    private boolean myTurn;
    private int currCharacter;
    private String[] nicknames;
    private HashMap<Integer, String> assistantPlayer;
    private double scalingRatio;
    private boolean fullScreen;
    private boolean assistantPicked;
    private String currPlayer;

    public GUI(Stage stage) {
        this.stage = stage;
    }

    /** Start GUI with login.fxml
    */
    public void start() {
        try {
            assistantPlayer = new HashMap<>();
            fullScreen = false;
            assistantPicked = false;
            ap1Moves = 0;
            getScaleFactor();
            stage.getIcons().add(new Image("icon.png"));
            stage.setResizable(false);
            stage.setTitle("Eriantys");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/login.fxml"));
            Parent root = fxmlLoader.load();
            LoginController loginController = fxmlLoader.getController();
            loginController.setGUI(this);
            Scene scene = new Scene(root, 600, 402);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("CSS/login.css")).toExternalForm());
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Platform.exit();
                    System.exit(0);
                }
            });
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /** establish connection with the server
     */
    public boolean settingUpConnection(String host, int port) {
        try {
            serverHandler = new ServerHandler(host, port, this);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
    /** render view of nickname.fxml
     */
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
    /** render view of chooseGameMode.fxml
     */
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
    /** render view of newgame.fxml
     */
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
    /** render view of JoinGame.fxml
     */
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
    /* set this.numPlayer and this.expert, then render view of ChooseWizard.fxml
     */
    public void completeCreateNewGame(int numPlayer, boolean expert) {
        this.numPlayer = numPlayer;
        this.expert = expert;
        chooseWizard(true);
    }
    /** render view of ChooseWizard.fxml, disabling ratio buttons of unavailable wizards
     *
     */
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
            if (numPlayer == 4) {
                chooseWizardController.setFourPlayerGame(true);
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
    /** render view of GameCreated.fxml
     *
     */
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
    /** render view of ChooseAssistant.fxml, disabling ratio buttons of unavailable assistant cards
     *
     */
    public void playAssistant(String msg) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ChooseAssistant.fxml"));
            Parent root = fxmlLoader.load();
            ChooseAssistantController chooseAssistantController = fxmlLoader.getController();
            chooseAssistantController.setGUI(this);
            //disable my used assistants
            for (int i = 0; i < 10; i++) {
                if (getGame().getPlayers().get(getGame().getPlayerIndexFromNickname(getNickname())).getAssistants().get(i) == null) {
                    chooseAssistantController.disableRadio(i + 1);
                }
            }
            //disable assistants used by other players in the current round
            for (int i : assistantPlayer.keySet()) {
                chooseAssistantController.disableRadio(i);
            }
            if (!Objects.equals(msg, "")) {
                chooseAssistantController.setMessage(msg);
            }
            enableScalingAndShowScene(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** render view of GameBoard.fxml.
     * This method is called after players dragged a student from the entrance of the school board to islands.
     * This method differs from checkBoard because of the other parameter studentIndex, which indicates the index of the student in the entrance.
     */
    public void moveStudentToIsland(String msg, int studentIndex) {
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
            Scene scene = new Scene(root, 1920*scalingRatio, 1080*scalingRatio);
            Scale scale = new Scale(1*scalingRatio, 1*scalingRatio, 0, 0);
            root.getTransforms().add(scale);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/GameBoard.css")).toExternalForm());
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
    /** render view of GameOver.fxml.
     */
    public void gameOver(String msg) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GameOver.fxml"));
            Parent root = fxmlLoader.load();
            GameOverController gameOverController = fxmlLoader.getController();
            gameOverController.setGUI(this);
            if (!Objects.equals(msg, "")) {
                gameOverController.setMessage(msg);
            }
            enableScalingAndShowScene(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /** render view of GameBoard.fxml.
     * One of the two main views of the game.
     * It enables/disables components based on the current phase of the game.
     */
    public void checkBoard(String msg) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GameBoard.fxml"));
            Parent root = fxmlLoader.load();
            GameBoardController gameBoardController = fxmlLoader.getController();
            gameBoardController.setGUI(this);
            gameBoardController.setPhaseLabel(currPlayer + ": " + (currentOtherPlayerPhase.equals(Phase.Planning)?"Planning Phase": "Action Phase"));
            if (!currPhase.equals(Phase.Planning) || assistantPicked) {
                gameBoardController.disableBack(true);
            }
            if (myTurn) {
                if (currPhase.equals(Phase.Action1)) {
                    gameBoardController.setMoveStudent(true);
                }
                if (currPhase.equals(Phase.Action2)) {
                    gameBoardController.setMoveMotherNature(true);
                    gameBoardController.disableBack(true);
                }
                if (currPhase.equals(Phase.Action3)) {
                    gameBoardController.setChooseCloud(true);
                    gameBoardController.disableBack(true);
                }
                if (currPhase.equals(Phase.Character3)) {
                    gameBoardController.setCharacter(true);
                    gameBoardController.disableBack(true);
                }
                if (currPhase.equals(Phase.Character5)) {
                    gameBoardController.setCharacter(true);
                    gameBoardController.disableBack(true);
                }
                if (currPhase.equals(Phase.Character9)) {
                    gameBoardController.disableBack(false);
                }
                if (currPhase.equals(Phase.Character12)) {
                    gameBoardController.disableBack(false);
                }
            }
            gameBoardController.drawIslands(game.getIslands().size());
            gameBoardController.drawClouds(game.getPlayers().size());
            gameBoardController.drawBag();
            if (!Objects.equals(msg, "")) {
                gameBoardController.setMessage(msg);
            }
            Scene scene;
            scene = new Scene(root, 1920*scalingRatio, 1080*scalingRatio);
            Scale scale = new Scale(1*scalingRatio, 1*scalingRatio, 0, 0);
            root.getTransforms().add(scale);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/GameBoard.css")).toExternalForm());
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    stage.setScene(scene);
                    stage.setX(0);
                    stage.setY(0);
                    stage.show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** render view of SchoolBoard.fxml.
     * One of the two main views of the game.
     * It enables/disables components based on the current phase of the game.
     * If other == true, then display the other players' school boards(only if number of players > 2).
     */
    public void viewSchoolBoard(String msg, boolean other) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SchoolBoard.fxml"));
            Parent root = fxmlLoader.load();
            SchoolBoardController schoolBoardController = fxmlLoader.getController();
            schoolBoardController.setGUI(this);
            schoolBoardController.setPhaseLabel(currPlayer + ": " + (currentOtherPlayerPhase.equals(Phase.Planning)?"Planning Phase": "Action Phase"));
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
                if (expert && ((currPhase.equals(Phase.Action1)) || currPhase.equals(Phase.Action2) || currPhase.equals(Phase.Action3) || !myTurn)) {
                    schoolBoardController.enableCharacterButton();
                }
                if (myTurn && currPhase.equals(Phase.Action1)) {
                    schoolBoardController.enableMoveToIslandPane(true);
                    schoolBoardController.setBackMessage("View game board");
                }
                if (currPhase.equals(Phase.Character1)|| currCharacter == 1) {
                    schoolBoardController.enableCharacter1();
                } else if (currPhase.equals(Phase.Character2) || currCharacter == 2) {
                    schoolBoardController.enableCharacter2();
                } else if (currPhase.equals(Phase.Character3)) {
                    schoolBoardController.enableCharacter3();
                } else if (currPhase.equals(Phase.Character4) || currCharacter == 4) {
                    schoolBoardController.enableCharacter4();
                } else if (currPhase.equals(Phase.Character5)) {
                    schoolBoardController.enableCharacter5();
                } else if (currPhase.equals(Phase.Character6) || currCharacter == 6) {
                    schoolBoardController.enableCharacter6();
                } else if (currPhase.equals(Phase.Character7)) {
                    schoolBoardController.enableCharacter7();
                } else if (currPhase.equals(Phase.Character8) || currCharacter == 8) {
                    schoolBoardController.enableCharacter8();
                } else if (currPhase.equals(Phase.Character9)) {
                    schoolBoardController.enableCharacter9();
                } else if (currPhase.equals(Phase.Character10)) {
                    schoolBoardController.enableCharacter10();
                } else if (currPhase.equals(Phase.Character11)) {
                    schoolBoardController.enableCharacter11();
                } else if (currPhase.equals(Phase.Character12)) {
                    schoolBoardController.enableCharacter12();
                }
            } else {
                schoolBoardController.setUpOtherBoards();
                for (int i = 2; i < players.size(); i++) {
                    schoolBoardController.drawSchoolBoard(players.get(i));
                }
            }
            enableScalingAndShowScene(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** render view of UseCharacterCard.fxml.
     */
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
            enableScalingAndShowScene(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /** render view of PickColor.fxml.
     */
    public void pickColor() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PickColor.fxml"));
            Parent root = fxmlLoader.load();
            PickColorController pickColorController = fxmlLoader.getController();
            pickColorController.setGUI(this);
            enableScalingAndShowScene(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /** Send message to the server.
     */
    public void send(Object message) {
        if (message == null) {
            throw new IllegalArgumentException("Null cannot be sent as a message.\n");
        } else {
            serverHandler.send(message);
        }
    }
    /** Start the thread of my serverHandler.
     */
    public void startServerHandler() {
        Thread serverHandlerThread  = new Thread(this.serverHandler);
        serverHandlerThread.start();
    }
    /** Process received messages.
     */
    public void messageReceived(Object message) {
        try {
            if (message instanceof NickResponseMessage) {
                if (currPhase.equals(Phase.PickingNickname)) {
                    ((NickResponseMessage) message).GUIprocess(this.serverHandler);
                }
            } else if (message instanceof SendMatchesMessage) {
                if (currPhase.equals(Phase.ChoosingGameMode) || currPhase.equals(Phase.JoiningGame1)) {
                    ((SendMatchesMessage) message).processGUI(this.serverHandler);
                }
            } else if (message instanceof ConfirmJoiningMessage) {
                if (currPhase.equals(Phase.CreatingGame) ||
                        currPhase.equals(Phase.JoiningGame1) ||
                        currPhase.equals(Phase.JoiningGame2)) {
                    ((ConfirmJoiningMessage) message).processGUI(this.serverHandler);
                }
            } else if (message instanceof SendAvailableWizardsMessage) {
                if (currPhase.equals(Phase.JoiningGame1) ||
                        currPhase.equals(Phase.JoiningGame2)) {
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
                if (currPhase.equals(Phase.Action1)) {
                    ap1Moves++;
                    if (ap1Moves == ((getGame().getPlayers().size() == 3)? 4 : 3)) {
                        currPhase = Phase.Action2;
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
                ((CharacterUsedMessage) message).processGUI(this.serverHandler);
            } else if (message instanceof CharacterEntranceSwappedMessage){
                ((CharacterEntranceSwappedMessage) message).processGUI(this.serverHandler);
            } else if (message instanceof EntranceTableSwappedMessage){
                ((EntranceTableSwappedMessage) message).processGUI(this.serverHandler);
            } else if (message instanceof IslandChosenMessage){
                ((IslandChosenMessage) message).processGUI(this.serverHandler);
            } else if (message instanceof NoEntryMovedMessage){
                ((NoEntryMovedMessage) message).processGUI(this.serverHandler);
            } else if (message instanceof StudentColorChosenMessage){
                ((StudentColorChosenMessage) message).processGUI(this.serverHandler);
            } else if (message instanceof StudentMovedFromCharacterMessage){
                ((StudentMovedFromCharacterMessage) message).processGUI(this.serverHandler);
            } else if (message instanceof StudentMovedToTableMessage){
                ((StudentMovedToTableMessage) message).processGUI(this.serverHandler);
            }
        } catch (Exception e) {

        }

    }

    /**
     * Calculate scale factor based on the scaled size of the screen (in the case of Retina screens, the screen is always scaled down by 2, so if the original resolution is 2560*1440, then the practical resolution is 1280*720),
     * if the scaled screen is bigger than 1920*1080, do nothing; otherwise, scale down the game window to fit the screen.
     */
    private void getScaleFactor() {
        Screen screen = Screen.getPrimary();

        double boundX = screen.getBounds().getWidth();
        double boundY = screen.getBounds().getHeight();
        if (boundX >= 1920 && boundY >= 1080) {
            scalingRatio = 1;
        } else {
            this.scalingRatio = Math.min(boundX / 1920, boundY / 1080);
        }
    }

    private void enableScalingAndShowScene(Parent root) {
        Scene scene = new Scene(root, 1920*scalingRatio, 1080*scalingRatio);
        Scale scale = new Scale(1*scalingRatio, 1*scalingRatio, 0, 0);
        root.getTransforms().add(scale);
        Platform.runLater(new Runnable() {
            @Override public void run() {
                stage.setScene(scene);
                stage.show();
            }
        });
    }

    /**
     *
     * @return the current phase of the game
     */
    public Phase getCurrPhase() {
        return this.currPhase;
    }

    /**
     *
     * @return the current stage
     */
    public Stage getStage() {
        return this.stage;
    }

    /**
     *
     * @return the instance of the Server Handler that is managing the connection with the server
     */
    public ServerHandler getServerHandler() {
        return serverHandler;
    }

    /**
     * Set the server handler
     * @param serverHandler ServerHandler to be set
     */
    public void setServerHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    /**
     * Set the current phase of the game
     * @param currPhase phase to be set as current
     */
    public void setCurrPhase(Phase currPhase) {
        this.currPhase = currPhase;
    }

    /**
     *
     * @return the previous phase of the game
     */
    public Phase getPrevPhase() {
        return this.prevPhase;
    }

    /**
     *
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Set the nickname of the player
     * @param nickname nickname to be set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     *
     * @return whether the match is in expert mode or not
     */
    public boolean isExpert() {
        return expert;
    }

    /**
     *
     * @param expert true if the match must be in expert mode. False if not
     */
    public void setExpert(boolean expert) {
        this.expert = expert;
    }

    /**
     * Set the wizards chosen by the players
     * @param wizards wizards to be set
     */
    public void setWizards(List<Wizard> wizards) {
        this.wizards = wizards;
    }

    /**
     *
     * @return the instance of the Game Model that keeps the status of the game
     */
    public GameModel getGame() {
        return game;
    }

    /**
     * Set the instance of Game Model that keeps the status of the game
     * @param game GameModel to be set
     */
    public void setGame(GameModel game) {
        this.game = game;
    }

    /**
     * Set the state of the turn
     * @param myTurn true if it's the player' turn. False if not
     */
    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    /**
     *
     * @return whether is the turn of the player or not
     */
    public boolean isMyTurn() {
        return myTurn;
    }

    /**
     * Set the previous phase of the game
     * @param phase phase to be set as previous
     */
    public void setPrevPhase(Phase phase) {
        this.prevPhase = phase;
    }

    /**
     * Set the character that is currently being used
     * @param currCharacter ID of the character to be set
     */
    public void setCurrCharacter(int currCharacter) {
        this.currCharacter = currCharacter;
    }

    /**
     * Set the number of players that will play this match
     * @param numPlayer amount of players to be set
     */
    public void setNumPlayer(int numPlayer) {
        this.numPlayer = numPlayer;
    }

    /**
     * Set the status of the fullscreen
     * @param fullScreen true to turn on the fullscreen. False to turn it off.
     */
    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

    /**
     *
     * @return the nicknames of the players playing the match
     */
    public String[] getNicknames() {
        return nicknames;
    }

    /**
     * Set the nicknames of the players playing the match
     * @param nicknames nicknames to be set
     */
    public void setNicknames(String[] nicknames) {
        this.nicknames = nicknames;
    }

    /**
     *
     * @return the assistant chosen by the players
     */
    public HashMap<Integer, String> getAssistantPlayer() {
        return assistantPlayer;
    }

    /**
     *
     * @return whether the player has chosen the assistant or not
     */
    public boolean isAssistantPicked() {
        return assistantPicked;
    }

    /**
     * Set whether the player has chosen an assistant or not
     * @param assistantPicked true if the players has chosen an assistant. False if not
     */
    public void setAssistantPicked(boolean assistantPicked) {
        this.assistantPicked = assistantPicked;
    }


    /**
     * Set the phase of the current player when the current player is not ME.
     * @param currentOtherPlayerPhase the phase of the current player when the current player is not ME.
     */
    public void setCurrentOtherPlayerPhase(Phase currentOtherPlayerPhase) {
        this.currentOtherPlayerPhase = currentOtherPlayerPhase;
    }

    /**
     * Set the player currently playing
     * @param currPlayer nickname of the player to be set
     */
    public void setCurrPlayer(String currPlayer) {
        this.currPlayer = currPlayer;
    }


}