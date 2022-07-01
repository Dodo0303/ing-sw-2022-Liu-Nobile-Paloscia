package it.polimi.ingsw.Controller.ProfessorChecker;

import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.StudentColor;

import java.util.List;

/**
 * Interface that manages the assignment of a professor to a player
 */
public interface ProfessorChecker {
    /**
     * Given two players, checks if they should swap the professor.
     * @param game GameModel that is hosting the two players.
     * @param noProfessor Player that doesn't have the professor.
     * @param withProfessor Player that has the professor.
     * @param color color of the professor.
     * @return true if the professor should be swapped. False if not.
     */
    boolean shouldSwapProfessor(GameModel game, Player noProfessor, Player withProfessor, StudentColor color);

    /**
     * Checks among all the players who should have the professor
     * @param game GameModel that is hosting the players
     * @param players List of alla the players in the game
     * @param color color to be checked
     * @return the Player that should own the professor.
     */
    Player getNewOwnerOfProfessor(GameModel game, List<Player> players,StudentColor color);
}
