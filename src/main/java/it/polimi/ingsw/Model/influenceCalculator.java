package it.polimi.ingsw.Model;

public interface influenceCalculator {
    int calculateInfluence(Player player, Island island);
}

class StandardInfluenceCalculator implements influenceCalculator {
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

class SpecialInfluenceCalculator implements influenceCalculator {
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
