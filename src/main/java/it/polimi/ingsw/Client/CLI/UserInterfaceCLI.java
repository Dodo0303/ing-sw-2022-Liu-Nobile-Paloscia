package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Exceptions.WrongEffectException;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.StudentColor;
import it.polimi.ingsw.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class UserInterfaceCLI{

    private CLI cli;
    private BufferedReader input;

    public UserInterfaceCLI() {
        input = new BufferedReader(new InputStreamReader(System.in));
    }

    public String requireUserInput() {
        String str = "";
        try {
            str = input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    public void menu() {
        clearScreen();
        int coins = cli.getGame().getPlayerByNickname(cli.getNickname()).getCoins();
        int num = -1;
        while ((cli.isExpert() && num!= 6) || (!cli.isExpert() && num != 5)) {
            System.out.println("\n\nMenu:\n ");
            System.out.println("You have " + coins + " coins\n");
            System.out.println("1. Islands");
            System.out.println("2. Clouds");
            System.out.println("3. My school board");
            System.out.println("4. Others' school boards");;
            if (cli.isExpert() && cli.isMyTurn()) {
                System.out.println("5. Use character card");
                System.out.println("6. Continue/Refresh\n");
            } else if ((cli.isExpert() && !cli.isMyTurn())|| (cli.getCurrPhase().equals(Phase.Character1)) || cli.getCurrPhase().equals(Phase.Character5)|| cli.getCurrPhase().equals(Phase.Character7)|| cli.getCurrPhase().equals(Phase.Character11)){
                System.out.println("5. See character cards");
                System.out.println("6. Continue/Refresh\n");
            } else {
                System.out.println("5. Continue/Refresh\n");
            }
            String in = requireUserInput();
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
                    printSchoolBoard(cli.getGame().getPlayerByNickname(cli.getNickname()));
                    break;
                }
                case 4 : {
                    int index = 1;
                    int playerChosen = -1;
                    List<Player> playersToShow = new ArrayList<>(cli.getGame().getPlayers());
                    playersToShow.remove(cli.getGame().getPlayerByNickname(cli.getNickname()));
                    System.out.println("\nAvailable players: ");
                    for (int i = 0; i < playersToShow.size(); i++) {
                        Player player = playersToShow.get(i);
                        System.out.println(index + ". " + player.getNickName());
                        index++;
                    }
                    String temp = requireUserInput();
                    if (Utilities.isNumeric(temp)) {
                        playerChosen = Integer.parseInt(temp) - 1;
                        if (playerChosen < cli.getGame().getPlayers().size()) {
                            System.out.println("Hai inserito " + playerChosen + ": " + cli.getGame().getPlayers().get(playerChosen).getNickName());
                            printSchoolBoard(cli.getGame().getPlayers().get(playerChosen));
                        } else {
                            System.out.println("No such player.");
                        }
                    }
                    break;
                }
                case 5 : {
                    if (cli.isExpert()) {
                        int characterIndex = -1;
                        printCharacters();
                        if (cli.isMyTurn()) {
                            System.out.println("Choose a character, or enter 'e' to go back.");
                            String temp = requireUserInput();
                            if (temp.equals("e")) {
                                break;
                            }
                            if (Utilities.isNumeric(temp)) {
                                characterIndex = Integer.parseInt(temp);
                                if (characterIndex <= cli.getGame().getCharacters().size()) {
                                    cli.useCharacter(characterIndex - 1);
                                } else {
                                    System.out.println("No such character card.");
                                }
                            }
                        } else {
                            System.out.println("Enter to return.");
                            requireUserInput();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
        clearScreen();
    }


    void printIslands() {
        int numIslands = cli.getGame().getIslands().size();
        for (int i = 0; i < numIslands; i++) {
            printIsland(i);
        }
    }


    public void printIsland(int index){
        System.out.println("Island " + index);
        if (cli.getGame().getIslands().get(index).getTowerColor().equals(Color.VOID))
            System.out.println("No ♜ on this island");
        else
            System.out.println(cli.getGame().getIslands().get(index).getTowerColor() + " owns " + cli.getGame().getIslands().get(index).getNumTower() + " ♜");
        if (cli.isExpert()) {
            System.out.println("No Entries: " + cli.getGame().getIslands().get(index).getNoEntries());
        }
        if (cli.getGame().getMotherNatureIndex() == index)
            System.out.println("Mother nature is here");
        System.out.println("Students:");
        System.out.println("\uD83D\uDD34: " + cli.getGame().getIslands().get(index).getStudents().get(StudentColor.RED));
        System.out.println("\uD83D\uDFE1: " + cli.getGame().getIslands().get(index).getStudents().get(StudentColor.YELLOW));
        System.out.println("\uD83D\uDFE2: " + cli.getGame().getIslands().get(index).getStudents().get(StudentColor.GREEN));
        System.out.println("\uD83D\uDD35: " + cli.getGame().getIslands().get(index).getStudents().get(StudentColor.BLUE));
        System.out.println("\uD83D\uDFE3: " + cli.getGame().getIslands().get(index).getStudents().get(StudentColor.PINK));
    }

    public void printClouds() {
        for (int i = 0; i < cli.getGame().getClouds().size(); i++) {
            System.out.print("cloud " + i + " :\n");
            System.out.print("Students:\n");
            for (StudentColor color: cli.getGame().getClouds().get(i).getStudents()) {
                System.out.println(color + "  ");
            }
        }
    }

    public void printSchoolBoard(Player player) {
        System.out.println("\n" + player.getNickName().toUpperCase() + "'S school board:\n");
        System.out.println("Entrance: ");
        for (int i = 0; i < player.getEntranceStudents().size(); i++) {
            System.out.print(i + ")" + player.getEntranceStudents().get(i) + " ");
        }
        System.out.println("\n");

        System.out.println("Dining room:");
        for (StudentColor color: player.getDiningTables().keySet()) {
            System.out.println(color + ": " + player.getDiningTables().get(color).getNumOfStudents());
        }

        System.out.println("Professors: ");
        if (player.getProfessors().size() == 0) {
            System.out.print("none");
        }
        for (int i = 0; i < player.getProfessors().size(); i++) {
            System.out.print(player.getProfessors().get(i) + " ");
        }
        if (cli.getGame().getPlayers().size() == 4) {
            System.out.println("Towers of the team: " + player.getTowerNum()+ player.getColor().toString());
        } else {
            System.out.println("Towers: " + player.getTowerNum()+ player.getColor().toString());
        }
        if (cli.isExpert()) {
            System.out.println("Coins: " + player.getCoins());
        }
    }

    public void printCharacters()  {
        try {
            for (int i = 1; i <= cli.getGame().getCharacters().size(); i++) {
                if (cli.getGame().getCharacters().get(i - 1) == null) {
                    continue;
                }
                if (cli.getGame().getCharacters().get(i - 1).getID() == 1 ) {
                    System.out.print(i + ". character" + i + ": cost: " + cli.getGame().getCharacterById(1).getPrice() + "; Students: ");
                    try {
                        for(StudentColor color : cli.getGame().getCharacterById(1).getStudents()) {
                            System.out.print(color.toString() + " ");
                        }
                        System.out.println("");
                    } catch (WrongEffectException e) {
                        e.printStackTrace();
                    }
                } else if (cli.getGame().getCharacters().get(i - 1).getID() == 5) {
                    try {
                        System.out.println(i + ". character" + i + ": cost: " + cli.getGame().getCharacterById(5).getPrice() + "; Number of no entries: " + cli.getGame().getCharacterById(5).getNumberOfNoEntries());
                    } catch (WrongEffectException e) {
                        e.printStackTrace();
                    }
                } else if (cli.getGame().getCharacters().get(i - 1).getID() == 7) {
                    System.out.print(i + ". character" + i + ": cost: " + cli.getGame().getCharacterById(7).getPrice() + "; Students: ");
                    try {
                        for(StudentColor color : cli.getGame().getCharacterById(7).getStudents()) {
                            System.out.print(color.toString() + " ");
                        }
                        System.out.println("");
                    } catch (WrongEffectException e) {
                        e.printStackTrace();
                    }
                } else if (cli.getGame().getCharacters().get(i - 1).getID() == 11) {
                    System.out.print(i + ". character" + i + ": cost: " + cli.getGame().getCharacterById(11).getPrice() + "; Students: ");
                    try {
                        for(StudentColor color : cli.getGame().getCharacterById(11).getStudents()) {
                            System.out.print(color.toString() + " ");
                        }
                        System.out.println("");
                    } catch (WrongEffectException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println(i + ". character" + cli.getGame().getCharacters().get(i - 1).getID() + ": cost: " + cli.getGame().getCharacters().get(i - 1).getPrice());
                }
            }
        } catch (GameException e) {
            e.printStackTrace();
        }

    }

    public void printTitle(){
        System.out.println(
                "███████╗██████╗░██╗░█████╗░███╗░░██╗████████╗██╗░░░██╗░██████╗\n" +
                        "██╔════╝██╔══██╗██║██╔══██╗████╗░██║╚══██╔══╝╚██╗░██╔╝██╔════╝\n" +
                        "█████╗░░██████╔╝██║███████║██╔██╗██║░░░██║░░░░╚████╔╝░╚█████╗░\n" +
                        "██╔══╝░░██╔══██╗██║██╔══██║██║╚████║░░░██║░░░░░╚██╔╝░░░╚═══██╗\n" +
                        "███████╗██║░░██║██║██║░░██║██║░╚███║░░░██║░░░░░░██║░░░██████╔╝\n" +
                        "╚══════╝╚═╝░░╚═╝╚═╝╚═╝░░╚═╝╚═╝░░╚══╝░░░╚═╝░░░░░░╚═╝░░░╚═════╝░");
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void setCli(CLI cli) {
        this.cli = cli;
    }

}
