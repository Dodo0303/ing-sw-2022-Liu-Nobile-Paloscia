package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Network.Messages.toClient.ActionPhase.*;
import it.polimi.ingsw.Network.Messages.toClient.EndMessage;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.ConfirmJoiningMessage;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.NickResponseMessage;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.SendAvailableWizardMessage;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.SendMatchesMessage;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;
import it.polimi.ingsw.Network.Messages.toClient.PlanningPhase.ChangeTurnMessage;
import it.polimi.ingsw.Network.Messages.toClient.Uncategorized.DisconnectMessage;
import it.polimi.ingsw.Network.Messages.toClient.Uncategorized.ResetOutputMessage;
import it.polimi.ingsw.Network.Messages.toClient.Uncategorized.StatusMessage;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.ChooseCloudMessage;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.MoveMotherNatureMessage;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.MoveStudentFromEntranceMessage;
import it.polimi.ingsw.Network.Messages.toServer.JoiningPhase.*;
import it.polimi.ingsw.Network.Messages.toServer.PlanningPhase.SendAssistantMessage;
import it.polimi.ingsw.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class CLI {
    String DEFAULT_HOST = "localhost";
    int DEFAULT_PORT = 8001;
    boolean closed;
    private String nickname;
    private String host;
    private int port;
    private Scanner input = new Scanner(System.in);
    private ServerHandler serverHandler;
    private Phase currPhase;
    private List<Integer> matchesID;
    private List<List<String>> players;
    private List<Wizard> wizards;
    private Assistant[] assistants;
    private int numStudentToMove;
    private HashMap<StudentColor, Integer> entrance;
    private int numIslands;
    private HashMap<Integer, Island> islands;
    private ArrayList<Cloud> clouds;

    public void start() {
        closed = false;
        currPhase = Phase.BuildingConnection;
        buildConnection();
        Thread serverHandlerThread  = new Thread(this.serverHandler);
        serverHandlerThread.start();
        requireNickname();
        chooseGameMode();
        while (!closed) {
            playAssistant();//planning
            moveStudentsFromEntrance();//AP1
            moveMotherNature();//AP2
            chooseCloud();//AP3
        }
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
                currPhase = Phase.ChoosingGameMode;
            } else {
                serverHandler.getOutgoingMessages().add(message);
            }
        } else if (message instanceof SendMatchesMessage) {
            if (currPhase.equals(Phase.ChoosingGameMode)) {
                ((SendMatchesMessage) message).process(this.serverHandler);
                currPhase = Phase.JoiningGame1;
            } else {
                serverHandler.getOutgoingMessages().add(message);
            }
        } else if (message instanceof ConfirmJoiningMessage) {
            if (currPhase.equals(Phase.CreatingGame)) {
                ((ConfirmJoiningMessage) message).process(this.serverHandler);
                currPhase = Phase.GameJoined;
            } else if (currPhase.equals(Phase.JoiningGame2)) {
                ((ConfirmJoiningMessage) message).process(this.serverHandler);
                currPhase = Phase.GameJoined;
            } else {
                serverHandler.getOutgoingMessages().add(message);
            }
        } else if (message instanceof SendAvailableWizardMessage) {
            if (currPhase.equals(Phase.JoiningGame1)) {
                ((SendAvailableWizardMessage) message).process(this.serverHandler);
                currPhase = Phase.JoiningGame2;
            } else {
                serverHandler.getOutgoingMessages().add(message);
            }
        } else if (message instanceof ChangeTurnMessage) {
            if (currPhase.equals(Phase.GameJoined) && ((ChangeTurnMessage) message).getPhase().equals(Phase.Planning)) {//TODO waiting for phase's definition in the server side
                ((ChangeTurnMessage) message).process(this.serverHandler);
                currPhase = Phase.Planning;
            } else if (currPhase.equals(Phase.Planning) && ((ChangeTurnMessage) message).getPhase().equals(Phase.Action1)) {
                ((ChangeTurnMessage) message).process(this.serverHandler);
                currPhase = Phase.Action1;
            } else if (currPhase.equals(Phase.Action3) && ((ChangeTurnMessage) message).getPhase().equals(Phase.Planning)) {
                ((ChangeTurnMessage) message).process(this.serverHandler);
                currPhase = Phase.Planning;
            } else {
                serverHandler.getOutgoingMessages().add(message);
            }
        } else if (message instanceof ConfirmMovementFromEntrance) {
            if (currPhase.equals(Phase.Action1)) {
                ((ConfirmMovementFromEntrance) message).process(this.serverHandler);
                currPhase = Phase.Action2;
            } else {
                serverHandler.getOutgoingMessages().add(message);
            }
        } else if (message instanceof MoveProfessorMessage) {
            if (currPhase.equals(Phase.Action2)) {
                ((MoveProfessorMessage) message).process(this.serverHandler);
            } else {
                serverHandler.getOutgoingMessages().add(message);
            }
        } else if (message instanceof DenyMovementMessage) {
                if (currPhase.equals(Phase.Action2)) {
                    ((DenyMovementMessage) message).process(this.serverHandler);
                } else {
                    serverHandler.getOutgoingMessages().add(message);
                }
            } else if (message instanceof ConfirmMovementMessage) {
            if (currPhase.equals(Phase.Action2)) {
                ((ConfirmMovementMessage) message).process(this.serverHandler);
                currPhase = Phase.Action3;
            } else {
                serverHandler.getOutgoingMessages().add(message);
            }
        } else if (message instanceof ConfirmCloudMessage) {
            if (currPhase.equals(Phase.Action3)) {
                ((ConfirmCloudMessage) message).process(this.serverHandler);
            } else {
                serverHandler.getOutgoingMessages().add(message);
            }
        } else if(message instanceof EndMessage) {
            if (currPhase.equals(Phase.Action3)) {
                ((EndMessage) message).process(this.serverHandler);
                currPhase = Phase.Ending;
            } else {
                serverHandler.getOutgoingMessages().add(message);
            }
        }
    }

    public void requireNickname() {
        System.out.print("Nickname?");
        String temp = input.nextLine();
        send(new SendNickMessage(temp));
    }


    private void buildConnection() {
        while(serverHandler == null) {
            System.out.print("Host?\n");
            host = input.nextLine();
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
        }
    }

    private void newgame() {
        while (!currPhase.equals(Phase.CreatingGame));
        int numPlayer = 0, wiz = 0;
        String mode = "";
        while(numPlayer > 4 || numPlayer < 2) {
            System.out.print("choose between 2,3, or 4 for total number of players.\n");
            numPlayer = Integer.parseInt(input.nextLine());
        }
        while(!mode.equals("true") && !mode.equals("false")) {
            System.out.print("turn on expert mode? Respond with true or false.\n");
            mode = input.nextLine();
        }
        while(wiz > 4 || wiz < 1) {
            System.out.print("Choose a wizard for yourself. Type in 1,2,3, or 4.\n");
            wiz = Integer.parseInt(input.nextLine());
        }
        send(new SendStartInfoMessage(numPlayer, Boolean.parseBoolean(mode), Wizard.values()[Integer.parseInt(input.nextLine())]));
    }

    private void joinGame() {
        while (!currPhase.equals(Phase.JoiningGame1));
        int match = -1, wiz = -1;
        for (int i = 1; i <= matchesID.size(); i++) {
            System.out.print(i + "." + matchesID.get(i - 1) + "\n");
        }
        while(match < 0 || match > matchesID.size() - 1) {
            System.out.print("Choose a match.\n");
            match = Integer.parseInt(input.nextLine()) - 1;
        }
        send(new MatchChosenMessage(match));
        while (!currPhase.equals(Phase.JoiningGame2));
        for (int i = 1; i <= wizards.size(); i++) {
            System.out.print(i + "." + wizards.get(i - 1) + "\n");
        }
        while(wiz < 0 || wiz > wizards.size() - 1) {
            System.out.print("Choose a wizard.\n");
            wiz = Integer.parseInt(input.nextLine()) - 1;
        }
        send(new SendChosenWizardMessage(Wizard.values()[wiz]));
    }

    private void playAssistant() {
        while (!currPhase.equals(Phase.Planning));
        int assis = -1;
        for (int i = 1; i <= assistants.length; i++) {
            System.out.print(i + "." + assistants[i - 1] + "\n");
        }
        while(assis < 1 || assis > assistants.length - 1) { //TODO did not use UsedAssistantMessage
            System.out.print("Choose an assistant card.\n");
            assis = Integer.parseInt(input.nextLine()) - 1;
            if (assistants[assis] == null) {
                System.out.print("You cannot choose this assistant card.\n");
                assis = -1;
            }
        }
        send(new SendAssistantMessage(assis));
    }

    private void moveStudentsFromEntrance() {
        while (!currPhase.equals(Phase.Action1));
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
        while (!currPhase.equals(Phase.Action2));
        int num = -1;
        while (num < 0 || num >= numIslands) {
            printIslands();
            System.out.print("Where would you like to move the mather nature?\n");
            num = Integer.parseInt(input.nextLine());
        }
        send(new MoveMotherNatureMessage(num));
    }

    public void chooseCloud() {
        while (!currPhase.equals(Phase.Action3));
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
        for (int i = 0; i < clouds.size(); i++) {
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

    public void setMatchesID(List<Integer> matchesID) {
        this.matchesID = matchesID;
    }

    public void setPlayers(List<List<String>> players) {
        this.players = players;
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

    public void setClouds(ArrayList<Cloud> clouds) {
        this.clouds = clouds;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}
