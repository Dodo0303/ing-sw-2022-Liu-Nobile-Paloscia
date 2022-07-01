package it.polimi.ingsw.Controller.InfluenceCalculators;

import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.StudentColor;

/**
 * This Influence Calculator computes the influence excluding a specific color, chosen by the player, from the computation.
 */
public class ExcludeColorInfluenceCalculator implements InfluenceCalculator {
    StudentColor color;

    public ExcludeColorInfluenceCalculator(StudentColor color) {
        this.color = color;
    }

    @Override
    public int calculateInfluence(Player player, Island island) {
        int result = 0;
        if (island.getTowerColor() == player.getColor())
            result += island.getNumTower();
        for (StudentColor color :
                player.getProfessors()) {
            if (color != this.color)
                result += island.getStudents().get(color);
        }
        return result;
    }
}
