package it.polimi.ingsw.Model;

import java.io.Serializable;

/**
 * Enumeration for the color of the tiles
 */
public enum StudentColor implements Serializable {
    /** Possible tiles color. */
    GREEN("\uD83D\uDFE2"),
    BLUE("\uD83D\uDD35"),
    YELLOW("\uD83D\uDFE1"),
    RED("\uD83D\uDD34"),
    PINK("\uD83D\uDFE3");

    /**
     * Code of the color
     */
    private String color;

    StudentColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        switch (color) {
            case "\uD83D\uDFE2":
                return "Green";
            case "\uD83D\uDD35":
                return "Blue";
            case "\uD83D\uDD34":
                return "Red";
            case "\uD83D\uDFE1":
                return "Yellow";
            default:
                return "Pink";
        }
    }
}
