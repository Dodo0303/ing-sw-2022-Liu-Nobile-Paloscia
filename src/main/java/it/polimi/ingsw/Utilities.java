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
        return str.equals("GREEN") || str.equals("BLUE") || str.equals("YELLOW") || str.equals("RED") || str.equals("PINK");
    }


}
