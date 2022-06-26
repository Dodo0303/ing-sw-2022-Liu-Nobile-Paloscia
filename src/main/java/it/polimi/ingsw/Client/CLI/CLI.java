package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Exceptions.EmptyCloudException;
import it.polimi.ingsw.Exceptions.FullTableException;
import it.polimi.ingsw.Exceptions.NotEnoughNoEntriesException;
import it.polimi.ingsw.Exceptions.WrongEffectException;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Network.Messages.toClient.ActionPhase.*;
import it.polimi.ingsw.Network.Messages.toClient.CharacterPhase.*;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.*;
import it.polimi.ingsw.Network.Messages.toClient.PlanningPhase.CloudsUpdateMessage;
import it.polimi.ingsw.Network.Messages.toClient.PlanningPhase.UsedAssistantMessage;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.ChooseCloudMessage;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.MoveMotherNatureMessage;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.MoveStudentFromEntranceMessage;
import it.polimi.ingsw.Network.Messages.toServer.ActionPhase.UseCharacterMessage;
import it.polimi.ingsw.Network.Messages.toServer.CharacterPhase.*;
import it.polimi.ingsw.Network.Messages.toServer.JoiningPhase.*;
import it.polimi.ingsw.Network.Messages.toServer.PlanningPhase.SendAssistantMessage;
import it.polimi.ingsw.Utilities;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class CLI {
    GameModel game;
    boolean closed;
    private String nickname;
    private String host;
    private int port;
    private Scanner input = new Scanner(System.in);
    private ServerHandler serverHandler;
    private Phase currPhase, prevPhase;
    private List<Wizard> wizards;
    private int ap1Moves;
    private boolean expert;
    private boolean myTurn;
    private UserInterfaceCLI view;
    private int numPlayers;
    private String[] nicknames;
    private int currCharacter;

    public void start() {
        ap1Moves = 0;
        closed = false;
        currPhase = Phase.BuildingConnection;
        myTurn = true;
        buildConnection();
        Thread serverHandlerThread  = new Thread(this.serverHandler);
        serverHandlerThread.start();
        startUI("");
        view.printTitle();
        requireNickname();
    }

    public void startUI(String str) {
        view = new UserInterfaceCLI(str);
        view.setCli(this);
    }

    public void send(Object message) {
        if (message == null) {
            throw new IllegalArgumentException("Null cannot be sent as a message.\n");
        } else {
            serverHandler.send(message);
        }
    }

    public void messageReceived(Object message) throws InterruptedException, EmptyCloudException{
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
            ((ChangeTurnMessage) message).process(this.serverHandler);
        } else if (message instanceof ConfirmMovementFromEntranceMessage) {
            ((ConfirmMovementFromEntranceMessage) message).process(this.serverHandler);
        } else if (message instanceof MoveMothernatureMessage) {
            ((MoveMothernatureMessage) message).process(this.serverHandler);
        } else if (message instanceof MoveProfessorMessage) {
            ((MoveProfessorMessage) message).process(this.serverHandler);
        } else if (message instanceof DenyMovementMessage) {
            ((DenyMovementMessage) message).process(this.serverHandler);
        } else if (message instanceof ConfirmMovementMessage) {
            ((ConfirmMovementMessage) message).process(this.serverHandler);
        } else if (message instanceof ConfirmCloudMessage) {
            ((ConfirmCloudMessage) message).process(this.serverHandler);
        } else if(message instanceof EndMessage) {
            ((EndMessage) message).process(this.serverHandler);
        } else if (message instanceof GameModelUpdateMessage) {
            ((GameModelUpdateMessage) message).process(this.serverHandler);
        } else if (message instanceof CloudsUpdateMessage) {
            ((CloudsUpdateMessage) message).process(this.serverHandler);
        } else if (message instanceof UsedAssistantMessage) {
            ((UsedAssistantMessage) message).process(this.serverHandler);
        } else if (message instanceof CharacterUsedMessage) {
            try {
                ((CharacterUsedMessage) message).process(this.serverHandler);
            } catch (WrongEffectException | NotEnoughNoEntriesException e3) {
                System.out.println(e3.getMessage());
            }
        } else if (message instanceof CharacterEntranceSwappedMessage){
            try {
                ((CharacterEntranceSwappedMessage) message).process(this.serverHandler);
            } catch (InterruptedException ignored){

            } catch (EmptyCloudException | FullTableException e1) {
                System.out.println(e1.getMessage());
            }

        } else if (message instanceof EntranceTableSwappedMessage){
            try {
                ((EntranceTableSwappedMessage) message).process(this.serverHandler);
            } catch (InterruptedException ignored){

            } catch (EmptyCloudException | FullTableException e1) {
                System.out.println(e1.getMessage());
            }

        } else if (message instanceof IslandChosenMessage){
            try {
                ((IslandChosenMessage) message).process(this.serverHandler);
            } catch (InterruptedException ignored){

            } catch (EmptyCloudException | FullTableException e1) {
                System.out.println(e1.getMessage());
            }

        } else if (message instanceof NoEntryMovedMessage){
            try {
                ((NoEntryMovedMessage) message).process(this.serverHandler);
            } catch (InterruptedException ignored){

            } catch (EmptyCloudException | FullTableException e1) {
                System.out.println(e1.getMessage());
            }
        } else if (message instanceof StudentColorChosenMessage){
            try {
                ((StudentColorChosenMessage) message).process(this.serverHandler);
            } catch (InterruptedException ignored){

            } catch (EmptyCloudException | FullTableException e1) {
                System.out.println(e1.getMessage());
            }
        } else if (message instanceof StudentMovedFromCharacterMessage){
            try {
                ((StudentMovedFromCharacterMessage) message).process(this.serverHandler);
            } catch (InterruptedException ignored){

            } catch (EmptyCloudException | FullTableException e1) {
                System.out.println(e1.getMessage());
            }
        } else if (message instanceof StudentMovedToTableMessage){
            try {
                ((StudentMovedToTableMessage) message).process(this.serverHandler);
            } catch (InterruptedException ignored){

            } catch (EmptyCloudException | FullTableException e1) {
                System.out.println(e1.getMessage());
            }
        }
    }

    public void requireNickname() {
        System.out.print("Nickname?\n");
        String temp = view.requireUserInput();
        send(new SendNickMessage(temp));
    }

    private void buildConnection() {
        try {
            while(serverHandler == null) {
                //System.out.print("Host?\n");
                //host = view.requireUserInput();
                host = "localhost";
                port = 12345;
                /*
                String in = "";
                while (!Utilities.isNumeric(in)) {
                    System.out.print("Port?\n");
                    in = view.requireUserInput();
                    port = Integer.parseInt(in);
                }

                 */
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
        int temp = 0;
        while(temp != 1 && temp != 2) {
            System.out.print("Press 1 for new game, press 2 for joining an existing game.\n");
            String in = view.requireUserInput();
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
        }
    }

    private void newgame() {
        try {
            int numPlayer = 0, wiz = -1;
            String mode = "";
            while(numPlayer > 4 || numPlayer < 2) {
                System.out.print("choose between 2,3, or 4 for total number of players.\n");
                String in = view.requireUserInput();
                if (Utilities.isNumeric(in)) {
                    numPlayer = Integer.parseInt(in);
                }
            }
            while(!mode.equals("true") && !mode.equals("false")) {
                System.out.print("turn on expert mode? Respond with true or false.\n");
                mode = view.requireUserInput().toLowerCase();
                if (mode.equals("true")) {
                    this.expert = true;
                } else {
                    this.expert = false;
                }
            }
            while(wiz > 3 || wiz < 0) {
                System.out.print("Choose a wizard for yourself. Type in 1,2,3, or 4.\n");
                String in = view.requireUserInput();
                if (Utilities.isNumeric(in)) {
                    wiz = Integer.parseInt(in) - 1;
                }
            }
            send(new SendStartInfoMessage(numPlayer, Boolean.parseBoolean(mode), Wizard.values()[wiz]));

        } catch (Exception e) {
            System.out.print("Something went wrong, please try again.\n");
            newgame();
        }
    }

    public void joinGame(List<Integer> matchIDs) {
        try {
            int match = -1;
            while (match < 0) {
                System.out.print("Choose a match.\n");
                String in = view.requireUserInput();
                if (Utilities.isNumeric(in)) {
                    match = Integer.parseInt(in);
                }
            }
            send(new MatchChosenMessage(matchIDs.get(match - 1)));
        } catch (Exception e) {
            System.out.print("Something went wrong, please try again.\n");
            e.printStackTrace();
            joinGame(matchIDs);
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
                String in = view.requireUserInput();;
                if (Utilities.isNumeric(in)) {
                    wiz = Integer.parseInt(in) - 1;
                }
            }
            setPhase(Phase.JoiningGame2);
            send(new SendChosenWizardMessage(wizards.get(wiz)));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("Something went wrong, please try again.\n");
            chooseWizard();
        }
    }

    public void playAssistant() {
        try {
            while (!currPhase.equals(Phase.Planning)) {
                currPhase = getCurrPhase();
            }
            int assis = -1;
            while(assis < 0 || assis > 9) {
                for (int i = 1; i <= 10; i++) {
                    Assistant assistant = game.getPlayers().get(game.getPlayerIndexFromNickname(nickname)).getAssistants().get(i - 1);
                    if (assistant != null) {
                        System.out.println(i + ". MaxStep: " +  assistant.getMaxSteps() + ", Value: "+ assistant.getValue());
                    }
                }
                System.out.print("Choose assistant card.\n");
                String in = view.requireUserInput();
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
            System.out.print("Something went wrong, please try again.\n");
            playAssistant();
        }
    }

    public void moveStudentsFromEntrance() {
        while (!currPhase.equals(Phase.Action1)) {
            currPhase = getCurrPhase();
        }
        int num = -1, islandID = -1, i, index = -1;
        List<StudentColor>  entrance = game.getPlayers().get(game.getPlayerIndexFromNickname(nickname)).getEntranceStudents();
        int numIslands = game.getIslands().size();
        StudentColor tempColor;
        while (index < 0 || index > entrance.size() - 1) {
            i = 0;
            System.out.print("You have in your entrance:\n");
            for (StudentColor color: entrance) {
                System.out.print(i + ")" + color + " ");
                i++;
            }
            System.out.print("Which student would you like to move?(Write the index). Press 'm' to check menu.\n");
            String in = view.requireUserInput();
            if (in.equals("m")){
                view.menu();
            }
            if (Utilities.isNumeric(in))
                index = Integer.parseInt(in);
        }
        while (num != 0 && num != 1) {
            System.out.print("Where would you like to move the student?(0 for dining table, 1 for island). Press 'm' to check menu.\n");
            String in = view.requireUserInput();
            if (in.equals("m")){
                view.menu();
            }
            if (Utilities.isNumeric(in)) {
                num = Integer.parseInt(in);
            }
        }
        if (num == 1) {
            while(islandID < 0 || islandID >= numIslands) {
                view.printIslands();
                System.out.print("To which island would you like to move the student?. Press 'm' to check menu.\n");
                String in = view.requireUserInput();
                if (in.equals("m")){
                    view.menu();
                }
                if (Utilities.isNumeric(in)) {
                    islandID = Integer.parseInt(in);
                }
            }
        }
        send(new MoveStudentFromEntranceMessage(index, num, islandID));
    }

    public void moveMotherNature() {
        while (!currPhase.equals(Phase.Action2)) {
            currPhase = getCurrPhase();
        }
        int num = -1;
        while (num < 0 || num >= game.getIslands().size()) {
            view.printIslands();
            System.out.print("Where would you like to move the mother nature? Press 'm' to check menu.\n");
            String in = view.requireUserInput();
            if (in.equals("m")){
                view.menu();
            }
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
        while (num < 0 || num >= game.getIslands().size() - 1) {
            view.printClouds();
            System.out.print("Which cloud would you like to take students from? Press 'm' to check menu.\n");
            String in = view.requireUserInput();
            if (in.equals("m")){
                view.menu();
            }
            if (Utilities.isNumeric(in)) {
                num = Integer.parseInt(in);
            }
        }
        send(new ChooseCloudMessage(num));
    }

    public void shutDown() {
        System.exit(0);
    }

    void useCharacter(int characterIndex) {
        int realCharacterIndex = game.getCharacters().get(characterIndex).getID();
        if (game.getPlayerByNickname(nickname).getCoins() < game.getCharacterById(realCharacterIndex).getPrice()) {
            System.out.println("The character is too expensive for you.");
            return;
        }
        send(new UseCharacterMessage(realCharacterIndex));
        if (realCharacterIndex == 1) {
            prevPhase = currPhase;
            currPhase = Phase.Character1;
            character1();
        } else if (realCharacterIndex == 3) {
            prevPhase = currPhase;
            currPhase = Phase.Character3;
            character3();
        } else if (realCharacterIndex == 5) {
            prevPhase = currPhase;
            currPhase = Phase.Character5;
            character5();
        } else if (realCharacterIndex == 7) {
            prevPhase = currPhase;
            currPhase = Phase.Character7;
            character7();
        }  else if (realCharacterIndex == 9) {
            prevPhase = currPhase;
            currPhase = Phase.Character9;
            character9();
        } else if (realCharacterIndex == 10) {
            prevPhase = currPhase;
            currPhase = Phase.Character10;
            character10();
        } else if (realCharacterIndex == 11) {
            prevPhase = currPhase;
            currPhase = Phase.Character11;
            character11();
        } else if (realCharacterIndex == 12) {
            prevPhase = currPhase;
            currPhase = Phase.Character12;
            character12();
        }
    }

    private void character1() {
        int numStudent = -1;
        int i = 1;
        while (numStudent < 0) {
            System.out.print("Students: ");
            try {
                for(StudentColor color : game.getCharacterById(1).getStudents()) {
                    System.out.print(i + ") " + color.toString() + " ");
                }
                System.out.println("");
            } catch (WrongEffectException e) {
                e.printStackTrace();
            }
            System.out.print("Which Student would you like to take? Enter 'm' to see menu.\n");
            String in = view.requireUserInput();
            if (in.equals("m")){
                view.menu();
            } else if (Utilities.isNumeric(in)) {
                numStudent = Integer.parseInt(in);
            }
        }
        int numIsland = -1;
        while (numIsland < 0 || numIsland >= game.getIslands().size()) {
            view.printIslands();
            System.out.print("Where would you like to put the student? Press 'm' to check menu.\n");
            String in = view.requireUserInput();
            if (in.equals("m")){
                view.menu();
            }
            if (Utilities.isNumeric(in)) {
                numIsland = Integer.parseInt(in);
            }
        }
        send(new MoveStudentFromCharacterMessage(numStudent, numIsland));
    }

    private void character3() {
        int numIsland = -1;
        while (numIsland < 0 || numIsland >= game.getIslands().size()) {
            view.printIslands();
            System.out.print("Which island would you like to choose for your character effect?? Press 'm' to check menu.\n");
            String in = view.requireUserInput();
            if (in.equals("m")){
                view.menu();
            }
            if (Utilities.isNumeric(in)) {
                numIsland = Integer.parseInt(in);
            }
        }
        send(new ChooseIslandMessage(numIsland));
    }
    private void character5() {
        int numIsland = -1;
        while (numIsland < 0 || numIsland >= game.getIslands().size()) {
            view.printIslands();
            System.out.print("Which island would you like to choose for your character effect?? Press 'm' to check menu.\n");
            String in = view.requireUserInput();
            if (in.equals("m")){
                view.menu();
            }
            if (Utilities.isNumeric(in)) {
                numIsland = Integer.parseInt(in);
            }
        }
        send(new MoveNoEntryMessage(numIsland));
    }

    private void character7() {
        int i = 1;
        int[] characterStudents = new int[3];
        int[] entranceStudents = new int[3];
        int index1 = 0, index2 = 0;
        for (int j = 0; j < 3; j++) {
            int numStudent = -1;
            int numIsland = -1;
            while (numStudent < 0) {
                System.out.print("Students: ");
                try {
                    for(StudentColor color : game.getCharacterById(7).getStudents()) {
                        System.out.print(i + ") " + color.toString() + " ");
                    }
                    System.out.println("");
                } catch (WrongEffectException e) {
                    e.printStackTrace();
                }
                System.out.print("Which Student would you like to take? Enter 'm' to see menu.\n");
                String in = view.requireUserInput();
                if (in.equals("m")){
                    view.menu();
                } else if (Utilities.isNumeric(in)) {
                    numStudent = Integer.parseInt(in);
                    characterStudents[index1++] = numStudent;
                }
            }
            while (numIsland < 0 || numIsland >= game.getIslands().size()) {
                view.printIslands();
                System.out.print("Where would you like to put the student? Press 'm' to check menu.\n");
                String in = view.requireUserInput();
                if (in.equals("m")){
                    view.menu();
                }
                if (Utilities.isNumeric(in)) {
                    numIsland = Integer.parseInt(in);
                    entranceStudents[index2++] = numIsland;
                }
            }
        }
        send(new SwapStudentsCharacterEntranceMessage(characterStudents, entranceStudents));
    }
    private void character9() {
        StudentColor res;
        while (true) {
            System.out.println("Choose a color.");
            for (StudentColor color : StudentColor.values()) {
                System.out.println(color.toString());
            }
            String in = view.requireUserInput();
            if (Utilities.existInStudentColor(in)) {
                res = Utilities.getColor(in);
                send(new ChooseStudentColorMessage(res));
                break;
            }
        }

    }
    private void character10() {
        int i = 1;
        StudentColor[] colors = new StudentColor[2];
        int[] entranceStudents = new int[2];
        int index1 = 0, index2 = 0;
        for (int j = 0; j < 2; j++) {
            int numEntranceStudent = -1;
            while (true) {
                System.out.print("Dining room: ");
                for (StudentColor color: game.getPlayerByNickname(nickname).getDiningTables().keySet()) {
                    System.out.println(color + ": " + game.getPlayerByNickname(nickname).getDiningTables().get(color).getNumOfStudents());
                }
                System.out.println("");
                System.out.print("Which color of student would you like to choose? Enter 'm' to see menu.\n");
                String in = view.requireUserInput();
                if (in.equals("m")){
                    view.menu();
                } else if (Utilities.existInStudentColor(in)) {
                    colors[index1++] = Utilities.getColor(in);
                    break;
                }
            }
            while (numEntranceStudent < 0) {
                System.out.print("Entrance students: ");
                for(StudentColor color : game.getPlayerByNickname(nickname).getEntranceStudents()) {
                    System.out.print(i + ") " + color.toString() + " ");
                }
                System.out.println("");
                System.out.print("Which Student would you like to take? Enter 'm' to see menu.\n");
                String in = view.requireUserInput();
                if (in.equals("m")){
                    view.menu();
                } else if (Utilities.isNumeric(in)) {
                    numEntranceStudent = Integer.parseInt(in);
                    entranceStudents[index1++] = numEntranceStudent;
                }
            }
        }
        send(new SwapStudentsTableEntranceMessage(colors, entranceStudents));
    }
    private void character11() {
        int numStudent = -1;
        int i = 1;
        while (numStudent < 0) {
            System.out.print("Students: ");
            try {
                for(StudentColor color : game.getCharacterById(7).getStudents()) {
                    System.out.print(i + ") " + color.toString() + " ");
                }
                System.out.println("");
            } catch (WrongEffectException e) {
                e.printStackTrace();
            }
            System.out.print("Which Student would you like to take to your dining room? Enter 'm' to see menu.\n");
            String in = view.requireUserInput();
            if (in.equals("m")){
                view.menu();
            } else if (Utilities.isNumeric(in)) {
                numStudent = Integer.parseInt(in);
            }
        }
        send(new MoveStudentsToTableMessage(numStudent));
    }
    private void character12() {
        StudentColor res;
        while (true) {
            System.out.println("Choose a color.");
            for (StudentColor color : StudentColor.values()) {
                System.out.println(color.toString());
            }
            String in = view.requireUserInput();
            if (Utilities.existInStudentColor(in)) {
                res = Utilities.getColor(in);
                send(new ChooseStudentColorMessage(res));
                break;
            }
        }
    }

    public String getNickname() {
        return nickname;
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

    public void setGame(GameModel game) {
        this.game = game;
    }

    public GameModel getGame() {
        return game;
    }

    public boolean isExpert() {
        return expert;
    }

    public void setExpert(boolean expert) {
        this.expert = expert;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public Phase getPrevPhase() {
        return prevPhase;
    }

    public void setPrevPhase(Phase prevPhase) {
        this.prevPhase = prevPhase;
    }

    public UserInterfaceCLI getView() {
        return view;
    }

    public void setCurrCharacter(int characterID) {
        this.currCharacter = characterID;
    }

    public int getAp1Moves() {
        return ap1Moves;
    }

    public void setAp1Moves(int ap1Moves) {
        this.ap1Moves = ap1Moves;
    }


    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }


    public String[] getNicknames() {
        return nicknames;
    }

    public void setNicknames(String[] nicknames) {
        this.nicknames = nicknames;
    }

}
