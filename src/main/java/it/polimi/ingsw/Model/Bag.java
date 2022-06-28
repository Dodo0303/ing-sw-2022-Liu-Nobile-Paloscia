package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.EmptyBagException;
import it.polimi.ingsw.Exceptions.TooManyStudentsException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

/**
 * Class for the bag.
 */
public class Bag implements Serializable {
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
     * Extract a student from the bag
     * @throws EmptyBagException when the bag is empty and no student can be extracted
     * @return color of the student extracted
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

    /**
     * Checks whether the bag is empty or not
     * @return true if the bag is empty, false if not.
     */
    public Boolean isEmpty() {
        for (int numberOfOneColor :
                students.values()) {
            if (numberOfOneColor != 0) return false;
        }

        return true;
    }


    /**
     * Get the number of remaining students in the bag
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

    /**
     * Add a student to the bag. May be useful for implementing some card effect
     * @param student student to be added
     * @throws TooManyStudentsException if in the bag there are already 26 students of that color
     */
    public void addStudent(StudentColor student) throws TooManyStudentsException{
        if (students.get(student) == 26)
            throw new TooManyStudentsException();
        students.replace(student, students.get(student) + 1);
    }

    /**
     * @return number of students with that color
     */
    public int getNumOfStudentsByColor(StudentColor color) {
        return students.get(color);
    }
}
