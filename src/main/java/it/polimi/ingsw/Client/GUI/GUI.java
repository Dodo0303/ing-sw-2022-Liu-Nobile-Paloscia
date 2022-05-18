package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.GUI.Controllers.Joining.ChooseGameModeController;
import it.polimi.ingsw.Client.GUI.Controllers.Joining.LoginController;
import it.polimi.ingsw.Client.GUI.Controllers.Joining.NicknameController;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GUI {
    private final Stage stage;
    private ServerHandler serverHandler;
    private Phase_GUI currPhase;
    private String nickname;
    private String host;
    private int port;

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

    public void settingUpConnection(String host, int port) throws IOException {
        while(serverHandler == null) {
            serverHandler = new ServerHandler(host,port, this);
        }
        currPhase = Phase_GUI.PickingNickname;
    }

    public void requireNickname(String nickname) throws IOException {
        if (nickname != null) {
            this.nickname = nickname;
            setCurrPhase(Phase_GUI.ChoosingGameMode);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/chooseGameMode.fxml"));
            Parent root = fxmlLoader.load();
            ChooseGameModeController chooseGameModeController = fxmlLoader.getController();
            chooseGameModeController.setGUI(this);
            Scene scene = new Scene(root, 600, 402);
            stage.setScene(scene);
            stage.show();
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/nickname.fxml"));
            Parent root = fxmlLoader.load();
            NicknameController nicknameController = fxmlLoader.getController();
            nicknameController.setGUI(this);
            nicknameController.setMessage("Nickname has been taken.");
            Scene scene = new Scene(root, 600, 402);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void send(Object message) {
        if (message == null) {
            throw new IllegalArgumentException("Null cannot be sent as a message.\n");
        } else {
            serverHandler.send(message);
        }
    }

    public void messageReceived(Object message) {
        if (message instanceof NickResponseMessage) {
            System.out.println("pass");
            if (currPhase.equals(Phase_GUI.PickingNickname)) {
                try {
                    requireNickname(((NickResponseMessage) message).getNickname());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (message instanceof SendMatchesMessage) {
            if (currPhase.equals(Phase_GUI.ChoosingGameMode) || currPhase.equals(Phase_GUI.JoiningGame1)) {
                //((SendMatchesMessage) message).process(this.serverHandler);
            }
        } else if (message instanceof ConfirmJoiningMessage) {
            if (currPhase.equals(Phase_GUI.CreatingGame) ||
                    currPhase.equals(Phase_GUI.JoiningGame1) ||
                    currPhase.equals(Phase_GUI.JoiningGame2)) {
                //((ConfirmJoiningMessage) message).process(this.serverHandler);
            }
        } else if (message instanceof SendAvailableWizardsMessage) {
            if (currPhase.equals(Phase_GUI.JoiningGame1) ||
                    currPhase.equals(Phase_GUI.JoiningGame2)) {
                //((SendAvailableWizardsMessage) message).process(this.serverHandler);
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
}
