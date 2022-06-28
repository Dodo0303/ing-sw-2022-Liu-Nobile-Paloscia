package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
import it.polimi.ingsw.Model.Assistant;
import it.polimi.ingsw.Model.Character.CharacterCard;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.UseCharacterMessage;
import it.polimi.ingsw.Network.Messages.toServer.PlanningPhase.SendAssistantMessage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ChooseCharacterController implements Initializable {
    @FXML
    private RadioButton radio1, radio2, radio3, radio4, radio5, radio6, radio7, radio8, radio9, radio10, radio11, radio12;
    @FXML
    private Button ConfirmButton, CheckBoardButton;
    @FXML
    private Label messageLabel;
    @FXML
    ToggleGroup toggleGroup = new ToggleGroup();
    @FXML
    private ImageView pic1, pic2, pic3, pic4, pic5, pic6, pic7, pic8, pic9, pic10, pic11, pic12;
    GUI gui;
    int res;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        radio1.setToggleGroup(toggleGroup);
        radio2.setToggleGroup(toggleGroup);
        radio3.setToggleGroup(toggleGroup);
        radio4.setToggleGroup(toggleGroup);
        radio5.setToggleGroup(toggleGroup);
        radio6.setToggleGroup(toggleGroup);
        radio7.setToggleGroup(toggleGroup);
        radio8.setToggleGroup(toggleGroup);
        radio9.setToggleGroup(toggleGroup);
        radio10.setToggleGroup(toggleGroup);
        radio11.setToggleGroup(toggleGroup);
        radio12.setToggleGroup(toggleGroup);
        radio1.setDisable(true);
        radio2.setDisable(true);
        radio3.setDisable(true);
        radio4.setDisable(true);
        radio5.setDisable(true);
        radio6.setDisable(true);
        radio7.setDisable(true);
        radio8.setDisable(true);
        radio9.setDisable(true);
        radio10.setDisable(true);
        radio11.setDisable(true);
        radio12.setDisable(true);
    }

    public void execute() {
        gui.setPrevPhase(gui.getCurrPhase());
        if (radio1.isSelected()) {
            res = 1;
            checkValidity(res);
            gui.setCurrPhase(Phase_GUI.Character1);
            gui.setCurrCharacter(1);
            gui.viewSchoolBoard("Move a student from card.", false);
        } else if (radio2.isSelected()) {
            res = 2;
            checkValidity(res);
            gui.setCurrPhase(Phase_GUI.Character2);
            gui.setCurrCharacter(2);
        } else if (radio3.isSelected()) {
            res = 3;
            checkValidity(res);
            gui.setCurrPhase(Phase_GUI.Character3);
            gui.setCurrCharacter(3);
            gui.checkBoard("Choose an island.");
        } else if (radio4.isSelected()) {
            res = 4;
            checkValidity(res);
            gui.setCurrPhase(Phase_GUI.Character4);
            gui.setCurrCharacter(4);
        } else if (radio5.isSelected()) {
            res = 5;
            checkValidity(res);
            gui.setCurrPhase(Phase_GUI.Character5);
            gui.setCurrCharacter(5);
            gui.viewSchoolBoard("Move a noEntry tile.", false);
        } else if (radio6.isSelected()) {
            res = 6;
            checkValidity(res);
            gui.setCurrPhase(Phase_GUI.Character6);
            gui.setCurrCharacter(6);
        } else if (radio7.isSelected()) {
            res = 7;
            checkValidity(res);
            gui.setCurrPhase(Phase_GUI.Character7);
            gui.setCurrCharacter(7);
            gui.viewSchoolBoard("Click a student on the card. ", false);
        } else if (radio8.isSelected()) {
            res = 8;
            checkValidity(res);
            gui.setCurrPhase(Phase_GUI.Character8);
            gui.setCurrCharacter(8);
        } else if (radio9.isSelected()) {
            res = 9;
            checkValidity(res);
            gui.setCurrPhase(Phase_GUI.Character9);
            gui.setCurrCharacter(9);
            gui.pickColor();
        } else if (radio10.isSelected()) {
            res = 10;
            checkValidity(res);
            gui.setCurrPhase(Phase_GUI.Character10);
            gui.setCurrCharacter(10);
            gui.viewSchoolBoard("swap students", false);
        } else if (radio11.isSelected()) {
            res = 11;
            checkValidity(res);
            gui.setCurrPhase(Phase_GUI.Character11);
            gui.setCurrCharacter(11);
            gui.viewSchoolBoard("take a student to dining room.", false);
        } else if (radio12.isSelected()) {
            res = 12;
            checkValidity(res);
            gui.setCurrPhase(Phase_GUI.Character12);
            gui.setCurrCharacter(12);
            gui.pickColor();
        } else {
            setMessage("Please choose a card.");
            return;
        }
        radio1.setDisable(true);
        radio2.setDisable(true);
        radio3.setDisable(true);
        radio4.setDisable(true);
        radio5.setDisable(true);
        radio6.setDisable(true);
        radio7.setDisable(true);
        radio8.setDisable(true);
        radio9.setDisable(true);
        radio10.setDisable(true);
        radio11.setDisable(true);
        radio12.setDisable(true);
        ConfirmButton.setDisable(true);
        CheckBoardButton.setDisable(true);
        gui.send(new UseCharacterMessage(res));
    }

    public boolean checkValidity(int res) {
        if (!check(res)) {
            setMessage("You cannot use this card.");
            return false;
        }
        if (gui.getGame().getPlayerByNickname(gui.getNickname()).getCoins() < gui.getGame().getCharacterById(res).getPrice()) {
            gui.viewSchoolBoard("Character too expensive..", false);
            return false;
        }
        return true;
    }

    public void showDetails() {
        for (int h = 0; h < gui.getGame().getCharacters().size(); h++) {
            StringBuilder stringBuilder = new StringBuilder();
            if (gui.getGame().getCharacters().get(h).getID() == 1) {
                Tooltip tooltip1 = new Tooltip();
                try {
                    for (int i = 0; i < gui.getGame().getCharacterById(1).getStudents().size(); i++) {
                        stringBuilder.append(gui.getGame().getCharacterById(1).getStudents().get(i).toString()).append(" ");
                    }
                    tooltip1.setText(stringBuilder.toString());
                    Tooltip.install(pic1, tooltip1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (gui.getGame().getCharacters().get(h).getID() == 5) {
                Tooltip tooltip5 = new Tooltip();
                try {
                    stringBuilder.append("No entries: ");
                    stringBuilder.append(gui.getGame().getCharacterById(5).getNumberOfNoEntries()).append(" ");
                    tooltip5.setText(stringBuilder.toString());
                    Tooltip.install(pic5, tooltip5);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (gui.getGame().getCharacters().get(h).getID() == 7) {
                Tooltip tooltip7 = new Tooltip();
                try {
                    for (int i = 0; i < gui.getGame().getCharacterById(7).getStudents().size(); i++) {
                        stringBuilder.append(gui.getGame().getCharacterById(7).getStudents().get(i).toString()).append(" ");
                    }
                    tooltip7.setText(stringBuilder.toString());
                    Tooltip.install(pic7, tooltip7);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (gui.getGame().getCharacters().get(h).getID() == 11) {
                Tooltip tooltip11 = new Tooltip();
                try {
                    for (int i = 0; i < gui.getGame().getCharacterById(11).getStudents().size(); i++) {
                        stringBuilder.append(gui.getGame().getCharacterById(11).getStudents().get(i).toString()).append(" ");
                    }
                    tooltip11.setText(stringBuilder.toString());
                    Tooltip.install(pic11, tooltip11);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void back() {
        gui.checkBoard("");
    }

    private boolean check(int x) {
        try {
            gui.getGame().canAffordCharacter(gui.getNickname(),x);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public void setAvailableCharacters() {
        for (int i = 0; i < gui.getGame().getCharacters().size(); i++) {
            if (gui.getGame().getCharacters().get(i).getID() == 1) {
                radio1.setDisable(false);
            } else if (gui.getGame().getCharacters().get(i).getID() == 2) {
                radio2.setDisable(false);
            } else if (gui.getGame().getCharacters().get(i).getID() == 3) {
                radio3.setDisable(false);
            } else if (gui.getGame().getCharacters().get(i).getID() == 4) {
                radio4.setDisable(false);
            } else if (gui.getGame().getCharacters().get(i).getID() == 5) {
                radio5.setDisable(false);
            } else if (gui.getGame().getCharacters().get(i).getID() == 6) {
                radio6.setDisable(false);
            } else if (gui.getGame().getCharacters().get(i).getID() == 7) {
                radio7.setDisable(false);
            } else if (gui.getGame().getCharacters().get(i).getID() == 8) {
                radio8.setDisable(false);
            } else if (gui.getGame().getCharacters().get(i).getID() == 9) {
                radio9.setDisable(false);
            } else if (gui.getGame().getCharacters().get(i).getID() == 10) {
                radio10.setDisable(false);
            } else if (gui.getGame().getCharacters().get(i).getID() == 11) {
                radio11.setDisable(false);
            } else if (gui.getGame().getCharacters().get(i).getID() == 12) {
                radio12.setDisable(false);
            }
        }
    }

    public void disableChoose(boolean disable) {
        if (disable) {
            radio1.setDisable(true);
            radio2.setDisable(true);
            radio3.setDisable(true);
            radio4.setDisable(true);
            radio5.setDisable(true);
            radio6.setDisable(true);
            radio7.setDisable(true);
            radio8.setDisable(true);
            radio9.setDisable(true);
            radio10.setDisable(true);
            radio11.setDisable(true);
            radio12.setDisable(true);
            ConfirmButton.setDisable(true);
        }
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }
    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}
