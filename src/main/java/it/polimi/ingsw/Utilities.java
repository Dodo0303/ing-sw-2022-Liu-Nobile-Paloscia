package it.polimi.ingsw;

import java.io.*;

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

}
