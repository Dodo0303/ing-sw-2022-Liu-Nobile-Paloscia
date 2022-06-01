package it.polimi.ingsw.Model;

import java.io.Serializable;

public enum StudentColor implements Serializable {
    /** Possible student colors. */
    GREEN("\uD83D\uDFE2"),
    BLUE("\uD83D\uDD35"),
    YELLOW("\uD83D\uDFE1"),
    RED("\uD83D\uDD34"),
    PINK("\uD83D\uDFE3");

    private String color;

    StudentColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return color;
    }
}
