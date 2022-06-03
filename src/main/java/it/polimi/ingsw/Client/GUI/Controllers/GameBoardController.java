package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.ChooseCloudMessage;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.MoveMotherNatureMessage;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.MoveStudentFromEntranceMessage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameBoardController implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private StackPane cloud0, cloud1, cloud2, cloud3;
    @FXML
    private Label messageLabel;
    @FXML
    private Button backButton;
    private ArrayList<Point> students, towers, cloudStudents;
    private ArrayList<ImageView> islandImageViews, imageViews;
    private ArrayList<StackPane> clouds;
    private Point motherNature;
    private GUI gui;
    private boolean moveStudent, moveMotherNature, chooseCloud;
    private int studentIndex;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCoords();
        moveStudent = false;
        moveMotherNature = false;
        chooseCloud = false;
    }

    public void back() {
        if (moveStudent) {
            gui.moveStudentsFromEntrance("Move a student");
        } else {
            gui.playAssistant("");
        }
    }

    public void viewSchoolBoard() {
        if (gui.getCurrPhase().equals(Phase_GUI.Action1)) {
            gui.moveStudentsFromEntrance("");
        } else {
            gui.viewSchoolBoard("", false);
        }
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    private void initCoords() {
        imageViews = new ArrayList<>();
        islandImageViews = new ArrayList<>();
        students = new ArrayList<>();
        towers = new ArrayList<>();
        cloudStudents = new ArrayList<>();
        clouds = new ArrayList<>();
        clouds.add(cloud0);
        clouds.add(cloud1);
        clouds.add(cloud2);
        clouds.add(cloud3);
        cloudStudents.add(new Point(-50, 0));
        cloudStudents.add(new Point(30, 40));
        cloudStudents.add(new Point(5, 5));
        cloudStudents.add(new Point(10, -30));
        students.add(new Point(60, -60));
        students.add(new Point(30, -40));
        students.add(new Point(60, -30));
        students.add(new Point(-75, 0));
        students.add(new Point(-50, 0));
        students.add(new Point(-25, 0));
        students.add(new Point(0, 0));
        students.add(new Point(25, 0));
        students.add(new Point(50, 0));
        students.add(new Point(75, 0));
        students.add(new Point(-60, 35));
        students.add(new Point(-35, 35));
        students.add(new Point(-10, 35));
        students.add(new Point(-60, 60));
        students.add(new Point(-35, 60));
        students.add(new Point(-10, 60));
        students.add(new Point(10, 35));
        students.add(new Point(35, 35));
        students.add(new Point(60, 35));
        students.add(new Point(10, 60));
        students.add(new Point(35, 60));
        students.add(new Point(60, 60));
        towers.add(new Point(-20, -80));
        towers.add(new Point(-10, -40));
        towers.add(new Point(25, -80));
        motherNature = new Point(-50, -50);
    }

    public void cloud0Chosen() {
        if (chooseCloud)
            gui.send(new ChooseCloudMessage(0));
    }

    public void cloud1Chosen() {
        if (chooseCloud)
            gui.send(new ChooseCloudMessage(1));
    }

    public void cloud2Chosen() {
        if (chooseCloud)
            gui.send(new ChooseCloudMessage(2));
    }

    public void cloud3Chosen() {
        if (chooseCloud)
            gui.send(new ChooseCloudMessage(3));
    }

    public void drawClouds(int numClouds) {
        int index = 0;
        if (numClouds == 3) {
            cloud2.setVisible(true);
        } else if (numClouds == 4){
            cloud2.setVisible(true);
            cloud3.setVisible(true);
        }
        for (Cloud cloud : gui.getGame().getClouds()) {
            ArrayList<Point> tempStudents = new ArrayList<>(cloudStudents);
            for (int i = 0; i < cloud.getStudents().size(); i++) {
                imageViews.add(new ImageView());
                ImageView imageView1 = imageViews.get(imageViews.size() - 1);
                if (cloud.getStudents().get(i).equals(StudentColor.GREEN)) {
                    Image stu = new Image("/assets/Students/green.png");
                    imageView1.setImage(stu);
                } else if (cloud.getStudents().get(i).equals(StudentColor.RED)) {
                    Image stu = new Image("/assets/Students/red.png");
                    imageView1.setImage(stu);
                } else if (cloud.getStudents().get(i).equals(StudentColor.YELLOW)) {
                    Image stu = new Image("/assets/Students/yellow.png");
                    imageView1.setImage(stu);
                } else if (cloud.getStudents().get(i).equals(StudentColor.PINK)) {
                    Image stu = new Image("/assets/Students/pink.png");
                    imageView1.setImage(stu);
                } else if (cloud.getStudents().get(i).equals(StudentColor.BLUE)) {
                    Image stu = new Image("/assets/Students/blue.png");
                    imageView1.setImage(stu);
                }
                imageView1.setFitHeight(25);
                imageView1.setFitWidth(25);
                Point temp = tempStudents.remove((int)(Math.random() * tempStudents.size()));
                imageView1.setTranslateX(temp.getX());
                imageView1.setTranslateY(temp.getY());
                clouds.get(index).getChildren().add(imageView1);
            }
            index++;
        }
    }

    public void drawIslands(int numIslands) {
        int x0 = 846;
        int y0 = 540;
        for (int i = 0; i < gui.getGame().getIslands().size(); i++) {
            Island island = gui.getGame().getIslands().get(i);
            double x = 440 * Math.cos(2 * Math.PI * i / numIslands - Math.PI / 2) + x0 - 100;
            double y = 440 * Math.sin(2 * Math.PI * i / numIslands - Math.PI / 2) + y0 - 100;
            StackPane stackPane = new StackPane();
            stackPane.setLayoutX(x);
            stackPane.setLayoutY(y);
            stackPane.setPrefWidth(200);
            stackPane.setPrefHeight(200);
            islandImageViews.add(new ImageView());
            ImageView imageView = islandImageViews.get(islandImageViews.size() - 1);
            Image il = new Image("/assets/Island2.png");
            imageView.setImage(il);
            imageView.setFitWidth(190);
            imageView.setFitHeight(190);
            Tooltip tooltip = new Tooltip();
            StringBuilder stringBuilder = new StringBuilder();
            for (StudentColor color : island.getStudents().keySet()) {
                stringBuilder.append(color).append(": ").append(island.getStudents().get(color)).append("\n");
            }
            if (!island.getTowerColor().equals(Color.VOID)) {
                stringBuilder.append("Tower: ").append(island.getNumTower()).append(" ").append(island.getTowerColor());
            }
            tooltip.setText(stringBuilder.toString());
            Tooltip.install(imageView, tooltip);
            if (moveStudent) {
                imageView.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                    int islandChosen = islandImageViews.indexOf(imageView);
                    gui.send(new MoveStudentFromEntranceMessage(studentIndex, 1, islandChosen));
                    event.consume();
                });
            }
            if (moveMotherNature) {
                imageView.setOnDragOver(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent dragEvent) {
                        if (dragEvent.getDragboard().hasImage()) {
                            dragEvent.acceptTransferModes(TransferMode.COPY);
                            dragEvent.consume();
                        }
                    }
                });
                imageView.setOnDragDropped(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent dragEvent) {
                        dragEvent.setDropCompleted(true);
                        int islandChosen = islandImageViews.indexOf(imageView);
                        gui.send(new MoveMotherNatureMessage(islandChosen));
                        dragEvent.consume();
                    }
                });
            }
            stackPane.getChildren().add(imageView);
            ArrayList<Point> tempStudents = new ArrayList<>(students);
            for (StudentColor color : island.getStudents().keySet()) {
                for (int j = 0; j < island.getStudents().get(color); j++) {
                    imageViews.add(new ImageView());
                    ImageView imageView1 = imageViews.get(imageViews.size() - 1);
                    if (color.equals(StudentColor.GREEN)) {
                        Image stu = new Image("/assets/Students/green.png");
                        imageView1.setImage(stu);
                    } else if (color.equals(StudentColor.RED)) {
                        Image stu = new Image("/assets/Students/red.png");
                        imageView1.setImage(stu);
                    } else if (color.equals(StudentColor.YELLOW)) {
                        Image stu = new Image("/assets/Students/yellow.png");
                        imageView1.setImage(stu);
                    } else if (color.equals(StudentColor.PINK)) {
                        Image stu = new Image("/assets/Students/pink.png");
                        imageView1.setImage(stu);
                    } else if (color.equals(StudentColor.BLUE)) {
                        Image stu = new Image("/assets/Students/blue.png");
                        imageView1.setImage(stu);
                    }
                    imageView1.setFitHeight(25);
                    imageView1.setFitWidth(25);
                    Point temp = tempStudents.remove((int)(Math.random() * tempStudents.size()));
                    imageView1.setTranslateX(temp.getX());
                    imageView1.setTranslateY(temp.getY());
                    stackPane.getChildren().add(imageView1);
                }
            }
            ArrayList<Point> tempTowers = new ArrayList<>(towers);
            for (int h = 0; h < island.getNumTower(); h++) {
                imageViews.add(new ImageView());
                ImageView imageView2 = imageViews.get(imageViews.size() - 1);
                if (island.getTowerColor().equals(Color.WHITE)) {
                    Image tow = new Image("/assets/Towers/white.png");
                    imageView2.setImage(tow);
                } else if (island.getTowerColor().equals(Color.GRAY)) {
                    Image tow = new Image("/assets/Towers/gray.png");
                    imageView2.setImage(tow);
                } else {
                    Image tow = new Image("/assets/Towers/black.png");
                    imageView2.setImage(tow);
                }
                Point temp = tempTowers.remove((int)(Math.random() * tempTowers.size()));
                imageView2.setTranslateX(temp.getX());
                imageView2.setTranslateY(temp.getY());
                imageView2.setFitWidth(20);
                imageView2.setFitHeight(40);
                stackPane.getChildren().add(imageView2);
            }
            if (gui.getGame().getMotherNatureIndex() == i) {
                Image mot = new Image("/assets/mothernature.png");
                imageViews.add(new ImageView());
                ImageView imageView3 = imageViews.get(imageViews.size() - 1);
                imageView3.setImage(mot);
                imageView3.setTranslateX(-50);
                imageView3.setTranslateY(-50);
                imageView3.setFitWidth(20);
                imageView3.setFitHeight(30);
                if (moveMotherNature) {
                    imageView3.addEventFilter(MouseEvent.DRAG_DETECTED, event -> {
                        Dragboard dragboard = imageView3.startDragAndDrop(TransferMode.COPY);
                        ClipboardContent content = new ClipboardContent();
                        content.putImage(mot);
                        dragboard.setContent(content);
                    });
                }
                stackPane.getChildren().add(imageView3);
            }
            anchorPane.getChildren().add(stackPane);
        }
    }

    public void disableBack() {
        backButton.setDisable(true);
        backButton.setVisible(false);
    }
    public void setMessage(String msg) {
        messageLabel.setText(msg);
    }

    public void setMoveStudent(boolean moveStudent) {
        this.moveStudent = moveStudent;
    }

    public void setMoveMotherNature(boolean moveMotherNature) {
        this.moveMotherNature = moveMotherNature;
    }

    public void setChooseCloud(boolean chooseCloud) {
        this.chooseCloud = chooseCloud;
    }

    public void setStudentIndex(int studentIndex) {
        this.studentIndex = studentIndex;
    }


}