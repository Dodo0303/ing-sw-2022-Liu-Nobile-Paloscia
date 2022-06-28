package it.polimi.ingsw.Model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class for the assistant card.
 */
public class Assistant implements Serializable {
    /**
     * Value of the assistant
     */
    private int value;
    /**
     * Maximum amount of steps that Mother Nature can do when this assistant is used
     */
    private int maxSteps;
    /**
     * Back of the card
     */
    private Wizard wizard;

    /**
     *
     * @param value index of the card. It can go from 1 to 10
     * @param maxSteps maximum amount of steps that mother nature can do using this card. It can go from 1 to 5
     * @param wizard indicates the back of the card
     */
    public Assistant(int value, int maxSteps, Wizard wizard) {
        this.value = value;
        this.maxSteps = maxSteps;
        this.wizard = wizard;
    }

    /**
     *
     * @return the value of this assistant
     */
    public int getValue() {
        return value;
    }

    /**
     *
     * @return the maximum amount of steps allowed by this assistant
     */
    public int getMaxSteps() {
        return maxSteps;
    }

    /**
     *
     * @return the back of this card
     */
    public Wizard getWizard() {
        return wizard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assistant assistant = (Assistant) o;
        return value == assistant.value && maxSteps == assistant.maxSteps && wizard == assistant.wizard;
    }

}
