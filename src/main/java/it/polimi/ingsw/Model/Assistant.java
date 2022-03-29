package it.polimi.ingsw.Model;

public class Assistant {
    private int value;
    private int maxSteps;
    private Wizard wizard;

    /**
     * TODO Should we handle Assistant class as a singleton?
     */
    public Assistant(int value, int maxSteps, Wizard wizard) {
        this.value = value;
        this.maxSteps = maxSteps;
        this.wizard = wizard;
    }

    /**
     * THIS = assistant.
     */
    public Assistant(Assistant assistant) {
        this.value = assistant.getValue();
        this.maxSteps = assistant.getMaxSteps();
        this.wizard = assistant.getWizard();
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
