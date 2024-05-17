package it.polimi.ingsw.protocol.client.view;

import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerArea;
import it.polimi.ingsw.model.cards.Cell;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.cards.PlayerHand;
import it.polimi.ingsw.model.cards.enumeration.Reign;
import it.polimi.ingsw.model.cards.enumeration.Resource;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.*;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.messages.responseMessage;


import java.util.*;
import java.util.stream.Collectors;

/**
 * this class is the view of the client, it is used to visualize the game in the command line
 */
public class ViewCLI extends View {
    //the purpose of ViewCLI is to handle the interaction with the user and
    // visualize what he needs to see in order to play the game
    // by printing it in the command line


    //COLORS

    // ANSI escape code constants for text color
    private static final String RESET = "\033[0m";  // Reset to default color
    private static final String BLACK_TEXT = "\033[0;30m";
    private static final String RED_TEXT = "\033[0;31m";
    private static final String GREEN_TEXT = "\033[0;32m";
    private static final String YELLOW_TEXT = "\033[0;33m";
    private static final String BLUE_TEXT = "\033[0;34m";
    private static final String PURPLE_TEXT = "\033[0;35m";
    private static final String CYAN_TEXT = "\033[0;36m";
    private static final String WHITE_TEXT = "\033[0;37m";

    // ANSI escape code constants for background color
    private static final String BLACK_BACKGROUND = "\033[40m";
    private static final String RED_BACKGROUND = "\033[41m";
    private static final String GREEN_BACKGROUND = "\033[42m";
    private static final String YELLOW_BACKGROUND = "\033[43m";
    private static final String BLUE_BACKGROUND = "\033[44m";
    private static final String PURPLE_BACKGROUND = "\033[45m";
    private static final String CYAN_BACKGROUND = "\033[46m";
    private static final String WHITE_BACKGROUND = "\033[47m";

    /**
     * method {@code ViewCLI}: constructs a new ViewCLI
     */
    public ViewCLI() {
        super();

    }

    /**
     * method {@code askPortIP}: asks the user to enter the IP and the port of the server
     * @return String[]
     */
    @Override
    public String[] askPortIP(){
        Scanner scanner = new Scanner(System.in);
        String[] server = new String[2];

        System.out.println("Enter IP:");
        server[0] = scanner.nextLine();
        System.out.println("Enter port:");
        server[1] = scanner.nextLine();

        return server;
    }



    /**
     * communicates to the user he lost connection
     */
    public void playerDisconnected() {
        System.out.println("\nYou have been disconnected.\n");
    }




    /**
     * Visualizes the current state of the players
     * @param message: currentStateMessage
     */
    public void updatePlayer (currentStateMessage message){
        System.out.println("Player " + message.getCurrentPlayer().getNickname() + " is in the state " + message.getStateName());

        System.out.println("The online players are: " + message.getOnlinePlayers());

        if(!Objects.equals(message.getStateName(), "StarterCardState"))
        {
            ArrayList<Integer> objectivesID = new ArrayList<>();
            objectivesID.add(message.getCommonObjectiveCards().get(0).getID());
            objectivesID.add(message.getCommonObjectiveCards().get(1).getID());

            System.out.println("The common objectives are: " + objectivesID);

            if(!Objects.equals(message.getStateName(), "ObjectiveState")) {
                if(message.getPlayer().getNickname().equals(message.getCurrentPlayer().getNickname())){
                    System.out.println("The private objective is: " + message.getCurrentPlayer().getObjective().getID());
                }
                else {
                    System.out.println("The private objective is: ?");
                }
            }
        }

        this.showCommonArea(message.getCurrentPlayer().getCommonArea());

        System.out.println("Score of " + message.getCurrentPlayer().getNickname() + ": " + message.getCurrentPlayer().getScore());

        if(!Objects.equals(message.getStateName(), "StarterCardState")) {
            this.showPlayerArea(message.getCurrentPlayer().getPlayerArea());

        }

        this.showPlayerHand(message.getCurrentPlayer(), message.getPlayer().getNickname());

        if (message.isLastTurn()) {
            System.out.println("\nTHIS IS THE LAST TURN\n");
        }

        System.out.println("\n");
    }

    /**
     * Prints the current state of the common area
     * @param area CommonArea to be printed
     */
    private void showCommonArea(CommonArea area) {
       System.out.println("\nCARDS ON THE TABLE: ");
       System.out.println("(1) Resource deck top card: " + area.getD1().getList().getFirst().getReign());
       System.out.println("(2) Gold deck top card: " + area.getD2().getList().getFirst().getReign());

       for(int i=0; i<area.getTableCards().size(); i++) {
           PlaceableCard card = area.getTableCards().get(i);
           if (card != null)
               System.out.println("("+ (i + 3) +") Card ID: " + area.getTableCards().get(i).getID());
           else
               System.out.println("("+ (i + 3) +") Card ID: " + "empty");
       }

       System.out.println("\n");

    }

    /**
     * Prints the current state of the player area
     * @param area PlayerArea to be printed
     */
    private void showPlayerArea(PlayerArea area) {
        System.out.println("\n");

        ArrayList<Integer> resourceList = area.getResources();
        String[] resourceNames = {"FUNGUS", "INSECT", "ANIMAL", "PLANT", "MANUSCRIPT", "QUILL", "INKWELL", "EMPTY"};

        for (int i = 0; i < resourceList.size() - 1; i++) {
            System.out.println(resourceNames[i] + ": " + resourceList.get(i));
        }
        System.out.println("\n");

        ArrayList<Cell> Cells = area.getAllCards().stream()
                .map(PlaceableCard::getCells)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));

        int minRow = Cells.stream()
                        .mapToInt(Cell::getRow)
                        .min()
                        .orElse(0);

        int maxRow = Cells.stream()
                        .mapToInt(Cell::getRow)
                        .max()
                        .orElse(0);

        int minColumn = Cells.stream()
                .mapToInt(Cell::getColumn)
                .min()
                .orElse(0);

        int maxColumn = Cells.stream()
                .mapToInt(Cell::getColumn)
                .max()
                .orElse(0);

        // Print the column numbers
        System.out.print("   ");
        for (int i = minColumn; i <= maxColumn; i++) {
            System.out.print(String.format("%2d", i) + "  ");
        }
        System.out.println();

        for (int i = minRow; i <= maxRow; i++) {
            ArrayList<String> lines = new ArrayList<>();
            lines.add(String.format("%2d", i) + " ");
            // lines.add("");
            lines.add("   ");
            lines.add("   ");

            // System.out.print(i + " ");
            for(int j = minColumn; j <= maxColumn; j++) {
                Cell cell = findCell(i, j, Cells);
                if(cell != null) {
                    PlaceableCard card = cell.getTopCard();
                    boolean isTopCard = true;
                    if (card == null) {
                        card = cell.getBottomCard();
                        isTopCard = false;
                    }

                    Integer relativePosition = findRelativeCellPosition(cell);

                    switch (relativePosition) {
                        case 0 -> {
                            // The cell is the top left corner of the card
                            // So all the printed space is of the same card
                            String fillColor = this.fillColor(card.getReign());
                            ArrayList<String> center = this.fillCenterCard(card);
                            lines.set(0, lines.get(0) + WHITE_BACKGROUND + WHITE_TEXT + this.resourceToPrint(cell.getResource()) + RESET + fillColor);
                            // lines.set(1, lines.get(1) + WHITE_BACKGROUND + WHITE_TEXT + "  " + RESET + fillColor);
                            lines.set(1, lines.get(1) + fillColor + center.get(0));
                            lines.set(2, lines.get(2) + fillColor + center.get(1));
                        }
                        case 1 -> {
                            // The cell is the top right corner of the card
                            // So the top-right part to be printed is not of the same card
                            String fillColor;
                            if(isTopCard) {
                                fillColor = this.fillColor(cell.getBottomCard().getReign());
                            } else {
                                fillColor = BLACK_BACKGROUND + "  " + RESET;
                            }
                            lines.set(0, lines.get(0) + WHITE_BACKGROUND + WHITE_TEXT + this.resourceToPrint(cell.getResource()) + RESET + fillColor);
                            // lines.set(1, lines.get(1) + WHITE_BACKGROUND + WHITE_TEXT + "  " + RESET + fillColor);

                            fillColor = this.fillColor(card.getReign());

                            lines.set(1, lines.get(1) + fillColor + BLACK_BACKGROUND + "  " + RESET);
                            lines.set(2, lines.get(2) + fillColor + BLACK_BACKGROUND + "  " + RESET);
                        }
                        case 2 -> {
                            // The cell is the bottom left corner of the card
                            // So the bottom-left part to be printed is not of the same card
                            String fillColor = this.fillColor(card.getReign());
                            lines.set(0, lines.get(0) + WHITE_BACKGROUND + WHITE_TEXT + this.resourceToPrint(cell.getResource()) + RESET + fillColor);
                            // lines.set(1, lines.get(1) + WHITE_BACKGROUND + WHITE_TEXT + "  " + RESET + fillColor);

                            if(isTopCard) {
                                fillColor = this.fillColor(cell.getBottomCard().getReign());
                            } else {
                                fillColor = BLACK_BACKGROUND + "  " + RESET;
                            }

                            lines.set(1, lines.get(1) + fillColor + BLACK_BACKGROUND + "  " + RESET);
                            lines.set(2, lines.get(2) + fillColor + BLACK_BACKGROUND + "  " + RESET);
                        }
                        case 3 -> {
                            // The cell is the bottom right corner of the card
                            // So only the top right part is of the same card
                            String fillColor;
                            if (isTopCard) {
                                fillColor = this.fillColor(cell.getBottomCard().getReign());
                            } else {
                                fillColor = BLACK_BACKGROUND + "  " + RESET;
                            }

                            lines.set(0, lines.get(0) + WHITE_BACKGROUND + WHITE_TEXT + this.resourceToPrint(cell.getResource()) + RESET + fillColor);
                            // lines.set(1, lines.get(1) + WHITE_BACKGROUND + WHITE_TEXT + "  " + RESET + fillColor);
                            lines.set(1, lines.get(1) + fillColor + fillColor);
                            lines.set(2, lines.get(2) + fillColor + fillColor);
                        }
                    }

                } else {
                    // The cell doesn't exist, so it is all black
                    for (int k = 0; k < 3; k++) {
                        lines.set(k, lines.get(k) + BLACK_BACKGROUND + "    " + RESET);
                    }
                }
            }

            for (String line : lines) {
                System.out.println(line);
            }
        }

        System.out.println("\n");
        ArrayList<String> availablePositionsString = area.getAvailablePosition().stream()
                .map(position -> "[" + position[0] + ", " + position[1] + "]")
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.println("The available positions are [row, column]: ");
        for (String position : availablePositionsString) {
            System.out.println("> " + position);
        }
        System.out.println("\n");
    }

    /**
     * Finds a cell in a list of cells
     * @param row the row of the cell
     * @param column the column of the cell
     * @param cells the list of cells
     * @return the cell if found, null otherwise
     */
    private Cell findCell(int row, int column, List<Cell> cells) {
        return cells.stream()
            .filter(cell -> cell.getRow() == row && cell.getColumn() == column)
            .findFirst()
            .orElse(null);
    }

    /**
     * Finds the relative position of a cell in a card
     * @param cell the cell to find
     * @return the relative position of the cell in the card
     */
    private Integer findRelativeCellPosition(Cell cell) {
        if(cell == null)
            return null;

        PlaceableCard card = cell.getTopCard();
        if (card == null) {
            card = cell.getBottomCard();
        }

        ArrayList<Cell> cells = card.getCells();
        for (int i = 0; i < cells.size(); i++) {
            if (cells.get(i) == cell) {
                return i;
            }
        }

        return 0;

    }

    /**
     * Converts a resource to a string to be printed
     * @param res the resource to be converted
     * @return the string to be printed
     */
    private String resourceToPrint(Resource res) {
        String resource = "";
        switch (res) {
            case Fungus -> resource = "FU";
            case Insect -> resource = "IN";
            case Animal -> resource = "AN";
            case Plant -> resource = "PL";
            case Manuscript -> resource = "MA";
            case Quill -> resource = "QU";
            case Inkwell -> resource = "IK";
            case Empty, Blocked -> resource = "  ";
        }
        return resource;
    }

    /**
     * Prints two colored empty spaces
     * @param res the reign of the card
     * @return the string to be printed
     */
    private String fillColor(Reign res) {
        String color = "";
        switch (res) {
            case Fungus -> {
                color = RED_BACKGROUND + "  " + RESET;
            }
            case Insect -> {
                color = PURPLE_BACKGROUND + "  " + RESET;
            }
            case Animal -> {
                color = CYAN_BACKGROUND + "  " + RESET;
            }
            case Plant -> {
                color = GREEN_BACKGROUND + "  " + RESET;
            }
            case null, default -> {
                color = YELLOW_BACKGROUND + "  " + RESET;
            }
        }
        return color;
    }

    /**
     * Fills the center of the card with the correct color, resource and ID
     * @param card the card to be printed
     * @return the center of the card to be printed
     */
    private ArrayList<String> fillCenterCard(PlaceableCard card) {
        ArrayList<String> center = new ArrayList<>();
        center.add("");
        center.add("");

        switch (card.getReign()) {
            case Fungus -> {
                if (card.isFront()) {
                    center.set(0, RED_BACKGROUND + "  " + RESET);
                } else {
                    center.set(0, RED_BACKGROUND + "FU" + RESET);
                }
                center.set(1, RED_BACKGROUND + String.format("%02d", card.getID()) + RESET);
            }
            case Insect -> {
                if (card.isFront()) {
                    center.set(0, PURPLE_BACKGROUND + "  " + RESET);
                } else {
                    center.set(0, PURPLE_BACKGROUND + "IN" + RESET);
                }
                center.set(1, PURPLE_BACKGROUND + String.format("%02d", card.getID()) + RESET);
            }
            case Animal -> {
                if (card.isFront()) {
                    center.set(0, CYAN_BACKGROUND + "  " + RESET);
                } else {
                    center.set(0, CYAN_BACKGROUND + "AN" + RESET);
                }
                center.set(1, CYAN_BACKGROUND + String.format("%02d", card.getID()) + RESET);
            }
            case Plant -> {
                if (card.isFront()) {
                    center.set(0, GREEN_BACKGROUND + "  " + RESET);
                } else {
                    center.set(0, GREEN_BACKGROUND + "PL" + RESET);
                }
                center.set(1, GREEN_BACKGROUND + String.format("%02d", card.getID()) + RESET);
            }
            case null, default -> {
                center.set(0, YELLOW_BACKGROUND + "  " + RESET);
                center.set(1, YELLOW_BACKGROUND + String.format("%02d", card.getID()) + RESET);
            }
        }

        return center;
    }

    /**
     * Prints the current state of the player hand
     * @param currentPlayer the player whose hand is to be printed
     */
    private void showPlayerHand(Player currentPlayer, String viewer) {
        //PlayerHand hand = message.getCurrentPlayer().getPlayerHand();
        PlayerHand hand = currentPlayer.getPlayerHand();
        System.out.println("\nPLAYER HAND: ");
        if (currentPlayer.getNickname().equals(viewer)) {
            int i = 0;
            for (PlaceableCard card : hand.getPlaceableCards()) {
                System.out.println("("+i+") Card ID: " + card.getID());
                i += 1;
            }
        } else {
            for (PlaceableCard card : hand.getPlaceableCards()) {
                System.out.println("Card: " + card.getReign());
            }
        }
    }

    /**
     * this method allow the user to say if he wants to connect with socket or rmi
     * @return boolean
     */
    public boolean askSocket () {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Do you want to use Socket or RMI?");
            String choice = scanner.nextLine().toLowerCase();
            if (choice.equals("socket")) return true;
            else if (choice.equals("rmi")) return false;
        }
    }


    /**
     * this method allow the client to visualize the server response about the connection
     * @param message
     */
    public void answerToConnection (connectionResponseMessage message){
        if (message.getCorrect())
            System.out.println("the connection has been established");
    }

    /**
     *
     * @param message
     * @return message with the values chosen by the user
     */
    public serverOptionMessage serverOptions (serverOptionMessage message){
        Scanner scanner = new Scanner(System.in);
        boolean newMatch = false;
        Integer matchID = null;
        Integer startedMatchID = null;
        boolean loadMatch = false;
        Integer savedMatchID = null;

        // Asks the user if he wants to join a new match
        while(true) {
            System.out.print("Do you want to join a new match? [YES/no] ");
            String choice = scanner.nextLine().toLowerCase();
            if(choice.equals("yes") || choice.equals("y")) {
                newMatch = true;
            } else if(choice.equals("no") || choice.equals("n")) {
                newMatch = false;
            } else {
                System.out.println("ANSWER NOT VALID");
                continue;
            }
            break;
        }

        // Asks the user if he wants to create a new match or join an existing one in waiting
        while (newMatch) {
            System.out.print("Create a New Match (1) or Join a Match (2)? ");
            String choice = scanner.nextLine().toLowerCase();
            if(choice.equals("2")) {
                    if(message.getWaitingMatches().isEmpty()) {
                        System.out.println("There are no matches to join: creating a new match");
                        matchID = null;
                        break;
                    } else {
                        System.out.println("Here are the matches you can join: ");
                        for (Integer id : message.getWaitingMatches()) {
                            System.out.println(id);
                        }
                        System.out.print("Enter the match ID: ");
                        try {
                            matchID = scanner.nextInt();
                            scanner.nextLine();
                            break;
                        } catch (Exception e) {
                            System.out.println("ANSWER NOT VALID");
                            continue;
                        }
                    }

            } else if(choice.equals("1")) {
                matchID = null;
                break;
            } else {
                System.out.println("ANSWER NOT VALID");
                continue;
            }
        }


        if(!newMatch) {

            boolean runMatch = false;
            while (true)  {
                System.out.println("Join a running match? [YES/no]");
                String choice = scanner.nextLine().toLowerCase();
                if(choice.equals("yes") || choice.equals("y")) {
                    runMatch = true;
                } else if(choice.equals("no") || choice.equals("n")) {
                    runMatch = false;
                } else {
                    System.out.println("ANSWER NOT VALID");
                    continue;
                }
                break;

            }


            if(runMatch) {

                // Asks the user which running match he wants to join
                while (true) {
                    if (message.getRunningMatches().isEmpty()) {
                        System.out.println("There are no matches to join.");
                        startedMatchID = null;
                        break;
                    } else {
                        System.out.println("Here are the matches you can join: ");
                        for (Integer id : message.getRunningMatches()) {
                            System.out.println(id);
                        }
                        System.out.println("Enter the started match ID");
                        try {
                            startedMatchID = scanner.nextInt();
                            scanner.nextLine();
                            break;
                        } catch (Exception e) {
                            System.out.println("You didn't enter an int value");
                            continue;
                        }
                    }
                }

            } else {
                while (true) {
                    System.out.print("Join a saved match? [YES/no] ");
                    String choice = scanner.nextLine().toLowerCase();
                    if(choice.equals("yes") || choice.equals("y")) {
                        loadMatch = true;
                    } else if(choice.equals("no") || choice.equals("n")) {
                        loadMatch = false;
                    } else {
                        System.out.println("ANSWER NOT VALID");
                        continue;
                    }
                    break;
                }

                if(loadMatch) {
                    // Asks the user which saved match he wants to join
                    while (true) {
                        if (message.getSavedMatches().isEmpty()) {
                            System.out.println("There are no matches to join.");
                            savedMatchID = null;
                            break;
                        } else {
                            System.out.println("Here are the matches you can join: ");
                            for (Integer id : message.getSavedMatches()) {
                                System.out.println(id);
                            }
                            System.out.println("Enter the saved match ID");
                            try {
                                savedMatchID = scanner.nextInt();
                                scanner.nextLine();
                                break;
                            } catch (Exception e) {
                                System.out.println("ANSWER NOT VALID");
                                continue;
                            }
                        }
                    }
                }


            }

        }

        message = new serverOptionMessage(newMatch, matchID, startedMatchID, loadMatch, savedMatchID);
        return message;
    }

    /**
     * allow the user to see if he managed to join the match
     * @param message
     */
    public void answerToOption (serverOptionResponseMessage message){
        if (message.getCorrect()) {
            System.out.println("You joined correctly the match " + message.getMatchID());
        } else {
            System.out.println("An error occurred, you couldn't join the game");
        }
    }

    /**
     * this method shows what nickname are not available and allows the user to choose his nickname
     * @param message
     * @return
     */
    public String unavailableNames(unavailableNamesMessage message){
        //the client can call the method view.unavailableNames passing as a parameter the arraylist of unavailable names received from server
        if(!Objects.equals(message.toString(), "[]"))
            System.out.println("This nicknames are not available: " + message.toString());

        String name;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a nickname:");
        name = scanner.nextLine();
        return name;
    }

    /**
     * visualize the response about the value entered
     * @param message
     */
    public void answer (responseMessage message){
        if (!message.getCorrect())
            System.out.println("You didn't entered a valid value, please try again");
    }


    /**
     * this method shows what colors are available and allows the user to choose his color
     * @param message
     */
    public String availableColors (availableColorsMessage message){
        //the client can call the method view.availableColors passing as a parameter the arraylist of available colors received from server
        System.out.println("This are the colors that are available: " + message.toString());

        String color;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a color:");
        color = scanner.nextLine();
        return color;
    }

    /**
     *this method allow the user to place his starter card
     * @return int
     */
    public int placeStarter () {
        int side = 1000;
        System.out.println("Place your starter card.");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Do you want to place it on the front side or on the back side?");
            String choice = scanner.nextLine().toLowerCase();
            switch (choice) {
                case "front", "front side" -> {
                    return 1;
                }
                case "back", "back side" -> {
                    return 0;
                }
            }
        }
    }


    /**
     * allow the user to choose how many players will play
     * @return number of expected players
     */
    public int expectedPlayers () {
        int numExpected = 0;
        Scanner scanner = new Scanner(System.in);
        while(true) {

            try {
                System.out.println("How many player do you want to be in the game");
                numExpected = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (Exception e) {
                System.out.println("You didn't enter an integer value");
            }
        }
        return numExpected;

    }

    /**
     * allow the user to choose his secret objective
     * @return objective
     */
    public int chooseObjective (ArrayList<ObjectiveCard> objectives) {
        int objective = 1000;
        System.out.println("You have to choose your personal objective");

        for (int i = 0; i < objectives.size(); i++) {
            System.out.println("(" + (i + 1) + ") Objective  ID: " + objectives.get(i).getID());
        }

        System.out.println("Enter '1' if you want to choose the first one, '2' if you want to choose the second one");
        while(true) {
            try {
                Scanner scanner = new Scanner(System.in);
                objective = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (Exception e) {
                System.out.println("You didn't enter an integer value");
            }
        }
        return objective;
    }

    /**
     * allow the user to say what card he wants to play, front or back, and in which position
     * @return
     */
    public int[] placeCard () {
        int[] chosenCard = new int[4];
        chosenCard[0] = 1000;
        chosenCard[1] = 1000;
        chosenCard[2] = 1000;
        chosenCard[3] = 1000;
        Scanner scanner = new Scanner(System.in);
        while(true) {
            try {
                System.out.println("Enter the NUMBER of the card you want to place");
                chosenCard[0] = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (Exception e) {
                System.out.println("You didn't enter an integer value");
            }
        }

        boolean correct = false;
        while(!correct) {
            System.out.println("FRONT or BACK?");
            String choice = scanner.nextLine().toLowerCase();
            switch (choice) {
                case "front", "front side" -> {
                    chosenCard[1] = 1;
                    correct = true;
                }
                case "back", "back side" -> {
                    chosenCard[1] = 0;
                    correct = true;
                }
            }
        }


        while(true) {
            try {
                System.out.print("ROW: ");
                chosenCard[2] = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (Exception e) {
                System.out.println("You didn't enter an integer value");
            }
        }


        while(true) {
            try {
                System.out.print("COLUMN: ");
                chosenCard[3] = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (Exception e) {
                System.out.println("You didn't enter an integer value");
            }
        }

        return chosenCard;
    }

    /**
     * allow the user to say what card he wants to pick
     * @return the id of the card the user wants to pick
     */
    public int pickCard () {
        int choice = 1000;
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("enter the NUMBER of the card you want to pick");
            choice = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("you didn't enter an integer value");
        }
        return choice;
    }


    /**
     * visualize the final information about the game, points and number of objectives achieved by the players
     * @param message
     */
    public void endGame (declareWinnerMessage message){
        HashMap<String, Integer> points = new HashMap<>();
        HashMap<String, Integer> numObjectives = new HashMap<>();

        points = message.getPlayersPoints();
        numObjectives = message.getNumberOfObjects();

        System.out.println("\n\nSCOREBOARD\n");
        for (String playerName : points.keySet()) {
            Integer playerPoints = points.get(playerName);
            Integer playerObjectives = numObjectives.get(playerName);
            System.out.println("Player Name: " + playerName + " - Points: " + playerPoints + " - Number of Objectives: " + playerObjectives);
        }
    }

    @Override
    public void update(updatePlayerMessage update) {
        Player player = update.getPlayer();
        this.showCommonArea(player.getCommonArea());
        this.showPlayerArea(player.getPlayerArea());
        this.showPlayerHand(player, update.getNicknameViewer());

    }
}
