package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.GUI.Controllers.Joining.*;
import it.polimi.ingsw.Client.GUI.Controllers.Uncategorized.ChooseWizardController;
import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.Wizard;
import it.polimi.ingsw.Network.Messages.toClient.ActionPhase.ChangeTurnMessage;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.*;
import it.polimi.ingsw.Network.Messages.toClient.PlanningPhase.CloudsUpdateMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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

    public GUI(Stage stage) {
        this.stage = stage;
    }

    public void start() {
        try {
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
            serverHandler = new ServerHandler(host,port, this);
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

    public void playAssistant() {
        //todo
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
            System.out.println(currPhase.toString() + " to " + ((ChangeTurnMessage) message).getPhase().toString());//TODO DELETE AFTER TESTS
            if (currPhase.equals(Phase_GUI.GameJoined) && ((ChangeTurnMessage) message).getPhase().equals(Phase_GUI.Planning)) {
                ((ChangeTurnMessage) message).processGUI(this.serverHandler);
            } else if (currPhase.equals(Phase_GUI.Planning) && ((ChangeTurnMessage) message).getPhase().equals(Phase_GUI.Action1)) {
                ((ChangeTurnMessage) message).processGUI(this.serverHandler);
            } else if (currPhase.equals(Phase_GUI.Action3) && ((ChangeTurnMessage) message).getPhase().equals(Phase_GUI.Planning)) {
                ((ChangeTurnMessage) message).processGUI(this.serverHandler);
            }
        } else if (message instanceof CloudsUpdateMessage) {
            ((CloudsUpdateMessage) message).processGUI(this.serverHandler);
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
}
