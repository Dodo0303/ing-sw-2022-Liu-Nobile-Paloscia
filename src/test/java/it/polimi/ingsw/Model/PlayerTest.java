package it.polimi.ingsw.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    public void testAssistant(){
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

}