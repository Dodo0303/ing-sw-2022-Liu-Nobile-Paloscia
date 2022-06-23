package it.polimi.ingsw.Controller.ProfessorChecker;

import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.StudentColor;

import java.util.List;

public class SpecialProfessorChecker implements ProfessorChecker{
    @Override
    public boolean shouldSwapProfessor(GameModel game, Player noProfessor, Player withProfessor, StudentColor color) {
        return game.getTableNumber(withProfessor, color) <= game.getTableNumber(noProfessor, color);
    }

    @Override
    public Player getNewOwnerOfProfessor(GameModel game, List<Player> players, StudentColor color) {
        int max = 0;
        Player newOwner = null;

        for (Player player :
                players) {
            if (game.getTableNumber(player, color) > max) {
                max = game.getTableNumber(player, color);
                newOwner = player;
            } else if ((game.getTableNumber(player, color) == max) && !player.hasProfessor(color) && max != 0) {
                newOwner = player;
            }

        }
        return newOwner;
    }
}
