package it.polimi.ingsw.Controller.InfluenceCalculators;

import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.StudentColor;

/**
 * Interface that represents all the classes that, given a player and an island, compute the influence of that player in that island
 */
public interface InfluenceCalculator {
    /**
     * Compute the influence of a certain player in an island
     * @param player player of which is required to compute the influence
     * @param island island of which is required to compute the influence
     * @return the influence of the player in the island
     */
    int calculateInfluence(Player player, Island island);
}

