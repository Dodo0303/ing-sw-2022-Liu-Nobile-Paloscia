package it.polimi.ingsw;

import it.polimi.ingsw.Controller.MatchController;

import static it.polimi.ingsw.Utilities.*;

/** The Eriantys game. FOR NOW, ONLY 2-PERSON GAME IS IMPLEMENTED.
 */
public class App {

    /** Location of usage messages. */
    static final String USAGE = "src/Usage.txt";

    public static void main(String[] args) {
        int inputLength = args.length;
        if (inputLength == 0) {
            System.out.println("Please enter a command.");
        } else {
            MatchController game;
            switch (args[0]) {
                case "cli": {
                    //TODO
                }
                case "GUI": {
                    //TODO
                    break;
                }
                case "server": {

                }
                case "help": {
                    usage();
                    break;
                }

                default: {
                    game = new MatchController(2);
                    //System.exit(game.run());
                }
            }
        }

    }

    /** Print usage message. */
    private static void usage() {
        printUsage(USAGE);
    }

}
