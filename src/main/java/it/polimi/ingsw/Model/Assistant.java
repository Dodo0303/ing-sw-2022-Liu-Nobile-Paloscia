package it.polimi.ingsw.Model;

public class Assistant {
    private int value;
    private int maxSteps;
    private Wizard wizard;

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
}
