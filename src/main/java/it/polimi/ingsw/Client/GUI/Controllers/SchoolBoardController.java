package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
import it.polimi.ingsw.Exceptions.WrongEffectException;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.MoveStudentFromEntranceMessage;
import it.polimi.ingsw.Network.Messages.toServer.CharacterPhase.MoveStudentsToTableMessage;
import it.polimi.ingsw.Network.Messages.toServer.CharacterPhase.SwapStudentsCharacterEntranceMessage;
import it.polimi.ingsw.Network.Messages.toServer.CharacterPhase.SwapStudentsTableEntranceMessage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class SchoolBoardController implements Initializable {
    @FXML
    private Button otherBoardButton, backButton, useCharacterButton;
    @FXML
    private Label MyNickname, OpponentNickname, messageLabel, backLabel, opponentCoin, myCoin;
    @FXML
    private ImageView MyCard, OpponentCard, opponentCoinImage, myCoinImage, characterCardImage;
    private GUI gui;
    private ArrayList<Point> greenStudents, redStudents, yellowStudents, pinkStudents, blueStudents, entrance, towers;
    private ArrayList<String> players;
    private Point greenProf, redProf, yellowProf, pinkProf, blueProf;
    @FXML
    private StackPane OpponentBoard, MyBoard, moveToIslandPane, characterPane;
    @FXML
    private Rectangle tableArea, redArea, blueArea, pinkArea, yellowArea, greenArea;
    private ArrayList<ImageView> imageViews;
    private int studentIndex, swapStudentsTimes;
    private ArrayList<Integer> characterStudentIndex, entranceStudentsIndex;
    private ArrayList<StudentColor> colors;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCoords();
        players = new ArrayList<>();
    }

    public void fullScreen() {
        if (!gui.getStage().isFullScreen()) {
            gui.getStage().setFullScreen(true);
            gui.setFullScreen(true);
            gui.viewSchoolBoard(messageLabel.getText(), false);
        }
    }

    public void useCharacter() {
        gui.showCharacter("");
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
        if (gui.getCurrPhase().equals(Phase_GUI.Character11)) {
            gui.send(new MoveStudentsToTableMessage(studentIndex));
        } else {
            gui.send(new MoveStudentFromEntranceMessage(studentIndex, 0, -1));
        }
        studentIndex = -1;
    }

    public void handleRedDrop(DragEvent dragEvent) {
         if (gui.getCurrPhase().equals(Phase_GUI.Character10)) {
            colors.add(StudentColor.RED);
        }
    }

    public void handlePinkDrop(DragEvent dragEvent) {
        if (gui.getCurrPhase().equals(Phase_GUI.Character10)) {
            colors.add(StudentColor.PINK);
        }
    }

    public void handleYellowDrop(DragEvent dragEvent) {
        if (gui.getCurrPhase().equals(Phase_GUI.Character10)) {
            colors.add(StudentColor.YELLOW);
        }
    }

    public void handleGreenDrop(DragEvent dragEvent) {
        if (gui.getCurrPhase().equals(Phase_GUI.Character10)) {
            colors.add(StudentColor.GREEN);
        }
    }

    public void handleBlueDrop(DragEvent dragEvent) {
        if (gui.getCurrPhase().equals(Phase_GUI.Character10)) {
            colors.add(StudentColor.BLUE);
        }
    }

    public void otherBoards() {
        gui.viewSchoolBoard("", true);
    }

    public void setUpOtherBoards() {
        characterPane.setVisible(false);
        characterPane.setDisable(true);
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
            if (gui.isExpert()) {
                myCoin.setVisible(true);
                myCoinImage.setVisible(true);
                setMyCoinMessage(String.valueOf(player.getCoins()));
            }

            if (player.getUsedAssistant() != null) {
                int index = player.getUsedAssistant().getValue();
                Image image = new Image("/assets/Assistenti/3x/Animali_1_" + index + "@3x.png");
                MyCard.setImage(image);
            }
        } else if (player.getNickName().equals(players.get(1)) ||
                (players.size() > 2 && player.getNickName().equals(players.get(3)))) {
            p = OpponentBoard;
            OpponentNickname.setText(player.getNickName());
            if (gui.isExpert()) {
                opponentCoin.setVisible(true);
                opponentCoinImage.setVisible(true);
                setOpponentCoinMessage(String.valueOf(player.getCoins()));
            }
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
                tableArea.setVisible(false);
            }
            if (players.size() == 1 && gui.getCurrPhase().equals(Phase_GUI.Character7)) {
                imageView1.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                    for (Point point : entrance) {
                        if (point.getX() == imageView1.getTranslateX() && point.getY() == imageView1.getTranslateY()) {
                            if (entranceStudentsIndex.size() < 3) {
                                entranceStudentsIndex.add(entrance.indexOf(point));
                                setMessage("picked " + entranceStudentsIndex.size() + " entrance students. ");
                                if (entranceStudentsIndex.size() == 3) {
                                    if (characterStudentIndex.size() == 3) {
                                        gui.send(new SwapStudentsCharacterEntranceMessage(characterStudentIndex.stream().mapToInt(j -> j).toArray(), entranceStudentsIndex.stream().mapToInt(j -> j).toArray()));
                                        MyBoard.setDisable(true);
                                        characterPane.setDisable(true);
                                    }
                                }
                            } else {
                                setMessage("You already picked 3 students.");
                            }
                        }
                    }
                });
            }
            if (players.size() == 1 && gui.getCurrPhase().equals(Phase_GUI.Character10)) {
                imageView1.setOnDragDetected(evt -> {
                    Dragboard dragboard = imageView1.startDragAndDrop(TransferMode.COPY);
                    ClipboardContent content = new ClipboardContent();
                    content.putImage(finalStuImage);
                    dragboard.setContent(content);
                    for (Point point : entrance) {
                        if (point.getX() == imageView1.getTranslateX() && point.getY() == imageView1.getTranslateY()) {
                            if (entranceStudentsIndex.size() < 2) {
                                entranceStudentsIndex.add(entrance.indexOf(point));
                                setMessage("Picked " + entranceStudentsIndex.size() + " students");
                                if (entranceStudentsIndex.size() == 2) {
                                    StudentColor[] tempSt = new StudentColor[2];
                                    tempSt[0] = colors.get(0);
                                    tempSt[1] = colors.get(1);
                                    gui.send(new SwapStudentsTableEntranceMessage(tempSt, entranceStudentsIndex.stream().mapToInt(j -> j).toArray()));
                                    MyBoard.setDisable(true);
                                    characterPane.setDisable(true);
                                }
                            } else {
                                setMessage("You already picked 2 students.");
                            }
                        }
                    }
                });
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
        //draw towers
        for (int j = 0; j < gui.getGame().getIslands().size(); j++) {

        }
        for (int i = 0; i < player.getTowerNum(); i++) {
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

    public void enableMoveToIslandPane(boolean enable) {
        if (enable) {
            moveToIslandPane.setDisable(false);
            moveToIslandPane.setVisible(true);
        }
    }

    public void enableCharacterButton() {
        useCharacterButton.setDisable(false);
        useCharacterButton.setVisible(true);
    }

    public void enableCharacter1() {
        characterCardImage.setImage(new Image("/assets/Personaggi/CarteTOT_front.jpg"));
        characterCardImage.setDisable(false);
        characterCardImage.setVisible(true);
        Image stuImage = null;
        ArrayList<Point> character1 = new ArrayList<>();
        character1.add(new Point(-40, -40));
        character1.add(new Point(-40, 40));
        character1.add(new Point(40, -40));
        character1.add(new Point(40, 40));
        try {
            for (int i = 0; i < gui.getGame().getCharacterById(1).getStudents().size(); i++) {
                StudentColor stud = gui.getGame().getCharacterById(1).getStudents().get(i);
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
                Point temp = character1.get(i);
                imageView1.setTranslateX(temp.getX());
                imageView1.setTranslateY(temp.getY());
                if (gui.getCurrPhase().equals(Phase_GUI.Character1)) {
                    Image finalStuImage = stuImage;
                    imageView1.setOnDragDetected(evt -> {
                        Dragboard dragboard = imageView1.startDragAndDrop(TransferMode.COPY);
                        ClipboardContent content = new ClipboardContent();
                        content.putImage(finalStuImage);
                        dragboard.setContent(content);
                        for (Point point : character1) {
                            if (point.getX() == imageView1.getTranslateX() && point.getY() == imageView1.getTranslateY()) {
                                studentIndex = character1.indexOf(point);
                            }
                        }
                    });
                }
                characterPane.getChildren().add(imageView1);
            }
            if (gui.getCurrPhase().equals(Phase_GUI.Character1)) {
                tableArea.setDisable(true);
                tableArea.setVisible(false);
                enableMoveToIslandPane(true);
            }
        } catch (WrongEffectException e) {
            e.printStackTrace();
        }
    }
    public void enableCharacter2() {
        characterCardImage.setImage(new Image("/assets/Personaggi/CarteTOT_front12.jpg"));
        characterCardImage.setDisable(false);
        characterCardImage.setVisible(true);
    }
    public void enableCharacter3() {
        characterCardImage.setImage(new Image("/assets/Personaggi/CarteTOT_front2.jpg"));
        characterCardImage.setDisable(false);
        characterCardImage.setVisible(true);
    }
    public void enableCharacter4() {
        characterCardImage.setImage(new Image("/assets/Personaggi/CarteTOT_front3.jpg"));
        characterCardImage.setDisable(false);
        characterCardImage.setVisible(true);
    }
    public void enableCharacter5() {
        characterCardImage.setImage(new Image("/assets/Personaggi/CarteTOT_front4.jpg"));
        characterCardImage.setDisable(false);
        characterCardImage.setVisible(true);
        Image noEntryImage = null;
        ArrayList<Point> character1 = new ArrayList<>();
        character1.add(new Point(-40, -40));
        character1.add(new Point(-40, 40));
        character1.add(new Point(40, -40));
        character1.add(new Point(40, 40));
        try {
            for (int i = 0; i < gui.getGame().getCharacterById(5).getNumberOfNoEntries(); i++) {
                imageViews.add(new ImageView());
                ImageView imageView1 = imageViews.get(imageViews.size() - 1);
                noEntryImage = new Image("/assets/NoEntry.png");
                imageView1.setImage(noEntryImage);
                imageView1.setFitHeight(35);
                imageView1.setFitWidth(35);
                Point temp = character1.get(i);
                imageView1.setTranslateX(temp.getX());
                imageView1.setTranslateY(temp.getY());
                if (gui.getCurrPhase().equals(Phase_GUI.Character5)) {
                    Image finalStuImage = noEntryImage;
                    imageView1.setOnDragDetected(evt -> {
                        Dragboard dragboard = imageView1.startDragAndDrop(TransferMode.COPY);
                        ClipboardContent content = new ClipboardContent();
                        content.putImage(finalStuImage);
                        dragboard.setContent(content);
                        for (Point point : character1) {
                            if (point.getX() == imageView1.getTranslateX() && point.getY() == imageView1.getTranslateY()) {
                                studentIndex = character1.indexOf(point);
                            }
                        }
                    });
                }
                characterPane.getChildren().add(imageView1);
            }
            if (gui.getCurrPhase().equals(Phase_GUI.Character5)) {
                tableArea.setDisable(true);
                enableMoveToIslandPane(true);
            }
        } catch (WrongEffectException e) {
            e.printStackTrace();
        }

    }
    public void enableCharacter6() {
        characterCardImage.setImage(new Image("/assets/Personaggi/CarteTOT_front5.jpg"));
        characterCardImage.setDisable(false);
        characterCardImage.setVisible(true);
    }
    public void enableCharacter7() {
        characterCardImage.setImage(new Image("/assets/Personaggi/CarteTOT_front.jpg"));
        characterCardImage.setDisable(false);
        characterCardImage.setVisible(true);
        characterStudentIndex = new ArrayList<>();
        entranceStudentsIndex = new ArrayList<>();
        Image stuImage = null;
        ArrayList<Point> character1 = new ArrayList<>();
        character1.add(new Point(-40, -40));
        character1.add(new Point(-40, 40));
        character1.add(new Point(40, -40));
        character1.add(new Point(40, 40));
        character1.add(new Point(0, 0));
        character1.add(new Point(0, 40));
        try {
            for (int i = 0; i < gui.getGame().getCharacterById(7).getStudents().size(); i++) {
                StudentColor stud = gui.getGame().getCharacterById(7).getStudents().get(i);
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
                imageView1.setFitHeight(25);
                imageView1.setFitWidth(25);
                Point temp = character1.get(i);
                imageView1.setTranslateX(temp.getX());
                imageView1.setTranslateY(temp.getY());
                if (gui.getCurrPhase().equals(Phase_GUI.Character7)) {
                    imageView1.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                        for (Point point : character1) {
                            if (point.getX() == imageView1.getTranslateX() && point.getY() == imageView1.getTranslateY()) {
                                if (characterStudentIndex.size() < 3) {
                                    characterStudentIndex.add(character1.indexOf(point));
                                    setMessage("picked " + characterStudentIndex.size() + " character students. ");
                                    if (characterStudentIndex.size() == 3) {
                                        imageView1.setDisable(true);
                                        setMessage("Now choose entrance students.");
                                    }
                                } else {
                                    setMessage("U already picked 3 students!");
                                }

                            }
                        }
                    });
                }
                characterPane.getChildren().add(imageView1);
            }
            if (gui.getCurrPhase().equals(Phase_GUI.Character7)) {
                tableArea.setDisable(true);
                tableArea.setVisible(false);
                Button done = new Button();
                done.setText("done");
                done.setTranslateX(0);
                done.setTranslateY(90);
                EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e)
                    {
                        if (entranceStudentsIndex.size() != characterStudentIndex.size()) {
                            if (entranceStudentsIndex.size() > characterStudentIndex.size()) {
                                for (int i = entranceStudentsIndex.size() - characterStudentIndex.size(); i > 0; i--) {
                                    characterStudentIndex.remove(characterStudentIndex.size() - 1);
                                }
                            } else {
                                for (int i = characterStudentIndex.size() - entranceStudentsIndex.size(); i > 0; i--) {
                                    entranceStudentsIndex.remove(entranceStudentsIndex.size() - 1);
                                }
                            }
                        }
                        gui.send(new SwapStudentsCharacterEntranceMessage(characterStudentIndex.stream().mapToInt(j -> j).toArray(), entranceStudentsIndex.stream().mapToInt(j -> j).toArray()));
                        MyBoard.setDisable(true);
                        characterPane.setDisable(true);
                        done.setDisable(true);
                        done.setVisible(false);
                    }
                };
                done.setOnAction(event);
                characterPane.getChildren().add(done);
            }
        } catch (WrongEffectException e) {
            e.printStackTrace();
        }
    }

    public void enableCharacter8() {
        characterCardImage.setImage(new Image("/assets/Personaggi/CarteTOT_front7.jpg"));
        characterCardImage.setDisable(false);
        characterCardImage.setVisible(true);
    }

    public void enableCharacter9() {
        characterCardImage.setImage(new Image("/assets/Personaggi/CarteTOT_front8.jpg"));
        characterCardImage.setDisable(false);
        characterCardImage.setVisible(true);
    }

    public void enableCharacter10() {
        characterCardImage.setImage(new Image("/assets/Personaggi/CarteTOT_front9.jpg"));
        characterCardImage.setDisable(false);
        characterCardImage.setVisible(true);
        entranceStudentsIndex = new ArrayList<>();
        colors = new ArrayList<StudentColor>();
        disableCharacter10(false);
        if (gui.getCurrPhase().equals(Phase_GUI.Character10)) {
            tableArea.setDisable(true);
            tableArea.setVisible(false);
            Button done = new Button();
            done.setText("done");
            done.setTranslateX(0);
            done.setTranslateY(90);
            EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e)
                {
                    if (entranceStudentsIndex.size() < 2) {
                        StudentColor[] tempSt = new StudentColor[entranceStudentsIndex.size()];
                        if (entranceStudentsIndex.size() == 1) {
                            tempSt[0] = colors.get(0);
                            gui.send(new SwapStudentsTableEntranceMessage(tempSt, entranceStudentsIndex.stream().mapToInt(j -> j).toArray()));
                        } else {
                            gui.send(new SwapStudentsTableEntranceMessage(null, null));
                        }
                        MyBoard.setDisable(true);
                        characterPane.setDisable(true);
                    }
                    MyBoard.setDisable(true);
                    characterPane.setDisable(true);
                    done.setDisable(true);
                    done.setVisible(false);
                }
            };
            done.setOnAction(event);
            characterPane.getChildren().add(done);
        }
    }

    public void disableCharacter10(boolean bool) {
        if (!bool) {
            redArea.setDisable(false);
            redArea.setVisible(true);
            greenArea.setDisable(false);
            greenArea.setVisible(true);
            blueArea.setDisable(false);
            blueArea.setVisible(true);
            pinkArea.setDisable(false);
            pinkArea.setVisible(true);
            yellowArea.setDisable(false);
            yellowArea.setVisible(true);
        } else {
            redArea.setDisable(true);
            redArea.setVisible(false);
            greenArea.setDisable(true);
            greenArea.setVisible(false);
            blueArea.setDisable(true);
            blueArea.setVisible(false);
            pinkArea.setDisable(true);
            pinkArea.setVisible(false);
            yellowArea.setDisable(true);
            yellowArea.setVisible(false);
        }
    }

    public void enableCharacter11() {
        characterCardImage.setImage(new Image("/assets/Personaggi/CarteTOT_front10.jpg"));
        characterCardImage.setDisable(false);
        characterCardImage.setVisible(true);
        Image stuImage = null;
        ArrayList<Point> character1 = new ArrayList<>();
        character1.add(new Point(-40, -40));
        character1.add(new Point(-40, 40));
        character1.add(new Point(40, -40));
        character1.add(new Point(40, 40));
        try {
            for (int i = 0; i < gui.getGame().getCharacterById(11).getStudents().size(); i++) {
                StudentColor stud = gui.getGame().getCharacterById(11).getStudents().get(i);
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
                imageView1.setFitHeight(25);
                imageView1.setFitWidth(25);
                Point temp = character1.get(i);
                imageView1.setTranslateX(temp.getX());
                imageView1.setTranslateY(temp.getY());
                if (gui.getCurrPhase().equals(Phase_GUI.Character11)) {
                    Image finalStuImage = stuImage;
                    imageView1.setOnDragDetected(evt -> {
                        Dragboard dragboard = imageView1.startDragAndDrop(TransferMode.COPY);
                        ClipboardContent content = new ClipboardContent();
                        content.putImage(finalStuImage);
                        dragboard.setContent(content);
                        for (Point point : character1) {
                            if (point.getX() == imageView1.getTranslateX() && point.getY() == imageView1.getTranslateY()) {
                                studentIndex = character1.indexOf(point);
                            }
                        }
                    });
                }
                characterPane.getChildren().add(imageView1);
            }
        } catch (WrongEffectException e) {
            e.printStackTrace();
        }

    }
    public void enableCharacter12() {
        characterCardImage.setImage(new Image("/assets/Personaggi/CarteTOT_front11.jpg"));
        characterCardImage.setDisable(false);
        characterCardImage.setVisible(true);
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
}
