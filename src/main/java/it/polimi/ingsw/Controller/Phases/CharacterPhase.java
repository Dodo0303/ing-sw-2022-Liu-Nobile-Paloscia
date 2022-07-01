package it.polimi.ingsw.Controller.Phases;

import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Controller.InfluenceCalculators.AdditionalPointsInfluenceCalculator;
import it.polimi.ingsw.Controller.InfluenceCalculators.ExcludeColorInfluenceCalculator;
import it.polimi.ingsw.Controller.InfluenceCalculators.NoTowerInfluenceCalculator;
import it.polimi.ingsw.Controller.MatchController;
import it.polimi.ingsw.Controller.ProfessorChecker.SpecialProfessorChecker;
import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Network.Messages.toClient.CharacterPhase.*;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.UseCharacterMessage;
import it.polimi.ingsw.Network.Messages.toServer.CharacterPhase.*;
import it.polimi.ingsw.Network.Messages.toServer.MessageToServer;

/**
 * This phase is called when a player has requested to use a character.
 */
public class CharacterPhase extends Phase{
    private Phase previousPhase;
    private int expectedCharacterID;

    public CharacterPhase(MatchController match, Phase previousPhase, int expectedCharacterID) {
        super(match);
        this.previousPhase = previousPhase;
        this.expectedCharacterID = expectedCharacterID;
    }

    @Override
    public void process(MessageToServer msg, ClientHandler ch) {
        if (msg instanceof MoveNoEntryMessage){
            if (expectedCharacterID != 5) {
                match.denyMovement(ch);
            } else {
                try {
                    match.useCharacter(ch.getNickname(), expectedCharacterID);
                    match.addNoEntryToIsland(((MoveNoEntryMessage) msg).getIslandID());
                    match.broadcastMessage(new NoEntryMovedMessage(((MoveNoEntryMessage) msg).getIslandID(), match.getGame().getCharacterById(expectedCharacterID), true));
                } catch (NotEnoughNoEntriesException | WrongEffectException e) {
                    match.denyMovement(ch);
                }
            }
        } else if (msg instanceof MoveStudentFromCharacterMessage){
            if (expectedCharacterID != 1) {
                match.denyMovement(ch);
            } else {
                try {
                    StudentColor colorChosen = match.useCharacter(ch.getNickname(), expectedCharacterID, ((MoveStudentFromCharacterMessage) msg).getStudentIndex(), true);
                    match.moveStudentToIsland(((MoveStudentFromCharacterMessage) msg).getIslandID(), colorChosen);
                    match.broadcastMessage(new StudentMovedFromCharacterMessage(match.getGame().getCharacterById(expectedCharacterID), match.getGame().getIslands()));
                } catch (EmptyBagException | WrongEffectException e) {
                    match.denyMovement(ch);
                }
            }

        } else if (msg instanceof ChooseIslandMessage){
            if (expectedCharacterID != 3) {
                match.denyMovement(ch);
            } else {
                try {
                    match.useCharacter(ch.getNickname(), expectedCharacterID);
                    match.conquerAndJoinIslands(((ChooseIslandMessage) msg).getIslandID());
                    match.broadcastMessage(new IslandChosenMessage(match.getGame().getIslands(), match.getGame().getCharacterById(expectedCharacterID), match.getGame().getPlayers()));

                } catch (WrongEffectException | NotEnoughNoEntriesException e) {
                    match.denyMovement(ch);
                }
            }
        } else if (msg instanceof ChooseStudentColorMessage){
            if (expectedCharacterID != 9 && expectedCharacterID != 12) {
                match.denyMovement(ch);
            } else if (expectedCharacterID == 9){
                try {
                    match.useCharacter(ch.getNickname(), expectedCharacterID);
                    match.setInfluenceCalculator(new ExcludeColorInfluenceCalculator(((ChooseStudentColorMessage) msg).getStudentColor()));
                    match.broadcastMessage(new StudentColorChosenMessage( false, ((ChooseStudentColorMessage) msg).getStudentColor(), null));

                } catch (WrongEffectException | NotEnoughNoEntriesException e) {
                    match.denyMovement(ch);
                }

            } else {
                // expectedCharacterID == 12
                try {
                    match.useCharacter(ch.getNickname(), expectedCharacterID);
                    match.removeThreeStudentsFromTables(((ChooseStudentColorMessage) msg).getStudentColor());
                    match.broadcastMessage(new StudentColorChosenMessage(true, ((ChooseStudentColorMessage) msg).getStudentColor(), match.getGame().getPlayers()));
                } catch (WrongEffectException | NotEnoughNoEntriesException e) {
                    match.denyMovement(ch);
                    e.printStackTrace();
                }

            }

        } else if (msg instanceof SwapStudentsTableEntranceMessage) {
            if (expectedCharacterID != 10) {
                match.denyMovement(ch);
            } else {
                int numOfSwap = ((SwapStudentsTableEntranceMessage) msg).getEntrancePosition().length;
                if (numOfSwap > 2) {
                    match.denyMovement(ch);
                } else {
                    try {
                        match.useCharacter(ch.getNickname(), expectedCharacterID);
                        for (int i = 0; i < numOfSwap; i++) {
                            match.swapStudentsEntranceAndTable(ch.getNickname(), ((SwapStudentsTableEntranceMessage) msg).getEntrancePosition()[i], ((SwapStudentsTableEntranceMessage) msg).getTable()[i]);
                        }
                        match.broadcastMessage(new EntranceTableSwappedMessage(match.getCurrentPlayerID(), match.getGame().getPlayerByNickname(match.getCurrentPlayerID())));
                    } catch (EmptyTableException | FullTableException | WrongEffectException | NotEnoughNoEntriesException e) {
                        e.printStackTrace();
                        match.denyMovement(ch);
                    }
                }
            }

        } else if (msg instanceof MoveStudentsToTableMessage) {
            if (expectedCharacterID != 11) {
                match.denyMovement(ch);
            } else {
                try {
                    StudentColor studentChosen = match.useCharacter(ch.getNickname(), expectedCharacterID, ((MoveStudentsToTableMessage) msg).getIndex(), true);
                    match.addStudentToTable(ch.getNickname(), studentChosen);
                    match.broadcastMessage(new StudentMovedToTableMessage(match.getCurrentPlayerID(), studentChosen, match.getGame().getCharacterById(expectedCharacterID)));
                } catch (EmptyBagException | WrongEffectException | FullTableException e) {
                    match.denyMovement(ch);
                    e.printStackTrace();
                }
            }
        } else if (msg instanceof SwapStudentsCharacterEntranceMessage) {
            if (expectedCharacterID != 7) {
                match.denyMovement(ch);
            } else {
                int numOfSwap = ((SwapStudentsCharacterEntranceMessage) msg).getCharacterIndexes().length;
                if (numOfSwap > 3) {
                    match.denyMovement(ch);
                } else {
                    try {
                        StudentColor colorPicked, entranceStudent;
                        for (int i = 0; i < numOfSwap; i++) {
                            entranceStudent = match.removeStudentFromEntrance(ch.getNickname(), ((SwapStudentsCharacterEntranceMessage) msg).getEntranceIndexes()[i]);
                            if (i == 0) {

                                colorPicked = match.useCharacter(ch.getNickname(), expectedCharacterID, ((SwapStudentsCharacterEntranceMessage) msg).getCharacterIndexes()[i], true, entranceStudent);
                            } else {
                                colorPicked = match.useCharacter(ch.getNickname(), expectedCharacterID, ((SwapStudentsCharacterEntranceMessage) msg).getCharacterIndexes()[i], false, entranceStudent);
                            }
                            match.addStudentToEntrance(ch.getNickname(), colorPicked);

                        }

                        match.broadcastMessage(new CharacterEntranceSwappedMessage(match.getCurrentPlayerID(), match.getGame().getEntranceOfPlayer(match.getGame().getPlayerByNickname(match.getCurrentPlayerID())), match.getGame().getCharacterById(expectedCharacterID)));
                    } catch (WrongEffectException e) {
                        e.printStackTrace();
                        match.denyMovement(ch);
                    }
                }
            }
        } else if (msg instanceof UseCharacterMessage){
            //The character chosen didn't need any second message to apply its effect
            if (expectedCharacterID == 2) {
                try {
                    match.useCharacter(ch.getNickname(), expectedCharacterID);
                    match.setProfessorChecker(new SpecialProfessorChecker());
                    match.broadcastMessage(new CharacterUsedMessage(match.getCurrentPlayerID(), expectedCharacterID, match.getGame().getPlayerByNickname(ch.getNickname()).getCoins()));

                } catch (WrongEffectException | NotEnoughNoEntriesException e) {
                    e.printStackTrace();
                    match.denyMovement(ch);
                }
            } else if (expectedCharacterID == 4) {
                try {
                    match.useCharacter(ch.getNickname(), expectedCharacterID);
                    match.setAdditionalMoves();
                    match.broadcastMessage(new CharacterUsedMessage(match.getCurrentPlayerID(), expectedCharacterID, match.getGame().getPlayerByNickname(ch.getNickname()).getCoins()));

                } catch (WrongEffectException | NotEnoughNoEntriesException e) {
                    e.printStackTrace();
                    match.denyMovement(ch);
                }
            } else if (expectedCharacterID == 6) {
                try {
                    match.useCharacter(ch.getNickname(), expectedCharacterID);
                    match.setInfluenceCalculator(new NoTowerInfluenceCalculator());
                    match.broadcastMessage(new CharacterUsedMessage(match.getCurrentPlayerID(), expectedCharacterID, match.getGame().getPlayerByNickname(ch.getNickname()).getCoins()));

                } catch (WrongEffectException | NotEnoughNoEntriesException e) {
                    e.printStackTrace();
                    match.denyMovement(ch);
                }
            } else if (expectedCharacterID == 8) {
                try {
                    match.useCharacter(ch.getNickname(), expectedCharacterID);
                    match.setInfluenceCalculator(new AdditionalPointsInfluenceCalculator(ch.getNickname()));
                    match.broadcastMessage(new CharacterUsedMessage(match.getCurrentPlayerID(), expectedCharacterID, match.getGame().getPlayerByNickname(ch.getNickname()).getCoins()));

                } catch (WrongEffectException | NotEnoughNoEntriesException e) {
                    e.printStackTrace();
                    match.denyMovement(ch);
                }
            }

            return;
        } else {
            match.denyMovement(ch);
            System.out.println("Character message expected, received: " + msg.getClass());
        }
        nextPhase();
    }

    @Override
    public void nextPhase() {
        //Go back to the previous phase
        match.setGamePhase(previousPhase);
    }
}
