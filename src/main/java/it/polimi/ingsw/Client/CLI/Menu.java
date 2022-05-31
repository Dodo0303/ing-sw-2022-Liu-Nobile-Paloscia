package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Menu implements Runnable{
    private CLI cli;
    private GameModel game;
    private BufferedReader  input;
    private String nickname, callMethod;
    private boolean unlock;

    Menu(String callMethod) {
        this.callMethod = callMethod;
        unlock = false;
    }

    @Override
    public void run() {
        try {
            game = cli.getGame();
            input = new BufferedReader (new InputStreamReader(System.in));
            menu();
            while ((cli.myTurn && !unlock)) {
                menu();
            }
            cli.setInMenu(false);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printIslands() {
        int numIslands = game.getIslands().size();
        for (int i = 0; i < numIslands; i++) {
            printIsland(i);
        }
    }


    private void printIsland(int index){
        System.out.println("Island " + index);
        if (game.getIslands().get(index).getTowerColor().equals(Color.VOID))
            System.out.println("No ♜ on this island");
        else
            System.out.println(game.getIslands().get(index).getTowerColor() + " owns " + game.getIslands().get(index).getNumTower() + " ♜");
        if (game.getMotherNatureIndex() == index)
            System.out.println("Mother nature is here");
        System.out.println("Students:");
        System.out.println("\uD83D\uDD34: " + game.getIslands().get(index).getStudents().get(StudentColor.RED));
        System.out.println("\uD83D\uDFE1: " + game.getIslands().get(index).getStudents().get(StudentColor.YELLOW));
        System.out.println("\uD83D\uDFE2: " + game.getIslands().get(index).getStudents().get(StudentColor.GREEN));
        System.out.println("\uD83D\uDD35: " + game.getIslands().get(index).getStudents().get(StudentColor.BLUE));
        System.out.println("\uD83D\uDFE3: " + game.getIslands().get(index).getStudents().get(StudentColor.PINK));
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

    private void printSchoolBoard(Player player) {
        System.out.println("Entrance: ");
        for (int i = 0; i < player.getEntranceStudents().size(); i++) {
            System.out.print("  " + player.getEntranceStudents().get(i));
            if (i == player.getEntranceStudents().size() - 1) {
                System.out.println("  " + player.getEntranceStudents().get(i));
            }
        }
        System.out.println("Dining room:");
        for (StudentColor color: player.getDiningTables().keySet()) {
            System.out.println(color + ": " + player.getDiningTables().get(color).getNumOfStudents());
        }
        System.out.println("Professors: ");
        for (int i = 0; i < player.getProfessors().size(); i++) {
            System.out.print("  " + player.getProfessors().get(i));
            if (i == player.getEntranceStudents().size() - 1) {
                System.out.println("  " + player.getProfessors().get(i));
            }
        }
        System.out.println("Towers: There are " + player.getTowerNum() + " in the school board.");
    }

    public void menu() throws IOException {
        clearScreen();
        int num = -1;
        System.out.println("1. see islands");
        System.out.println("2. see clouds");
        System.out.println("3. see your school board");
        System.out.println("4. see others' school boards");
        if (cli.myTurn) {
            System.out.println("5. continue to play");
        } else {
            System.out.println("Waiting for your turn...");
        }
        String in = input.readLine();
        if (Utilities.isNumeric(in)) {
            num = Integer.parseInt(in);
        }
        switch (num) {
            case 1 : {
                printIslands();
                break;
            }
            case 2 : {
                printClouds();
                break;
            }
            case 3 : {
                printSchoolBoard(this.game.getPlayerByNickname(this.nickname));
                break;
            }
            case 4 : {
                int index = 1;
                int playerChosen = -1;
                for (int i = 0; i < game.getPlayers().size(); i++) {
                    Player player = game.getPlayers().get(i);
                    if (!player.getNickName().equals(nickname)) {
                        System.out.println(index + ". player " + player.getNickName());
                    }
                }
                String temp = input.readLine();
                if (Utilities.isNumeric(temp)) {
                    playerChosen = Integer.parseInt(temp);
                }
                if (playerChosen < game.getPlayers().size()) {
                    printSchoolBoard(game.getPlayers().get(playerChosen));
                } else {
                    System.out.println("No such player.");
                }
                break;
            }
            case 5 : {
                if (cli.myTurn) {
                    unlock = true;
                    call();
                }
            }
        }
        clearScreen();
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void call() {
        if (callMethod.equals("playAssistant")) {
            cli.playAssistant();
        } else if (callMethod.equals("moveStudentsFromEntrance")) {
            cli.moveStudentsFromEntrance();
        } else if (callMethod.equals("moveMotherNature")) {
            cli.moveMotherNature();
        } else if (callMethod.equals("chooseCloud")) {
            cli.chooseCloud();
        }
    }

    public void setCli(CLI cli) {
        this.cli = cli;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
