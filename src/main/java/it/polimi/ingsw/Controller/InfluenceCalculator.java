package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.StudentColor;

public interface InfluenceCalculator {
    int calculateInfluence(Player player, Island island);
}

class StandardInfluenceCalculator implements InfluenceCalculator {
    @Override
    public int calculateInfluence(Player player, Island island) {
        int result = 0;
        if (island.getTowerColor() == player.getColor())
            result += island.getNumTower();
        for (StudentColor color :
                player.getProfessors()) {
            result += island.getStudents().get(color);
        }
        return result;
    }
}

class SpecialInfluenceCalculator implements InfluenceCalculator {
    @Override
    public int calculateInfluence(Player player, Island island) {
        int result = 0;

        for (StudentColor color :
                player.getProfessors()) {
            result += island.getStudents().get(color);
        }
        return result;
    }
}
