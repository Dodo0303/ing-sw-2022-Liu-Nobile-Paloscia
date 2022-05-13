package it.polimi.ingsw.Client.CLI;

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
import java.util.Scanner;

public class CLI {
    String DEFAULT_HOST = "localhost";
    int DEFAULT_PORT = 8001;
    GameModel game;
    boolean closed;
    private String nickname;
    private String host;
    private int port;
    private Scanner input = new Scanner(System.in);
    private ServerHandler serverHandler;
    private Phase currPhase;
    private List<Wizard> wizards;
    private Assistant[] assistants;
    private int numStudentToMove;
    private HashMap<StudentColor, Integer> entrance;
    private int numIslands;
    private HashMap<Integer, Island> islands;

    public void start() {
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
        } else if (serverHandler.closed) {
            throw new IllegalStateException("You are off-line!\n");
        } else {
            serverHandler.send(message);
        }
    }

    public void resetOutput() {
        serverHandler.send(new ResetOutputMessage());
    }

    protected void messageReceived(Object message) {
        if (message instanceof NickResponseMessage) {
            if (currPhase.equals(Phase.PickingNickname)) {
                ((NickResponseMessage) message).process(this.serverHandler);
            }
        } else if (message instanceof SendMatchesMessage) {
            if (currPhase.equals(Phase.ChoosingGameMode) || currPhase.equals(Phase.JoiningGame1)) {
                ((SendMatchesMessage) message).process(this.serverHandler);
                currPhase = Phase.JoiningGame1;
            }
        } else if (message instanceof ConfirmJoiningMessage) {
            if (currPhase.equals(Phase.CreatingGame) ||
                    currPhase.equals(Phase.JoiningGame1)) {
                ((ConfirmJoiningMessage) message).process(this.serverHandler);
            } else if (currPhase.equals(Phase.JoiningGame2)) {
                ((ConfirmJoiningMessage) message).process(this.serverHandler);
            }
        } else if (message instanceof SendAvailableWizardsMessage) {
            if (currPhase.equals(Phase.JoiningGame1)) {
                ((SendAvailableWizardsMessage) message).process(this.serverHandler);
            }
        } else if (message instanceof ChangeTurnMessage) {
            System.out.println(currPhase.toString() + " to " + ((ChangeTurnMessage) message).getPhase().toString());//TODO DELETE AFTER TESTS
            if (currPhase.equals(Phase.GameJoined) && ((ChangeTurnMessage) message).getPhase().equals(Phase.Planning)) {//TODO waiting for phase's definition in the server side
                ((ChangeTurnMessage) message).process(this.serverHandler);
            } else if (currPhase.equals(Phase.Planning) && ((ChangeTurnMessage) message).getPhase().equals(Phase.Action1)) {
                ((ChangeTurnMessage) message).process(this.serverHandler);
                //currPhase = Phase.Action1;
            } else if (currPhase.equals(Phase.Action3) && ((ChangeTurnMessage) message).getPhase().equals(Phase.Planning)) {
                ((ChangeTurnMessage) message).process(this.serverHandler);
                //currPhase = Phase.Planning;
            }
        } else if (message instanceof ConfirmMovementFromEntrance) {
            if (currPhase.equals(Phase.Action1)) {
                ((ConfirmMovementFromEntrance) message).process(this.serverHandler);
                currPhase = Phase.Action2;
            }
        } else if (message instanceof MoveProfessorMessage) {
            if (currPhase.equals(Phase.Action2)) {
                ((MoveProfessorMessage) message).process(this.serverHandler);
            }
        } else if (message instanceof DenyMovementMessage) {
                if (currPhase.equals(Phase.Action2)) {
                    ((DenyMovementMessage) message).process(this.serverHandler);
                }
            } else if (message instanceof ConfirmMovementMessage) {
            if (currPhase.equals(Phase.Action2)) {
                ((ConfirmMovementMessage) message).process(this.serverHandler);
                currPhase = Phase.Action3;
            }
        } else if (message instanceof ConfirmCloudMessage) {
            if (currPhase.equals(Phase.Action3)) {
                ((ConfirmCloudMessage) message).process(this.serverHandler);
            }
        } else if(message instanceof EndMessage) {
            if (currPhase.equals(Phase.Action3)) {
                ((EndMessage) message).process(this.serverHandler);
                currPhase = Phase.Ending;
            }
        } else if (message instanceof GameModelUpdateMessage) { //TODO
            if (currPhase.equals(Phase.GameJoined) ||
                    currPhase.equals(Phase.Planning) ||
                    currPhase.equals(Phase.Action1) ||
                    currPhase.equals(Phase.Action2) ||
                    currPhase.equals(Phase.Action3)) {
                ((GameModelUpdateMessage) message).process(this.serverHandler);
            }
        } else if (message instanceof CloudsUpdateMessage) { //TODO
            ((CloudsUpdateMessage) message).process(this.serverHandler);
        } else if (message instanceof UsedAssistantMessage) { //TODO
            ((UsedAssistantMessage) message).process(this.serverHandler);
        }
    }

    public void requireNickname() {
        System.out.print("Nickname?\n");
        String temp = input.nextLine();
        send(new SendNickMessage(temp));
    }

    private void buildConnection() {
        while(serverHandler == null) {
            System.out.print("Host?\n");
            //host = input.nextLine(); //TODO
            host = "localhost";
            System.out.print("Port?\n");
            port = Integer.parseInt(input.nextLine());
            try {
                serverHandler = new ServerHandler(host,port, this);
            } catch (IOException e) {
                System.out.print(e.getMessage() + "\n");
            }
        }
        currPhase = Phase.PickingNickname;
    }

    private void chooseGameMode() {
        while (!getCurrPhase().equals(Phase.ChoosingGameMode)) {
            currPhase = getCurrPhase();
        }
        int temp = 0;
        while(temp != 1 && temp != 2) {
            System.out.print("Press 1 for new game, press 2 for joining exiting game.\n");
            temp = Integer.parseInt(input.nextLine());
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
                numPlayer = Integer.parseInt(input.nextLine());
            }
            while(!mode.equals("true") && !mode.equals("false")) {
                System.out.print("turn on expert mode? Respond with true or false.\n");
                mode = input.nextLine();
            }
            while(wiz > 3 || wiz < 0) {
                System.out.print("Choose a wizard for yourself. Type in 1,2,3, or 4.\n");
                wiz = Integer.parseInt(input.nextLine()) - 1;
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
                match = Integer.parseInt(input.nextLine()) - 1;
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
            while(wiz < 0 || wiz > wizards.size() - 1) {
                System.out.print("Choose a wizard.\n");
                wiz = Integer.parseInt(input.nextLine()) - 1;
            }
            send(new SendChosenWizardMessage(Wizard.values()[wiz]));
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
                System.out.print(i + "." + game.getPlayers().get(getServerHandler().playerID).getAssistants().get(i - 1) + "\n");
            }
            while(assis < 0 || assis > 9) { //TODO did not use UsedAssistantMessage
                System.out.print("Choose assistant card.\n");
                assis = Integer.parseInt(input.nextLine()) - 1;
                if (game.getPlayers().get(getServerHandler().playerID).getAssistants().get(assis) == null) {
                    System.out.print("You cannot choose this assistant card.\n");
                    assis = -1;
                }
            }
            send(new SendAssistantMessage(game.getPlayers().get(getServerHandler().playerID).getAssistants().get(assis))); //TODO Check
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("Something went wrong, please try agian.\n");
        }
    }
//TODO 05/13
    private void moveStudentsFromEntrance() {
        while (!currPhase.equals(Phase.Action1)) {
            currPhase = getCurrPhase();
        }
        int num = -1, islandID = -1;
        StudentColor tempColor;
        String str = "";
        System.out.print("You have in your entrance:\n");
        for (StudentColor color:StudentColor.values()) {
            System.out.print(color + "student:" + entrance.get(color) + "\n");
        }
        for (int i = 0; i < numStudentToMove; i++) {
            while ((!Utilities.existInStudentColor(str)) ||
                    (Utilities.existInStudentColor(str) && entrance.get(StudentColor.valueOf(str)) <= 0)) {
                System.out.print("Which color of student would you like to move?(Choose from GREEN, BLUE, YELLOW, RED, and PINK)");
                str = input.nextLine();
            }
            tempColor = StudentColor.valueOf(str);
            while (num != 0 && num != 1) {
                System.out.print("Where would you like to move the student?(0 for dinning table, 1 for island)\n");
                num = Integer.parseInt(input.nextLine());
            }
            if (num == 1) {
                while(islandID < 0 || islandID >= numIslands) {
                    printIslands();
                    System.out.print("To which island would you like to move the student?\n");
                    islandID = Integer.parseInt(input.nextLine());
                }
            }
            entrance.remove(tempColor);
            send(new MoveStudentFromEntranceMessage(tempColor, num, islandID));
        }
    }

    public void moveMotherNature() {
        while (!currPhase.equals(Phase.Action2)) {
            currPhase = getCurrPhase();
        }
        int num = -1;
        while (num < 0 || num >= numIslands) {
            printIslands();
            System.out.print("Where would you like to move the mather nature?\n");
            num = Integer.parseInt(input.nextLine());
        }
        send(new MoveMotherNatureMessage(num));
    }

    public void chooseCloud() {
        while (!currPhase.equals(Phase.Action3)) {
            currPhase = getCurrPhase();
        }
        int num = -1;
        while (num < 0 || num >= islands.size()) {
            printClouds();
            System.out.print("Which cloud would you like to take students from?\n");
            num = Integer.parseInt(input.nextLine());
        }
        send(new ChooseCloudMessage(num));
    }

    private void printIslands() {
        for (int i = 0; i < numIslands; i++) {
            System.out.print("island " + i + " :\n");
            if (!islands.get(i).getTowerColor().equals(Color.VOID)) {
                System.out.print(islands.get(i).getNumTower() + "tower(s) of color " + islands.get(i).getTowerColor().toString() +"on the island\n");
            } else {
                System.out.print("No tower on the island.\n");
            }
            System.out.print("Students:\n");
            for (StudentColor color:StudentColor.values()) {
                System.out.print(color + "student:" + entrance.get(color) + "\n");
            }
            System.out.print("\n");
        }
    }

    private void printClouds() {
        for (int i = 0; i < game.getClouds().size(); i++) {
            System.out.print("cloud " + i + " :\n");
            System.out.print("Students:\n");
            for (StudentColor color:StudentColor.values()) {
                System.out.print(color + "student:" + entrance.get(color) + "\n");
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

    public void setAssistants(Assistant[] assistants) {
        this.assistants = assistants;
    }

    public void setNumStudentToMove(int num) {
        this.numStudentToMove = num;
    }

    public void setEntrance(HashMap<StudentColor, Integer> entrance) {
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
