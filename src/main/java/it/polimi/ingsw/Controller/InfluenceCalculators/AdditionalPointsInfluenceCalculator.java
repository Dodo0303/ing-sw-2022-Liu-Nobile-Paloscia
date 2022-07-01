package it.polimi.ingsw.Controller.InfluenceCalculators;

import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Model.Player;

/**
 * This Influence Calculator computes the influence adding two additional points to the player that used the character
 */
public class AdditionalPointsInfluenceCalculator implements  InfluenceCalculator{
    /**
     * Player nickname that will get 2 additional points
     */
    private String withAdditionalPoints;

    public AdditionalPointsInfluenceCalculator(String withAdditionalPoints) {
        this.withAdditionalPoints = withAdditionalPoints;
    }

    @Override
    public int calculateInfluence(Player player, Island island) {
        InfluenceCalculator standardCalculator = new StandardInfluenceCalculator();
        int res;
        if (withAdditionalPoints.equals(player.getNickName()))
            return standardCalculator.calculateInfluence(player, island) + 2;
        return standardCalculator.calculateInfluence(player, island);
    }
}
