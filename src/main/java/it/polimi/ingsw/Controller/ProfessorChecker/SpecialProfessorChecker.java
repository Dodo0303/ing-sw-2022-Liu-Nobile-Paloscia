package it.polimi.ingsw.Controller.ProfessorChecker;

import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.StudentColor;

public class SpecialProfessorChecker implements ProfessorChecker{
    @Override
    public boolean shouldSwapProfessor(GameModel game, Player noProfessor, Player withProfessor, StudentColor color) {
        return game.getTableNumber(withProfessor, color) <= game.getTableNumber(noProfessor, color);
    }
}
