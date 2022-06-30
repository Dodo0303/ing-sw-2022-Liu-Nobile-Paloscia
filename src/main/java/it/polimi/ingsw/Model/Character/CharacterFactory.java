package it.polimi.ingsw.Model.Character;

import it.polimi.ingsw.Exceptions.EmptyBagException;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.StudentColor;

/**
 * Class used to generate characters, based on their ID
 */
public class CharacterFactory {
    private GameModel gameModel;

    /**
     *
     * @param gameModel Game model that wants to generate the characters
     */
    public CharacterFactory(GameModel gameModel){
        this.gameModel = gameModel;
    }


    /**
     * Draws a certain amount of students from the bag in order to put them on a character
     * @param quantity number of students to be taken
     * @return students to put on the character card
     */
    private StudentColor[] generateStudents(int quantity) throws GameException{
        StudentColor[] students = new StudentColor[quantity];
        for (int i = 0; i < quantity; i++) {
            try {
                students[i] = gameModel.drawStudentFromBag();
            } catch (EmptyBagException e) {
                e.printStackTrace();
                throw new GameException("Bag is empty");
            }
        }
        return students;
    }

    /**
     * Returns the correct character based on the ID given by input
     * @param ID ID of the character
     * @return return the character chosen
     */
    public CharacterCard createCharacter(int ID) throws GameException{
        CharacterCard res;
        switch (ID){
            case 1:
                res = new Character1(generateStudents(4));
                break;
            case 2:
                res = new Character2();
                break;
            case 3:
                res = new Character3();
                break;
            case 4:
                res = new Character4();
                break;
            case 5:
                res = new Character5();
                break;
            case 6:
                res = new Character6();
                break;
            case 7:
                res = new Character7(generateStudents(6));
                break;
            case 8:
                res = new Character8();
                break;
            case 9:
                res = new Character9();
                break;
            case 10:
                res = new Character10();
                break;
            case 11:
                res = new Character11(generateStudents(4));
                break;
            case 12:
                res = new Character12();
                break;
            default:
                throw new GameException("No character found: " + ID); //TODO Fix this
        }
        return res;
    }
}
