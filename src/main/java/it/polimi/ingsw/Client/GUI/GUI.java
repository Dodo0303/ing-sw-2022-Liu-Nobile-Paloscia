package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.GUI.Controllers.ChooseAssistantController;
import it.polimi.ingsw.Client.GUI.Controllers.Joining.*;
import it.polimi.ingsw.Client.GUI.Controllers.GameBoardController;
import it.polimi.ingsw.Client.GUI.Controllers.SchoolBoardController;
import it.polimi.ingsw.Client.GUI.Controllers.Uncategorized.ChooseWizardController;
import it.polimi.ingsw.Exceptions.EmptyCloudException;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Network.Messages.toClient.ActionPhase.*;
import it.polimi.ingsw.Network.Messages.toClient.CharacterPhase.CharacterUsedMessage;
import it.polimi.ingsw.Network.Messages.toClient.EndMessage;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.*;
import it.polimi.ingsw.Network.Messages.toClient.PlanningPhase.CloudsUpdateMessage;
import it.polimi.ingsw.Network.Messages.toClient.PlanningPhase.UsedAssistantMessage;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.MoveStudentFromEntranceMessage;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
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
    private Assistant assistant;
    private int ap1Moves;

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
            serverHandler = new ServerHandler(host,12345, this); //TODO Don't ask the user for the port
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
            /** todo need to clear "current assistants" (which are no longer being current) when a new round started
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

    public void moveStudentsFromEntrance(String msg) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SchoolBoard.fxml"));
            Parent root = fxmlLoader.load();
            SchoolBoardController schoolBoardController = fxmlLoader.getController();
            schoolBoardController.setGUI(this);
            schoolBoardController.enableMoveToIslandPane(true);
            schoolBoardController.setBackMessage("View game board");
            if (!Objects.equals(msg, "")) {
                schoolBoardController.setMessage(msg);
            }
            schoolBoardController.drawSchoolBoard(game.getPlayerByNickname(nickname));
            schoolBoardController.drawSchoolBoard(game.getPlayers().get(0));
            schoolBoardController.drawSchoolBoard(game.getPlayers().get(1));
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

    public void moveStudentToIsland(String msg, int studentIndex) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GameBoard.fxml"));
            Parent root = fxmlLoader.load();
            GameBoardController gameBoardController = fxmlLoader.getController();
            gameBoardController.setGUI(this);
            gameBoardController.setMoveStudent(true);
            gameBoardController.drawIslands(getGame().getIslands().size());
            gameBoardController.drawClouds(game.getPlayers().size());
            if (!Objects.equals(msg, "")) {
                gameBoardController.setMessage(msg);
            }
            gameBoardController.setStudentIndex(studentIndex);
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

    public void moveMotherNature(String msg) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GameBoard.fxml"));
            Parent root = fxmlLoader.load();
            GameBoardController gameBoardController = fxmlLoader.getController();
            gameBoardController.setGUI(this);
            gameBoardController.setMoveMotherNature(true);
            gameBoardController.disableBack();
            gameBoardController.drawIslands(getGame().getIslands().size());
            gameBoardController.drawClouds(game.getPlayers().size());
            if (!Objects.equals(msg, "")) {
                gameBoardController.setMessage(msg);
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

    public void chooseCloud(String msg) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GameBoard.fxml"));
            Parent root = fxmlLoader.load();
            GameBoardController gameBoardController = fxmlLoader.getController();
            gameBoardController.setGUI(this);
            gameBoardController.setChooseCloud(true);
            gameBoardController.disableBack();
            gameBoardController.drawIslands(getGame().getIslands().size());
            gameBoardController.drawClouds(game.getPlayers().size());
            if (!Objects.equals(msg, "")) {
                gameBoardController.setMessage(msg);
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
            if (!currPhase.equals(Phase_GUI.Planning) && game.getPlayers().get(game.getPlayerIndexFromNickname(nickname)).getUsedAssistant() != null) {
                gameBoardController.disableBack();
            }
            gameBoardController.drawIslands(game.getIslands().size());
            gameBoardController.drawClouds(game.getPlayers().size());
            if (!Objects.equals(msg, "")) {
                gameBoardController.setMessage(msg);
            }
            if (!currPhase.equals(Phase_GUI.Planning) && game.getPlayers().get(game.getPlayerIndexFromNickname(nickname)).getUsedAssistant() != null) {
                gameBoardController.disableBack();
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

    public void viewSchoolBoard(String msg) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SchoolBoard.fxml"));
            Parent root = fxmlLoader.load();
            SchoolBoardController schoolBoardController = fxmlLoader.getController();
            schoolBoardController.setGUI(this);
            schoolBoardController.setBackButton(true);//todo
            if (!Objects.equals(msg, "")) {
                schoolBoardController.setMessage(msg);
            }
            schoolBoardController.drawSchoolBoard(game.getPlayerByNickname(nickname));
            schoolBoardController.drawSchoolBoard(game.getPlayers().get(0));
            schoolBoardController.drawSchoolBoard(game.getPlayers().get(1));
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

    public void messageReceived(Object message) throws EmptyCloudException {
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
            System.out.println(currPhase.toString() + " to " + ((ChangeTurnMessage) message).getPhase().toString());//TODO DELETE AFTER TESTS
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
                    moveMotherNature("Move the mother nature");
                } else {
                    moveStudentsFromEntrance("move a student.");
                }
            } else {
                viewSchoolBoard("");
            }
        } else if (message instanceof DenyMovementMessage) {
            ((DenyMovementMessage) message).processGUI(this.serverHandler);
        } else if (message instanceof ConfirmMovementMessage) {
            ((ConfirmMovementMessage) message).processGUI(this.serverHandler);
        } else if (message instanceof ConfirmCloudMessage) {
            ((ConfirmCloudMessage) message).processGUI(this.serverHandler);
        } else if (message instanceof EndMessage) {
            ((EndMessage) message).processGUI(this.serverHandler);
        } else if (message instanceof CharacterUsedMessage) {
            ((CharacterUsedMessage) message).processGUI(this.serverHandler);
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

    public void setWizards(List<Wizard> wizards) {
        this.wizards = wizards;
    }

    public GameModel getGame() {
        return game;
    }

    public void setGame(GameModel game) {
        this.game = game;
    }


    public void setAssistant(Assistant assistant) {
        this.assistant = assistant;
    }
}
