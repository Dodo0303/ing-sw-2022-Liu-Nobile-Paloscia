package it.polimi.ingsw;

import it.polimi.ingsw.Client.EriantysClient;
import it.polimi.ingsw.Controller.EriantysServer;

import static it.polimi.ingsw.Utilities.*;

/** The Eriantys game. FOR NOW, ONLY 2-PERSON GAME IS IMPLEMENTED.
 */
public class App {

    /** Location of usage messages. */
    static final String USAGE = "src/Usage.txt";

    public static void main(String[] args) {
        int inputLength = args.length;
        if (inputLength == 0) {
            usage();
            return;
        }
        launchServer(args);
        switch (args[0].toLowerCase()) {
            case "-gui": {
                //TODO
                break;
            }
            case "-cli": {
                launchCLI(args);
                break;
            }
            case "-help": {
                usage();
                break;
                }
            }
    }

    /** Print usage message. */
    private static void usage() {
        printUsage(USAGE);
    }

    private static void launchServer(String[] args) {
        EriantysServer.main(args);
    }

    private static void launchCLI(String[] args) {
        EriantysClient.main(args);
    }

    private void launchGUI(String[] args) {
        //TODO
    }

}
