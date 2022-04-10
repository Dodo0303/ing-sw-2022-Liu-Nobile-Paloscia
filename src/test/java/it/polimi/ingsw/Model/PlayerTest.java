package it.polimi.ingsw.Model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    public void createPlayer_CheckAssistants(){
        Player player = new Player(Color.BLACK, Wizard.WIZARD1, 8);
        Assistant[] ass = player.getAssistants();
        for (int i = 0; i < 10; i++) {
            assertEquals(i+1, ass[i].getValue());
            //Using the switch so we don't use any algorithm that could be wrong
            switch (i){
                case 0:
                case 1:
                    assertEquals(1, ass[i].getMaxSteps());
                    break;
                case 2:
                case 3:
                    assertEquals(2, ass[i].getMaxSteps());
                    break;
                case 4:
                case 5:
                    assertEquals(3, ass[i].getMaxSteps());
                    break;
                case 6:
                case 7:
                    assertEquals(4, ass[i].getMaxSteps());
                    break;
                case 8:
                case 9:
                    assertEquals(5, ass[i].getMaxSteps());
                    break;
            }
        }
    }

    @Test
    public void createPlayer_CheckEntranceStudents() {
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 8);
        HashMap<StudentColor, Integer> students = p.getEntranceStudents();
        for (StudentColor color :
                StudentColor.values()) {
            assertEquals(0, students.get(color));
        }

    }
    
    @Test
    public void createPlayer_CheckProfessors() {
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 8);
        List<StudentColor> professors = p.getProfessors();
        assertTrue(professors.isEmpty());
    }

    @Test
    public void createPlayer_CheckDiningTables(){
        Player p = new Player(Color.BLACK, Wizard.WIZARD1, 8);
        Map<StudentColor, DiningTable> tables = p.getDiningTables();
        for (StudentColor color :
                StudentColor.values()) {
            DiningTable table = tables.get(color);
            assertEquals(0, table.getNumOfStudents());
            assertEquals(color, table.getColor());
            assertFalse(table.claimCoin());
        }
    }



}