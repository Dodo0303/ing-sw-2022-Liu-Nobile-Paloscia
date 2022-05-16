package it.polimi.ingsw.Controller.ProfessorChecker;

import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.StudentColor;

public interface ProfessorChecker {
    boolean shouldSwapProfessor(GameModel game, Player noProfessor, Player withProfessor, StudentColor color);
}
