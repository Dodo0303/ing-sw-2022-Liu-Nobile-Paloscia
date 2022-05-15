package it.polimi.ingsw.Controller.InfluenceCalculators;

import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.StudentColor;

public class NoTowerInfluenceCalculator implements InfluenceCalculator {
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
