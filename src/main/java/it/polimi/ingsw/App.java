package it.polimi.ingsw;

import it.polimi.ingsw.Client.EriantysClient;
import it.polimi.ingsw.Client.EriantysClientGUI;
import it.polimi.ingsw.Controller.EriantysServer;

import java.util.Scanner;

import static it.polimi.ingsw.Utilities.*;

/** The Eriantys game.
 */
public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter '0' to start server, enter '1' to start CLI, '2' to start GUI.");
        switch (scanner.nextLine()) {
            case "2": {
                launchGUI(args);
                break;
            }
            case "1": {
                launchCLI(args);
                break;
            }
            case "0": {
                launchServer(args);
                break;
                }
            }
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
