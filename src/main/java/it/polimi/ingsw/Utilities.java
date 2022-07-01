package it.polimi.ingsw;

import it.polimi.ingsw.Client.Phase;
import it.polimi.ingsw.Model.StudentColor;

import java.io.*;
import java.util.Locale;

public class Utilities {

    public static Boolean existInStudentColor(String str) {
        str = str.toUpperCase(Locale.ROOT);
        return str.equals("GREEN") || str.equals("BLUE") || str.equals("YELLOW") || str.equals("RED") || str.equals("PINK");
    }

    public static StudentColor getColor(String str) {
        str = str.toUpperCase(Locale.ROOT);
        if (str.equals("GREEN")) {
            return StudentColor.GREEN;
        } else if (str.equals("BLUE")) {
            return StudentColor.BLUE;
        } else if ( str.equals("YELLOW") ) {
            return StudentColor.YELLOW;
        } else if (str.equals("RED")) {
            return StudentColor.RED;
        } else if (str.equals("PINK")) {
            return StudentColor.PINK;
        } else {
            return null;
        }
    }

    public static Phase getPhase(String str) {
        switch (str) {
            case "PlanningPhase" : {
                return Phase.Planning;
            }
            case "ActionPhase1" : {
                return Phase.Action1;
            }
            case "ActionPhase2" : {
                return Phase.Action2;
            }
            case "ActionPhase3" : {
                return Phase.Action3;
            }
            case "Ending" : {
                return Phase.Ending;
            }
        }
        return null;
    }

    public static boolean isNumeric(String str) {
        return str.matches("^[0-9]\\d*$");
    }


}
