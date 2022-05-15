package it.polimi.ingsw.Controller.InfluenceCalculators;

import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.StudentColor;

public interface InfluenceCalculator {
    int calculateInfluence(Player player, Island island);
}

