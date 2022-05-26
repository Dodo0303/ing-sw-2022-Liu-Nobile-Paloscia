package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.StudentColor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SchoolBoardController implements Initializable {
    @FXML
    private Label MyNickname, OpponentNickname;
    @FXML
    private ImageView MyCard, OpponentCard;
    private GUI gui;
    private ArrayList<Point> greenStudents, redStudents, yellowStudents, pinkStudents, blueStudents, entrance, towers;
    private ArrayList<StackPane> boards;
    private Point greenProf, redProf, yellowProf, pinkProf, blueProf;
    @FXML
    private StackPane OpponentBoard, MyBoard;
    private ArrayList<ImageView> imageViews;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCoords();
    }

    public void otherBoards() {
        //todo
    }

    public void drawSchoolBoard(Player player) {
        StackPane p;
        if (player.getNickName().equals(gui.getNickname())) {
            p = MyBoard;
            MyNickname.setText(player.getNickName());
            if (player.getUsedAssistant() != null) {
                int index = player.getUsedAssistant().getValue();
                Image image = new Image("/assets/Assistenti/3x/Animali_1_" + index + "@3x.png");
                MyCard.setImage(image);
            }
        } else {
            p = OpponentBoard;
            OpponentNickname.setText(player.getNickName());
            if (player.getUsedAssistant() != null) {
                int index = player.getUsedAssistant().getValue();
                Image image = new Image("/assets/Assistenti/3x/Animali_1_" + index + "@3x.png");
                OpponentCard.setImage(image);
            }
        }
        for (int i = 0; i < gui.getGame().getEntranceOfPlayer(player).size(); i++) {
            StudentColor stud = gui.getGame().getEntranceOfPlayer(player).get(i);
            imageViews.add(new ImageView());
            ImageView imageView1 = imageViews.get(imageViews.size() - 1);
            if (stud.equals(StudentColor.GREEN)) {
                Image stu = new Image("/assets/Students/green.png");
                imageView1.setImage(stu);
            } else if (stud.equals(StudentColor.RED)) {
                Image stu = new Image("/assets/Students/red.png");
                imageView1.setImage(stu);
            } else if (stud.equals(StudentColor.YELLOW)) {
                Image stu = new Image("/assets/Students/yellow.png");
                imageView1.setImage(stu);
            } else if (stud.equals(StudentColor.PINK)) {
                Image stu = new Image("/assets/Students/pink.png");
                imageView1.setImage(stu);
            } else if (stud.equals(StudentColor.BLUE)) {
                Image stu = new Image("/assets/Students/blue.png");
                imageView1.setImage(stu);
            }
            imageView1.setFitHeight(35);
            imageView1.setFitWidth(35);
            Point temp = entrance.get(i);
            imageView1.setTranslateX(temp.getX());
            imageView1.setTranslateY(temp.getY());
            p.getChildren().add(imageView1);
        }

        for (StudentColor color : StudentColor.values()) {
            int index = 0;
            int stud = player.getDiningTables().get(color).getNumOfStudents();
            imageViews.add(new ImageView());
            ImageView imageView1 = imageViews.get(imageViews.size() - 1);
            Point temp = null;
            for (int i = 0; i < stud; i++) {
                if (color.equals(StudentColor.GREEN)) {
                    Image stu = new Image("/assets/Students/green.png");
                    imageView1.setImage(stu);
                    temp = greenStudents.get(index++);
                } else if (color.equals(StudentColor.RED)) {
                    Image stu = new Image("/assets/Students/red.png");
                    imageView1.setImage(stu);
                    temp = redStudents.get(index++);
                } else if (color.equals(StudentColor.YELLOW)) {
                    Image stu = new Image("/assets/Students/yellow.png");
                    imageView1.setImage(stu);
                    temp = yellowStudents.get(index++);
                } else if (color.equals(StudentColor.PINK)) {
                    Image stu = new Image("/assets/Students/pink.png");
                    imageView1.setImage(stu);
                    temp = pinkStudents.get(index++);
                } else if (color.equals(StudentColor.BLUE)) {
                    Image stu = new Image("/assets/Students/blue.png");
                    imageView1.setImage(stu);
                    temp = blueStudents.get(index++);
                }
                imageView1.setFitHeight(25);
                imageView1.setFitWidth(25);
                imageView1.setTranslateX(temp.getX());
                imageView1.setTranslateY(temp.getY());
                p.getChildren().add(imageView1);
            }
        }

        for (int i = 0; i < player.getProfessors().size(); i++) {
            StudentColor prof = player.getProfessors().get(i);
            imageViews.add(new ImageView());
            ImageView imageView1 = imageViews.get(imageViews.size() - 1);
            Point temp = null;
            if (prof.equals(StudentColor.GREEN)) {
                Image stu = new Image("/assets/Students/green.png");
                imageView1.setImage(stu);
                temp = greenProf;
            } else if (prof.equals(StudentColor.RED)) {
                Image stu = new Image("/assets/Students/red.png");
                imageView1.setImage(stu);
                temp = redProf;
            } else if (prof.equals(StudentColor.YELLOW)) {
                Image stu = new Image("/assets/Students/yellow.png");
                imageView1.setImage(stu);
                temp = yellowProf;
            } else if (prof.equals(StudentColor.PINK)) {
                Image stu = new Image("/assets/Students/pink.png");
                imageView1.setImage(stu);
                temp = pinkProf;
            } else if (prof.equals(StudentColor.BLUE)) {
                Image stu = new Image("/assets/Students/blue.png");
                imageView1.setImage(stu);
                temp = blueProf;
            }
            imageView1.setFitHeight(35);
            imageView1.setFitWidth(35);
            imageView1.setTranslateX(temp.getX());
            imageView1.setTranslateY(temp.getY());
            p.getChildren().add(imageView1);
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
        gui.checkBoard();
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
    }
}
