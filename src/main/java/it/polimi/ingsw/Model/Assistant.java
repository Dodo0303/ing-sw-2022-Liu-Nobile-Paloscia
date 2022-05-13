package it.polimi.ingsw.Model;

import java.io.Serializable;
import java.util.Objects;

public class Assistant implements Serializable {
    private int value;
    private int maxSteps;
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

    public int getValue() {
        return value;
    }

    public int getMaxSteps() {
        return maxSteps;
    }

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
