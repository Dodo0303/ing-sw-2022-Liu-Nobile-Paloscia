package it.polimi.ingsw.Model;

import java.util.HashMap;
import java.util.Random;

public class Bag {
    /**
     * The hashmap is used to memorize number of students of each color in bag.
     * Students are 24 because the other 2 of each color should be already on the islands.
     */
    private HashMap<StudentColor, Integer> students;

    public Bag() {
        students = new HashMap<>();
        for(StudentColor color : StudentColor.values()) {
            students.put(color, 24);
        }
    }

    /**
     * @throws EmptyBagException when the bag is empty and no student can be extracted
     * @return string value of the student's color
     */
    public StudentColor extractStudent() throws EmptyBagException{
        if (isEmpty()) throw new EmptyBagException();
        else {
            StudentColor[] colors = StudentColor.values();
            int choice = new Random().nextInt(colors.length);
            while (students.get(colors[choice]) == 0) {
                choice = new Random().nextInt(colors.length);
            }
            int updatedValue = students.get(colors[choice]) - 1;
            students.replace(colors[choice], updatedValue);
            return colors[choice];
        }
    }

    public Boolean isEmpty() {
        for (int numberOfOneColor :
                students.values()) {
            if (numberOfOneColor != 0) return false;
        }

        return true;
    }


    /**
     *
     * @return how many students are still in the bag
     */
    public int getRemainingNum() {
        if (isEmpty()) return 0;
        int totalNumber = 0;
        for (int numberOfOneColor :
                students.values()) {
            totalNumber += numberOfOneColor;
        }

        return totalNumber;
    }
}
