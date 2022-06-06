package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Network.Messages.toClient.CharacterPhase.StudentMovedToTableMessage;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.MoveStudentFromEntranceMessage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SchoolBoardController implements Initializable {
    @FXML
    private Button otherBoardButton, backButton;
    @FXML
    private Label MyNickname, OpponentNickname, messageLabel, backLabel, opponentCoin, myCoin;
    @FXML
    private ImageView MyCard, OpponentCard, opponentCoinImage;
    private GUI gui;
    private ArrayList<Point> greenStudents, redStudents, yellowStudents, pinkStudents, blueStudents, entrance, towers;
    private ArrayList<String> players;
    private Point greenProf, redProf, yellowProf, pinkProf, blueProf;
    @FXML
    private StackPane OpponentBoard, MyBoard, moveToIslandPane;
    @FXML
    private Rectangle tableArea;
    private ArrayList<ImageView> imageViews;
    private int studentIndex;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCoords();
        players = new ArrayList<>();
    }

    public void fullScreen() {
        gui.getStage().setFullScreen(true);
    }

    public void handleDropOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasImage()) {
            dragEvent.acceptTransferModes(TransferMode.COPY);
            dragEvent.consume();
        }
    }

    public void handleIslandDrop(DragEvent dragEvent) {
        dragEvent.setDropCompleted(true);
        gui.moveStudentToIsland("Choose an island.", studentIndex);
        studentIndex = -1;
    }

    public void handleTableDrop(DragEvent dragEvent) {
        dragEvent.setDropCompleted(true);
        MyBoard.setDisable(true);
        backButton.setDisable(true);
        otherBoardButton.setDisable(true);
        gui.send(new MoveStudentFromEntranceMessage(studentIndex, 0, -1));
        studentIndex = -1;
    }

    public void otherBoards() {
        gui.viewSchoolBoard("", true);
    }

    public void setUpOtherBoards() {
        if (gui.getGame().getPlayers().size() != 4) {
            OpponentBoard.setDisable(true);
            OpponentBoard.setVisible(false);
            OpponentNickname.setDisable(true);
            OpponentNickname.setVisible(false);
            opponentCoin.setDisable(true);
            opponentCoin.setVisible(false);
            OpponentCard.setVisible(false);
            opponentCoinImage.setVisible(false);
        }
        otherBoardButton.setDisable(true);
        otherBoardButton.setVisible(false);
    }

    public void drawSchoolBoard(Player player) {
        if (players.contains(player.getNickName())) {
            return;
        }
        players.add(player.getNickName());
        StackPane p = null;
        if (player.getNickName().equals(players.get(0)) ||
                (players.size() > 2 && player.getNickName().equals(players.get(2)))) {
            p = MyBoard;
            MyNickname.setText(player.getNickName());
            setMyCoinMessage(String.valueOf(player.getCoins())); //todo if expert
            if (player.getUsedAssistant() != null) {
                int index = player.getUsedAssistant().getValue();
                Image image = new Image("/assets/Assistenti/3x/Animali_1_" + index + "@3x.png");
                MyCard.setImage(image);
            }
        } else if (player.getNickName().equals(players.get(1)) ||
                (players.size() > 2 && player.getNickName().equals(players.get(3)))) {
            p = OpponentBoard;
            OpponentNickname.setText(player.getNickName());
            setOpponentCoinMessage(String.valueOf(player.getCoins())); //todo if expert
            if (player.getUsedAssistant() != null) {
                int index = player.getUsedAssistant().getValue();
                Image image = new Image("/assets/Assistenti/3x/Animali_1_" + index + "@3x.png");
                OpponentCard.setImage(image);
            }
        }
        //draw entrance
        for (int i = 0; i < gui.getGame().getEntranceOfPlayer(player).size(); i++) {
            StudentColor stud = gui.getGame().getEntranceOfPlayer(player).get(i);
            Image stuImage = null;
            imageViews.add(new ImageView());
            ImageView imageView1 = imageViews.get(imageViews.size() - 1);
            if (stud.equals(StudentColor.GREEN)) {
                stuImage = new Image("/assets/Students/green.png");
                imageView1.setImage(stuImage);
            } else if (stud.equals(StudentColor.RED)) {
                stuImage = new Image("/assets/Students/red.png");
                imageView1.setImage(stuImage);
            } else if (stud.equals(StudentColor.YELLOW)) {
                stuImage = new Image("/assets/Students/yellow.png");
                imageView1.setImage(stuImage);
            } else if (stud.equals(StudentColor.PINK)) {
                stuImage = new Image("/assets/Students/pink.png");
                imageView1.setImage(stuImage);
            } else if (stud.equals(StudentColor.BLUE)) {
                stuImage = new Image("/assets/Students/blue.png");
                imageView1.setImage(stuImage);
            }
            imageView1.setFitHeight(35);
            imageView1.setFitWidth(35);
            Point temp = entrance.get(i);
            imageView1.setTranslateX(temp.getX());
            imageView1.setTranslateY(temp.getY());
            Image finalStuImage = stuImage;
            if (players.size() == 1 && gui.getCurrPhase().equals(Phase_GUI.Action1)) {
                imageView1.setOnDragDetected(evt -> {
                    Dragboard dragboard = imageView1.startDragAndDrop(TransferMode.COPY);
                    ClipboardContent content = new ClipboardContent();
                    content.putImage(finalStuImage);
                    dragboard.setContent(content);
                    for (Point point : entrance) {
                        if (point.getX() == imageView1.getTranslateX() && point.getY() == imageView1.getTranslateY()) {
                            studentIndex = entrance.indexOf(point);
                        }
                    }
                });
            } else if (players.size() == 3 || !gui.getCurrPhase().equals(Phase_GUI.Action1)){
                tableArea.setDisable(true);
            }
            p.getChildren().add(imageView1);
        }
        //draw dining table
        for (StudentColor color : StudentColor.values()) {
            int stud = player.getDiningTables().get(color).getNumOfStudents();
            for (int i = 0; i < stud; i++) {
                imageViews.add(new ImageView());
                ImageView imageView1 = imageViews.get(imageViews.size() - 1);
                Point temp = null;
                if (color.equals(StudentColor.GREEN)) {
                    Image stu = new Image("/assets/Students/mercury.png");
                    imageView1.setImage(stu);
                    temp = greenStudents.get(i);
                } else if (color.equals(StudentColor.RED)) {
                    Image stu = new Image("/assets/Students/mercury.png");
                    imageView1.setImage(stu);
                    temp = redStudents.get(i);
                } else if (color.equals(StudentColor.YELLOW)) {
                    Image stu = new Image("/assets/Students/mercury.png");
                    imageView1.setImage(stu);
                    temp = yellowStudents.get(i);
                } else if (color.equals(StudentColor.PINK)) {
                    Image stu = new Image("/assets/Students/mercury.png");
                    imageView1.setImage(stu);
                    temp = pinkStudents.get(i);
                } else if (color.equals(StudentColor.BLUE)) {
                    Image stu = new Image("/assets/Students/mercury.png");
                    imageView1.setImage(stu);
                    temp = blueStudents.get(i);
                }
                imageView1.setFitHeight(25);
                imageView1.setFitWidth(25);
                imageView1.setTranslateX(temp.getX());
                imageView1.setTranslateY(temp.getY());
                p.getChildren().add(imageView1);
            }
        }
        //draw prof
        for (int i = 0; i < player.getProfessors().size(); i++) {
            StudentColor prof = player.getProfessors().get(i);
            imageViews.add(new ImageView());
            ImageView imageView1 = imageViews.get(imageViews.size() - 1);
            Point temp = null;
            if (prof.equals(StudentColor.GREEN)) {
                Image stu = new Image("/assets/Students/mercury.png");
                imageView1.setImage(stu);
                temp = greenProf;
            } else if (prof.equals(StudentColor.RED)) {
                Image stu = new Image("/assets/Students/mercury.png");
                imageView1.setImage(stu);
                temp = redProf;
            } else if (prof.equals(StudentColor.YELLOW)) {
                Image stu = new Image("/assets/Students/mercury.png");
                imageView1.setImage(stu);
                temp = yellowProf;
            } else if (prof.equals(StudentColor.PINK)) {
                Image stu = new Image("/assets/Students/mercury.png");
                imageView1.setImage(stu);
                temp = pinkProf;
            } else if (prof.equals(StudentColor.BLUE)) {
                Image stu = new Image("/assets/Students/mercury.png");
                imageView1.setImage(stu);
                temp = blueProf;
            }
            imageView1.setFitHeight(35);
            imageView1.setFitWidth(35);
            imageView1.setTranslateX(temp.getX());
            imageView1.setTranslateY(temp.getY());
            p.getChildren().add(imageView1);
        }

        for (int i = 0; i < player.getTowerNum(); i++) {//todo towernum does not update
            imageViews.add(new ImageView());
            ImageView imageView1 = imageViews.get(imageViews.size() - 1);
            Point temp = null;
            Color tower = player.getColor();
            if (tower.equals(Color.BLACK)) {
                Image stu = new Image("/assets/Towers/black.png");
                imageView1.setImage(stu);
            } else if (tower.equals(Color.WHITE)) {
                Image stu = new Image("/assets/Towers/white.png");
                imageView1.setImage(stu);
            } else if (tower.equals(Color.GRAY)) {
                Image stu = new Image("/assets/Towers/gray.png");
                imageView1.setImage(stu);
            }
            temp = towers.get(i);
            imageView1.setFitHeight(50);
            imageView1.setFitWidth(20);
            imageView1.setTranslateX(temp.getX());
            imageView1.setTranslateY(temp.getY());
            p.getChildren().add(imageView1);
        }

    }

    public void back() {
        gui.checkBoard("");
    }

    private void initCoords() {
        imageViews = new ArrayList<>();
        greenStudents = new ArrayList<>();
        redStudents = new ArrayList<>();
        yellowStudents = new ArrayList<>();
        pinkStudents = new ArrayList<>();
        blueStudents = new ArrayList<>();
        entrance = new ArrayList<>();
        towers = new ArrayList<>();
        greenStudents.add(new Point(-295, -140));
        greenStudents.add(new Point(-245, -140));
        greenStudents.add(new Point(-200, -140));
        greenStudents.add(new Point(-150, -140));
        greenStudents.add(new Point(-100, -140));
        greenStudents.add(new Point(-55, -140));
        greenStudents.add(new Point(-5, -140));
        greenStudents.add(new Point(40, -140));
        greenStudents.add(new Point(90, -140));
        greenStudents.add(new Point(140, -140));
        greenProf = new Point(232, -140);
        redStudents.add(new Point(-295, -70));
        redStudents.add(new Point(-245, -70));
        redStudents.add(new Point(-200, -70));
        redStudents.add(new Point(-150, -70));
        redStudents.add(new Point(-100, -70));
        redStudents.add(new Point(-55, -70));
        redStudents.add(new Point(-5, -70));
        redStudents.add(new Point(40, -70));
        redStudents.add(new Point(90, -70));
        redStudents.add(new Point(140, -70));
        redProf = new Point(232, -70);
        yellowStudents.add(new Point(-295, 0));
        yellowStudents.add(new Point(-245, 0));
        yellowStudents.add(new Point(-200, 0));
        yellowStudents.add(new Point(-150, 0));
        yellowStudents.add(new Point(-100, 0));
        yellowStudents.add(new Point(-55, 0));
        yellowStudents.add(new Point(-5, 0));
        yellowStudents.add(new Point(40, 0));
        yellowStudents.add(new Point(90, 0));
        yellowStudents.add(new Point(140, 0));
        yellowProf = new Point(232, 0);
        pinkStudents.add(new Point(-295, 70));
        pinkStudents.add(new Point(-245, 70));
        pinkStudents.add(new Point(-200, 70));
        pinkStudents.add(new Point(-150, 70));
        pinkStudents.add(new Point(-100, 70));
        pinkStudents.add(new Point(-55, 70));
        pinkStudents.add(new Point(-5, 70));
        pinkStudents.add(new Point(40, 70));
        pinkStudents.add(new Point(90, 70));
        pinkStudents.add(new Point(140, 70));
        pinkProf = new Point(232, 70);
        blueStudents.add(new Point(-295, 140));
        blueStudents.add(new Point(-245, 140));
        blueStudents.add(new Point(-200, 140));
        blueStudents.add(new Point(-150, 140));
        blueStudents.add(new Point(-100, 140));
        blueStudents.add(new Point(-55, 140));
        blueStudents.add(new Point(-5, 140));
        blueStudents.add(new Point(40, 140));
        blueStudents.add(new Point(90, 140));
        blueStudents.add(new Point(140, 140));
        blueProf = new Point(232, 140);
        entrance.add(new Point(-390, -140));
        entrance.add(new Point(-445, -70));
        entrance.add(new Point(-390, -70));
        entrance.add(new Point(-445, 0));
        entrance.add(new Point(-390, 0));
        entrance.add(new Point(-445, 70));
        entrance.add(new Point(-390, 70));
        entrance.add(new Point(-445, 140));
        entrance.add(new Point(-390, 140));
        towers.add(new Point(340, -110));
        towers.add(new Point(420, -110));
        towers.add(new Point(340, -35));
        towers.add(new Point(420, -35));
        towers.add(new Point(340, 40));
        towers.add(new Point(420, 40));
        towers.add(new Point(340, 110));
        towers.add(new Point(420, 110));
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
        if (gui.getGame().getPlayers().size() > 2) {
            otherBoardButton.setVisible(true);
            otherBoardButton.setDisable(false);
        }
    }

    public void setMessage(String msg) {
        messageLabel.setText(msg);
    }
    public void setBackMessage(String msg) {
        backLabel.setText(msg);
    }
    public void setMyCoinMessage(String msg) {
        myCoin.setText(msg);
    }
    public void setOpponentCoinMessage(String msg) {
        opponentCoin.setText(msg);
    }

    public void enableMoveToIslandPane(boolean enable) {
        if (enable) {
            moveToIslandPane.setDisable(false);
            moveToIslandPane.setVisible(true);
        }
    }
}
