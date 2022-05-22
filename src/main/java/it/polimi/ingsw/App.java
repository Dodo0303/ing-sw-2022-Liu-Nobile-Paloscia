package it.polimi.ingsw;

import it.polimi.ingsw.Client.EriantysClient;
import it.polimi.ingsw.Client.EriantysClientGUI;
import it.polimi.ingsw.Controller.EriantysServer;

import static it.polimi.ingsw.Utilities.*;

/** The Eriantys game.
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
        switch (args[0].toLowerCase()) {
            case "-gui": {
                launchGUI(args);
                break;
            }
            case "-cli": {
                launchCLI(args);
                break;
            }
            case "-server": {
                launchServer(args);
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

    private static void launchGUI(String[] args) {
        EriantysClientGUI.main(args);
    }

}
