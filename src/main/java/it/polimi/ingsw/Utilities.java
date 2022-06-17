package it.polimi.ingsw;

import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
import it.polimi.ingsw.Model.StudentColor;

import java.io.*;
import java.util.Locale;

public class Utilities {
    /** Print the contents of the resource named NAME on System.out.
     *  */
    static void printUsage(String name) {
        try {
            BufferedReader str =
                    new BufferedReader(new FileReader(name));
            for (String s = str.readLine(); s != null; s = str.readLine())  {
                System.out.println(s);
            }
            str.close();
            System.out.flush();
        } catch (IOException excp) {
            System.out.printf("No help found.");
            System.out.flush();
        }
    }

    /** Return an object of type T read from InputStream, casting it to EXPECTEDCLASS.
     *  Throws IllegalArgumentException in case of problems. */
    static <T extends Serializable> T readObject(Class<T> expectedClass, ObjectInputStream in) {
        try {
            T result = expectedClass.cast(in.readObject());
            in.close();
            return result;
        } catch (IOException | ClassCastException
                | ClassNotFoundException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

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

    public static Phase_GUI getPhaseGUI(Phase phase) {
        if (phase.equals(Phase.Planning)) {
            return Phase_GUI.Planning;
        } else if (phase.equals(Phase.Action1)) {
            return Phase_GUI.Action1;
        } else if (phase.equals(Phase.Action2)) {
            return Phase_GUI.Action2;
        } else if (phase.equals(Phase.Action3)) {
            return Phase_GUI.Action3;
        } else if (phase.equals(Phase.Ending)) {
            return Phase_GUI.Ending;
        }
        return null;
    }
    public static boolean isNumeric(String str) {
        return str.matches("^[0-9]\\d*$");
    }


}
