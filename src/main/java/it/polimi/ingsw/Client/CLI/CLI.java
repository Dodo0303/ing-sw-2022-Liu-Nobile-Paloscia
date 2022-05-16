package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Exceptions.EmptyCloudException;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Network.Messages.toClient.ActionPhase.*;
import it.polimi.ingsw.Network.Messages.toClient.EndMessage;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.*;
import it.polimi.ingsw.Network.Messages.toClient.PlanningPhase.CloudsUpdateMessage;
import it.polimi.ingsw.Network.Messages.toClient.PlanningPhase.UsedAssistantMessage;
import it.polimi.ingsw.Network.Messages.toClient.Uncategorized.ResetOutputMessage;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.ChooseCloudMessage;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.MoveMotherNatureMessage;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.MoveStudentFromEntranceMessage;
import it.polimi.ingsw.Network.Messages.toServer.JoiningPhase.*;
import it.polimi.ingsw.Network.Messages.toServer.PlanningPhase.SendAssistantMessage;
import it.polimi.ingsw.Utilities;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class CLI {
    GameModel game;
    boolean closed;
    private String nickname;
    private String host;
    private int port;
    private Scanner input = new Scanner(System.in);
    private ServerHandler serverHandler;
    private Phase currPhase;
    private List<Wizard> wizards;
    private List<StudentColor> entrance;
    private int numIslands;
    private HashMap<Integer, Island> islands;
    private int ap1Moves;

    public void start() {
        ap1Moves = 0;
        closed = false;
        currPhase = Phase.BuildingConnection;
        buildConnection();
        Thread serverHandlerThread  = new Thread(this.serverHandler);
        serverHandlerThread.start();
        requireNickname();
        chooseGameMode();
    }

    public void send(Object message) {
        if (message == null) {
            throw new IllegalArgumentException("Null cannot be sent as a message.\n");
        } else {
            serverHandler.send(message);
        }
    }

    protected void messageReceived(Object message) throws InterruptedException, EmptyCloudException {
        if (message instanceof NickResponseMessage) {
            if (currPhase.equals(Phase.PickingNickname)) {
                ((NickResponseMessage) message).process(this.serverHandler);
            }
        } else if (message instanceof SendMatchesMessage) {
            if (currPhase.equals(Phase.ChoosingGameMode) || currPhase.equals(Phase.JoiningGame1)) {
                ((SendMatchesMessage) message).process(this.serverHandler);
            }
        } else if (message instanceof ConfirmJoiningMessage) {
            if (currPhase.equals(Phase.CreatingGame) ||
                    currPhase.equals(Phase.JoiningGame1) ||
                    currPhase.equals(Phase.JoiningGame2)) {
                ((ConfirmJoiningMessage) message).process(this.serverHandler);
            }
        } else if (message instanceof SendAvailableWizardsMessage) {
            if (currPhase.equals(Phase.JoiningGame1) ||
                    currPhase.equals(Phase.JoiningGame2)) {
                ((SendAvailableWizardsMessage) message).process(this.serverHandler);
            }
        } else if (message instanceof ChangeTurnMessage) {
            System.out.println(currPhase.toString() + " to " + ((ChangeTurnMessage) message).getPhase().toString());//TODO DELETE AFTER TESTS
            if (currPhase.equals(Phase.GameJoined) && ((ChangeTurnMessage) message).getPhase().equals(Phase.Planning)) {
                ((ChangeTurnMessage) message).process(this.serverHandler);
            } else if (currPhase.equals(Phase.Planning) && ((ChangeTurnMessage) message).getPhase().equals(Phase.Action1)) {
                ((ChangeTurnMessage) message).process(this.serverHandler);
            } else if (currPhase.equals(Phase.Action3) && ((ChangeTurnMessage) message).getPhase().equals(Phase.Planning)) {
                ((ChangeTurnMessage) message).process(this.serverHandler);
            }
        } else if (message instanceof ConfirmMovementFromEntranceMessage) {
            if (currPhase.equals(Phase.Action1)) {
                ((ConfirmMovementFromEntranceMessage) message).process(this.serverHandler);
                ap1Moves++;
                if (ap1Moves == ((getGame().getPlayers().size() == 3)? 4 : 3)) {
                    currPhase = Phase.Action2;
                    ap1Moves = 0;
                    moveMotherNature();
                } else {
                    moveStudentsFromEntrance();
                }
            } else {
                ((ConfirmMovementFromEntranceMessage) message).process(this.serverHandler);
            }
        } else if (message instanceof MoveProfessorMessage) {
            if (currPhase.equals(Phase.Action2)) {
                ((MoveProfessorMessage) message).process(this.serverHandler);
            }
        } else if (message instanceof DenyMovementMessage) {
            ((DenyMovementMessage) message).process(this.serverHandler);
        } else if (message instanceof ConfirmMovementMessage) {
            ((ConfirmMovementMessage) message).process(this.serverHandler);
        } else if (message instanceof ConfirmCloudMessage) {
            ((ConfirmCloudMessage) message).process(this.serverHandler);
        } else if(message instanceof EndMessage) {
            if (currPhase.equals(Phase.Action3)) {
                ((EndMessage) message).process(this.serverHandler);
                currPhase = Phase.Ending;
            }
        } else if (message instanceof GameModelUpdateMessage) {
            ((GameModelUpdateMessage) message).process(this.serverHandler);
        } else if (message instanceof CloudsUpdateMessage) {
            ((CloudsUpdateMessage) message).process(this.serverHandler);
        } else if (message instanceof UsedAssistantMessage) {
            ((UsedAssistantMessage) message).process(this.serverHandler);
        }
    }

    public void requireNickname() {
        System.out.print("Nickname?\n");
        String temp = input.nextLine();
        send(new SendNickMessage(temp));
    }

    private void buildConnection() {
        try {
            while(serverHandler == null) {
                System.out.print("Host?\n");
                //host = input.nextLine(); //TODO uncomment after tests
                host = "localhost";
                String in = "";
                while (!Utilities.isNumeric(in)) {
                    System.out.print("Port?\n");
                    in = input.nextLine();
                    port = Integer.parseInt(in);
                }
                try {
                    serverHandler = new ServerHandler(host,port, this);
                } catch (IOException e) {
                    System.out.print(e.getMessage() + "\n");
                }
            }
            currPhase = Phase.PickingNickname;
        } catch (Exception e) {
            System.out.println("Something went wrong, please try again.\n");
            buildConnection();
        }
    }

    public void chooseGameMode() {
        while (!getCurrPhase().equals(Phase.ChoosingGameMode)) {
            currPhase = getCurrPhase();
        }
        int temp = 0;
        while(temp != 1 && temp != 2) {
            System.out.print("Press 1 for new game, press 2 for joining exiting game.\n");
            String in = input.nextLine();
            if (Utilities.isNumeric(in)) {
                temp = Integer.parseInt(in);
            }
        }
        if (temp == 1) {
            send(new CreateMatchMessage(true));
            currPhase = Phase.CreatingGame;
            newgame();
        } else {
            send(new CreateMatchMessage(false));
            joinGame();
            chooseWizard();
        }
    }

    private void newgame() {
        try {
            while (!currPhase.equals(Phase.CreatingGame)) {
                currPhase = getCurrPhase();
            }
            int numPlayer = 0, wiz = -1;
            String mode = "";
            while(numPlayer > 4 || numPlayer < 2) {
                System.out.print("choose between 2,3, or 4 for total number of players.\n");
                String in = input.nextLine();
                if (Utilities.isNumeric(in)) {
                    numPlayer = Integer.parseInt(in);
                }
            }
            while(!mode.equals("true") && !mode.equals("false")) {
                System.out.print("turn on expert mode? Respond with true or false.\n");
                mode = input.nextLine().toLowerCase();
            }
            while(wiz > 3 || wiz < 0) {
                System.out.print("Choose a wizard for yourself. Type in 1,2,3, or 4.\n");
                String in = input.nextLine();
                if (Utilities.isNumeric(in)) {
                    wiz = Integer.parseInt(in) - 1;
                }
            }
            send(new SendStartInfoMessage(numPlayer, Boolean.parseBoolean(mode), Wizard.values()[wiz]));
        } catch (Exception e) {
            System.out.print("Something went wrong, please try agian.\n");
            newgame();
        }
    }

    public void joinGame() {
        try {
            while (!currPhase.equals(Phase.JoiningGame1)) {
                currPhase = getCurrPhase();
            }
            int match = -1;
            while (match < 0) {
                System.out.print("Choose a match.\n");
                String in = input.nextLine();
                if (Utilities.isNumeric(in)) {
                    match = Integer.parseInt(in) - 1;
                }
            }
            send(new MatchChosenMessage(match));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("Something went wrong, please try agian.\n");
            joinGame();
        }
    }

    public void chooseWizard() {
        try {
            int wiz = -1;
            while (!currPhase.equals(Phase.JoiningGame2)) {
                currPhase = getCurrPhase();
            }
            while(wiz < 0 || wiz >= wizards.size()) {
                System.out.print("Choose a wizard.\n");
                String in = input.nextLine();
                if (Utilities.isNumeric(in)) {
                    wiz = Integer.parseInt(in) - 1;
                }
            }
            setPhase(Phase.JoiningGame2);
            send(new SendChosenWizardMessage(wizards.get(wiz)));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("Something went wrong, please try agian.\n");
            chooseWizard();
        }
    }

    public void playAssistant() {
        try {
            while (!currPhase.equals(Phase.Planning)) {
                currPhase = getCurrPhase();
            }
            int assis = -1;
            for (int i = 1; i <= 10; i++) {
                Assistant assistant = game.getPlayers().get(game.getPlayerIndexFromNickname(nickname)).getAssistants().get(i - 1);
                if (assistant != null) {
                    System.out.println(i + ". MaxStep: " +  assistant.getMaxSteps() + ", Value: "+ assistant.getValue());
                }
            }
            while(assis < 0 || assis > 9) {
                System.out.print("Choose assistant card.\n");
                String in = input.nextLine();
                if (Utilities.isNumeric(in)) {
                    assis = Integer.parseInt(in) - 1;
                }
                if (assis != -1 && game.getPlayers().get(game.getPlayerIndexFromNickname(nickname)).getAssistants().get(assis) == null) {
                    System.out.print("You cannot choose this assistant card.\n");
                    assis = -1;
                }
            }
            send(new SendAssistantMessage(game.getPlayers().get(game.getPlayerIndexFromNickname(nickname)).getAssistants().get(assis)));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("Something went wrong, please try agian.\n");
            playAssistant();
        }
    }

    public void moveStudentsFromEntrance() {
        while (!currPhase.equals(Phase.Action1)) {
            currPhase = getCurrPhase();
        }
        int num = -1, islandID = -1, i = 0, index = -1;
        entrance = game.getPlayers().get(game.getPlayerIndexFromNickname(nickname)).getEntranceStudents();
        numIslands = game.getNumIslands();
        StudentColor tempColor;
        System.out.print("You have in your entrance:\n");
        for (StudentColor color: entrance) {
            System.out.print(i + ")" + color + " ");
            i++;
        }
        System.out.println("");

        while (index < 0 || index > entrance.size() - 1) {
            System.out.print("Which student would you like to move?(Write the index)");
            String in = input.nextLine();
            if (Utilities.isNumeric(in))
                index = Integer.parseInt(in);
        }
        while (num != 0 && num != 1) {
            System.out.print("Where would you like to move the student?(0 for dinning table, 1 for island)\n");
            String in = input.nextLine();
            if (Utilities.isNumeric(in)) {
                num = Integer.parseInt(in);
            }
        }
        if (num == 1) {
            while(islandID < 0 || islandID >= numIslands) {
                printIslands();
                System.out.print("To which island would you like to move the student?\n");
                String in = input.nextLine();
                if (Utilities.isNumeric(in)) {
                    islandID = Integer.parseInt(in);
                }
            }
        }
        System.out.println(index + num + islandID);//TODO DELETE AFTER TEST
        send(new MoveStudentFromEntranceMessage(index, num, islandID));
    }
    public void moveMotherNature() {
        while (!currPhase.equals(Phase.Action2)) {
            currPhase = getCurrPhase();
        }
        int num = -1;
        while (num < 0 || num >= numIslands) {
            printIslands();
            System.out.print("Where would you like to move the mather nature?\n");
            String in = input.nextLine();
            if (Utilities.isNumeric(in)) {
                num = Integer.parseInt(in);
            }
        }
        send(new MoveMotherNatureMessage(num));
    }
    public void chooseCloud() {
        while (!currPhase.equals(Phase.Action3)) {
            currPhase = getCurrPhase();
        }
        int num = -1;
        while (num < 0 || num >= islands.size() - 1) {
            printClouds();
            System.out.print("Which cloud would you like to take students from?\n");
            String in = input.nextLine();
            if (Utilities.isNumeric(in)) {
                num = Integer.parseInt(in);
            }
        }
        send(new ChooseCloudMessage(num));
    }

    private void printIslands() {
        numIslands = game.getNumIslands();
        islands = game.getIslands();
        for (int i = 0; i < numIslands; i++) {
            System.out.print("island " + i + " :\n");
            if (!islands.get(i).getTowerColor().equals(Color.VOID)) {
                System.out.print(islands.get(i).getNumTower() + " tower(s) of color " + islands.get(i).getTowerColor().toString() +" on the island\n");
            } else {
                System.out.print("No tower on the island.\n");
            }
            if (game.getMotherNatureIndex() == i) {
                System.out.print("The motherNature is here\n");
            }
            System.out.print("Students:\n");
            for (StudentColor color:StudentColor.values()) {
                System.out.print(color + "student:" + islands.get(i).getStudents().get(color) + "\n");
            }
            System.out.print("\n");
        }
    }

    private void printClouds() {
        for (int i = 0; i < game.getClouds().size(); i++) {
            System.out.print("cloud " + i + " :\n");
            System.out.print("Students:\n");
            for (StudentColor color: game.getClouds().get(i).getStudents()) {
                System.out.println(color + "  ");
            }
        }
    }

    public String getNickname() {
        return nickname;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Scanner getInput() {
        return input;
    }

    public Phase getCurrPhase() {
        return this.currPhase;
    }

    public ServerHandler getServerHandler() {
        return serverHandler;
    }

    public void setNickName(String str) {
        this.nickname = str;
    }

    public void setPhase(Phase phase) {
        this.currPhase = phase;
    }

    public void setWizards(List<Wizard> wizards) {
        this.wizards = wizards;
    }

    public void setEntrance(List<StudentColor> entrance) {
        this.entrance = entrance;
    }

    public void setNumIslands(int numIslands) {
        this.numIslands = numIslands;
    }

    public void setIslands(HashMap<Integer, Island> islands) {
        this.islands = islands;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public void setGame(GameModel game) {
        this.game = game;
    }

    public GameModel getGame() {
        return game;
    }
}
