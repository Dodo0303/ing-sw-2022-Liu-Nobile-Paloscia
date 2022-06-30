package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.Phase;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.UseCharacterMessage;
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
            gui.setCurrPhase(Phase.Character1);
            gui.setCurrCharacter(1);
            gui.viewSchoolBoard("Move a student from card.", false);
        } else if (radio2.isSelected()) {
            res = 2;
            checkValidity(res);
            gui.setCurrPhase(Phase.Character2);
            gui.setCurrCharacter(2);
        } else if (radio3.isSelected()) {
            res = 3;
            checkValidity(res);
            gui.setCurrPhase(Phase.Character3);
            gui.setCurrCharacter(3);
            gui.checkBoard("Choose an island.");
        } else if (radio4.isSelected()) {
            res = 4;
            checkValidity(res);
            gui.setCurrPhase(Phase.Character4);
            gui.setCurrCharacter(4);
        } else if (radio5.isSelected()) {
            res = 5;
            checkValidity(res);
            gui.setCurrPhase(Phase.Character5);
            gui.setCurrCharacter(5);
            gui.viewSchoolBoard("Move a noEntry tile.", false);
        } else if (radio6.isSelected()) {
            res = 6;
            checkValidity(res);
            gui.setCurrPhase(Phase.Character6);
            gui.setCurrCharacter(6);
        } else if (radio7.isSelected()) {
            res = 7;
            checkValidity(res);
            gui.setCurrPhase(Phase.Character7);
            gui.setCurrCharacter(7);
            gui.viewSchoolBoard("Click a student on the card. ", false);
        } else if (radio8.isSelected()) {
            res = 8;
            checkValidity(res);
            gui.setCurrPhase(Phase.Character8);
            gui.setCurrCharacter(8);
        } else if (radio9.isSelected()) {
            res = 9;
            checkValidity(res);
            gui.setCurrPhase(Phase.Character9);
            gui.setCurrCharacter(9);
            gui.pickColor();
        } else if (radio10.isSelected()) {
            res = 10;
            checkValidity(res);
            gui.setCurrPhase(Phase.Character10);
            gui.setCurrCharacter(10);
            gui.viewSchoolBoard("swap students", false);
        } else if (radio11.isSelected()) {
            res = 11;
            checkValidity(res);
            gui.setCurrPhase(Phase.Character11);
            gui.setCurrCharacter(11);
            gui.viewSchoolBoard("take a student to dining room.", false);
        } else if (radio12.isSelected()) {
            res = 12;
            checkValidity(res);
            gui.setCurrPhase(Phase.Character12);
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
                    stringBuilder.append("Effect: Take 1 student from this card and place it on an island of your choice.\n");
                    stringBuilder.append("Students: ");
                    for (int i = 0; i < gui.getGame().getCharacterById(1).getStudents().size(); i++) {
                        stringBuilder.append(gui.getGame().getCharacterById(1).getStudents().get(i).toString()).append(" ");
                    }
                    stringBuilder.append("\n");
                    tooltip1.setText(stringBuilder.toString());
                    Tooltip.install(pic1, tooltip1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (gui.getGame().getCharacters().get(h).getID() == 2) {
                Tooltip tooltip2 = new Tooltip();
                try {
                    stringBuilder.append("Effect: During this turn, you take control of any number of professors,\neven if you have the same number of students as the player who currently controls them.\n");
                    tooltip2.setText(stringBuilder.toString());
                    Tooltip.install(pic2, tooltip2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }  else if (gui.getGame().getCharacters().get(h).getID() == 3) {
                Tooltip tooltip3 = new Tooltip();
                try {
                    stringBuilder.append("Effect: Choose an island and resolve the island as if the mother nature had ended her movement there.\n");
                    tooltip3.setText(stringBuilder.toString());
                    Tooltip.install(pic3, tooltip3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }  else if (gui.getGame().getCharacters().get(h).getID() == 4) {
                Tooltip tooltip4 = new Tooltip();
                try {
                    stringBuilder.append("Effect: You may move the mother nature up to 2 additional islands than is indicated by the assistant card.\n");
                    tooltip4.setText(stringBuilder.toString());
                    Tooltip.install(pic4, tooltip4);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }  else if (gui.getGame().getCharacters().get(h).getID() == 5) {
                Tooltip tooltip5 = new Tooltip();
                try {
                    stringBuilder.append("Effect: During your turn, you take control of any number of professors, \neven if you have the same number of students as the player who currently controls them.\n");
                    stringBuilder.append("Number of entries: ");
                    stringBuilder.append(gui.getGame().getCharacterById(5).getNumberOfNoEntries()).append(" ");
                    stringBuilder.append("\n");
                    tooltip5.setText(stringBuilder.toString());
                    Tooltip.install(pic5, tooltip5);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (gui.getGame().getCharacters().get(h).getID() == 6) {
                Tooltip tooltip6 = new Tooltip();
                try {
                    stringBuilder.append("Effect: When resolving a conquering on an island, towers do not count towards influence.\n");
                    tooltip6.setText(stringBuilder.toString());
                    Tooltip.install(pic6, tooltip6);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }  else if (gui.getGame().getCharacters().get(h).getID() == 7) {
                Tooltip tooltip7 = new Tooltip();
                try {
                    stringBuilder.append("Effect: You may take up to 3 students from this card, \nand replace them with the same number of students from your entrance.\n");
                    stringBuilder.append("Students: ");
                    for (int i = 0; i < gui.getGame().getCharacterById(7).getStudents().size(); i++) {
                        stringBuilder.append(gui.getGame().getCharacterById(7).getStudents().get(i).toString()).append(" ");
                    }
                    stringBuilder.append("\n");
                    tooltip7.setText(stringBuilder.toString());
                    Tooltip.install(pic7, tooltip7);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (gui.getGame().getCharacters().get(h).getID() == 8) {
                Tooltip tooltip8 = new Tooltip();
                try {
                    stringBuilder.append("Effect: During the influence calculation, you count as having 2 more influence.\n");
                    tooltip8.setText(stringBuilder.toString());
                    Tooltip.install(pic8, tooltip8);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (gui.getGame().getCharacters().get(h).getID() == 9) {
                Tooltip tooltip9 = new Tooltip();
                try {
                    stringBuilder.append("Effect: Choose a color of student, during the influence calculation, that color won't be taken into consideration.\n");
                    tooltip9.setText(stringBuilder.toString());
                    Tooltip.install(pic9, tooltip9);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }  else if (gui.getGame().getCharacters().get(h).getID() == 10) {
                Tooltip tooltip10 = new Tooltip();
                try {
                    stringBuilder.append("Effect: You may exchange up to 2 students between your entrance and your dining room.\n");
                    tooltip10.setText(stringBuilder.toString());
                    Tooltip.install(pic10, tooltip10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (gui.getGame().getCharacters().get(h).getID() == 11) {
                Tooltip tooltip11 = new Tooltip();
                try {
                    stringBuilder.append("Effect: Take 1 student from this card and place it on your dining room.\n");
                    stringBuilder.append("Students: ");
                    for (int i = 0; i < gui.getGame().getCharacterById(11).getStudents().size(); i++) {
                        stringBuilder.append(gui.getGame().getCharacterById(11).getStudents().get(i).toString()).append(" ");
                    }
                    stringBuilder.append("\n");
                    tooltip11.setText(stringBuilder.toString());
                    Tooltip.install(pic11, tooltip11);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (gui.getGame().getCharacters().get(h).getID() == 12) {
                Tooltip tooltip12 = new Tooltip();
                try {
                    stringBuilder.append("Effect: Choose a type of student, every player must return 3 students of that type from the dining room to the bag.\nIf any player has fewer than 3 students of that type, return as many as they have.\n");
                    tooltip12.setText(stringBuilder.toString());
                    Tooltip.install(pic12, tooltip12);
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
