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
import it.polimi.ingsw.protocol.messages.ConnectionState.availableColorsMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.connectionResponseMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.unavailableNamesMessage;
import it.polimi.ingsw.protocol.messages.EndGameState.declareWinnerMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.messages.responseMessage;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is the view of the client, it is used to visualize the game in the command line
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

    private static final String BRIGHT_BLACK_BACKGROUND = "\033[100m";
    private static final String BRIGHT_RED_BACKGROUND = "\033[101m";
    private static final String BRIGHT_GREEN_BACKGROUND = "\033[102m";
    private static final String BRIGHT_YELLOW_BACKGROUND = "\033[103m";
    private static final String BRIGHT_BLUE_BACKGROUND = "\033[104m";
    private static final String BRIGHT_PURPLE_BACKGROUND = "\033[105m";
    private static final String BRIGHT_CYAN_BACKGROUND = "\033[106m";
    private static final String BRIGHT_WHITE_BACKGROUND = "\033[107m";

    /**
     * Method {@code ViewCLI}: constructs a new ViewCLI
     */
    public ViewCLI() {
        super();
    }

    /**
     * Method {@code getBgColor}: gets the background color
     *
     * @param card the card to get the background color of
     * @return the background color
     */
    private static String getBgColor(PlaceableCard card) {
        Reign reign = card.getReign();
        String BGColor;
        switch (reign) {
            case Fungus -> BGColor = RED_BACKGROUND;

            case Insect -> BGColor = PURPLE_BACKGROUND;

            case Animal -> BGColor = CYAN_BACKGROUND;

            case Plant -> BGColor = GREEN_BACKGROUND;

            case null -> BGColor = YELLOW_BACKGROUND;

        }
        return BGColor;
    }

    /**
     * Method {@code askPortIP}: asks the user to enter the IP and the port of the server
     *
     * @return String[]
     */
    @Override
    public String askIP() {
        Scanner scanner = new Scanner(System.in);
        String server;

        System.out.print("Enter IP: ");
        server = scanner.nextLine();
        return server;
    }

    /**
     * Communicates to the user he lost connection
     */
    public void playerDisconnected() {
        System.out.println("\n" + RED_BACKGROUND + "You have been disconnected." + RESET + "\n");
    }

    /**
     * Visualizes the current state of the players
     *
     * @param message: currentStateMessage
     */
    public void updatePlayer(currentStateMessage message) {
        System.out.println("\n This is the turn of " + message.getCurrentPlayer().getNickname());
        System.out.println("ID of the match: " + message.getMatchID());
        System.out.println("The online players are: " + message.getOnlinePlayers());

        if (!Objects.equals(message.getStateName(), "StarterCardState")) {
            ArrayList<String> objectives = new ArrayList<>();
            addOutput(objectives);
            printObjective(message.getCommonObjectiveCards().get(0).getID(), objectives);
            printObjective(message.getCommonObjectiveCards().get(1).getID(), objectives);

            System.out.println("The common objectives are: ");
            printOutput(objectives);

            if (!Objects.equals(message.getStateName(), "ObjectiveState")) {
                if (message.getPlayer().getNickname().equals(message.getCurrentPlayer().getNickname())) {
                    System.out.println("The private objective is: ");
                    objectives = new ArrayList<>();
                    addOutput(objectives);
                    printObjective(message.getPlayer().getObjective().getID(), objectives);
                    printOutput(objectives);
                } else {
                    System.out.println("The private objective is: ?");
                }
            }
        }

        this.showCommonArea(message.getCurrentPlayer().getCommonArea());

        System.out.println("Score of " + message.getCurrentPlayer().getNickname() + ": " + message.getCurrentPlayer().getScore());

        if (!Objects.equals(message.getStateName(), "StarterCardState")) {
            this.showPlayerArea(message.getCurrentPlayer().getPlayerArea());
        }

        this.showPlayerHand(message.getCurrentPlayer(), message.getPlayer().getNickname());

        if (message.isLastTurn()) {
            System.out.println("\nTHIS IS THE LAST TURN\n");
        }

        System.out.println("\n");
    }

    /**
     * @param output ArrayList<String>
     */
    private void addOutput(ArrayList<String> output) {
        output.add("");
        output.add("");
        output.add("");
        output.add("");
        output.add("");
        output.add("");
        output.add("");
        output.add("");
    }

    /**
     * Prints the current state of the common area
     *
     * @param area CommonArea to be printed
     */
    protected void showCommonArea(CommonArea area) {
        System.out.println("\nCARDS ON THE TABLE: ");
        this.firstRowCommonArea(area);
        this.secondRowCommonArea(area);
    }

    /**
     * Prints the first row of the common area
     *
     * @param area CommonArea to be printed
     */
    private void firstRowCommonArea(CommonArea area) {
        String index = "";
        ArrayList<String> output = new ArrayList<>();
        // Sets up the first 4 lines of the output
        output.add("");
        output.add("");
        output.add("");

        index += String.format("%11s", " (1) DECK");
        index += String.format("%11s", " (2) DECK");
        index += " (3)  " + "     ";

        try {
            this.printDeck(output, area.getD1().getList().getFirst());
        } catch (IndexOutOfBoundsException e) {
            this.printDeck(output, null);
        }
        try {
            this.printDeck(output, area.getD2().getList().getFirst());
        } catch (IndexOutOfBoundsException e) {
            this.printDeck(output, null);
        }
        try {
            this.printCard(output, area.getTableCards().getFirst());
        } catch (IndexOutOfBoundsException e) {
            this.printCard(output, null);
        }

        System.out.println(index);
        System.out.println(output.get(0));
        System.out.println(output.get(1));
        System.out.println(output.get(2));
        System.out.println("\n");
    }

    /**
     * Prints the second row of the common area
     *
     * @param area CommonArea to be printed
     */
    private void secondRowCommonArea(CommonArea area) {
        String index = "";
        ArrayList<String> output = new ArrayList<>();
        // Sets up the first 4 lines of the output
        output.add("");
        output.add("");
        output.add("");

        index += " (4)  " + "     ";
        index += " (5)  " + "     ";
        index += " (6)  " + "     ";

        try {
            this.printCard(output, area.getTableCards().get(1));
        } catch (IndexOutOfBoundsException e) {
            this.printCard(output, null);
        }
        try {
            this.printCard(output, area.getTableCards().get(2));
        } catch (IndexOutOfBoundsException e) {
            this.printCard(output, null);
        }
        try {
            this.printCard(output, area.getTableCards().get(3));
        } catch (IndexOutOfBoundsException e) {
            this.printCard(output, null);
        }

        System.out.println(index);
        System.out.println(output.get(0));
        System.out.println(output.get(1));
        System.out.println(output.get(2));
        System.out.println("\n");
    }

    /**
     * Prints the color and the type (resource or gold) of the first card on top of the deck
     *
     * @param output ArrayList<String> to be printed
     * @param card   PlaceableCard on top of the deck
     */
    private void printDeck(ArrayList<String> output, PlaceableCard card) {

        // Adds a space column between the decks
        output.set(0, output.get(0) + " ");
        output.set(1, output.get(1) + " ");
        output.set(2, output.get(2) + " ");

        if (card == null) {
            setOutput(output);

        } else {
            Reign reign = card.getReign();

            if (card.isGold()) {
                switch (reign) {
                    case Fungus -> {
                        output.set(0, output.get(0) + RED_BACKGROUND + String.format("%3s", "") + YELLOW_BACKGROUND + String.format("%4s", "") + RED_BACKGROUND + String.format("%3s", "") + RESET);
                        output.set(1, output.get(1) + YELLOW_BACKGROUND + " " + RED_BACKGROUND + String.format("%1s", "") + "FUNGUS" + RED_BACKGROUND + String.format("%1s", "") + YELLOW_BACKGROUND + " " + RESET);
                        output.set(2, output.get(2) + RED_BACKGROUND + String.format("%3s", "") + YELLOW_BACKGROUND + String.format("%4s", "") + RED_BACKGROUND + String.format("%3s", "") + RESET);
                    }
                    case Insect -> {
                        output.set(0, output.get(0) + PURPLE_BACKGROUND + String.format("%3s", "") + YELLOW_BACKGROUND + String.format("%4s", "") + PURPLE_BACKGROUND + String.format("%3s", "") + RESET);
                        output.set(1, output.get(1) + YELLOW_BACKGROUND + " " + PURPLE_BACKGROUND + String.format("%1s", "") + "INSECT" + PURPLE_BACKGROUND + String.format("%1s", "") + YELLOW_BACKGROUND + " " + RESET);
                        output.set(2, output.get(2) + PURPLE_BACKGROUND + String.format("%3s", "") + YELLOW_BACKGROUND + String.format("%4s", "") + PURPLE_BACKGROUND + String.format("%3s", "") + RESET);
                    }
                    case Animal -> {
                        output.set(0, output.get(0) + CYAN_BACKGROUND + String.format("%3s", "") + YELLOW_BACKGROUND + String.format("%4s", "") + CYAN_BACKGROUND + String.format("%3s", "") + RESET);
                        output.set(1, output.get(1) + YELLOW_BACKGROUND + " " + CYAN_BACKGROUND + String.format("%1s", "") + "ANIMAL" + CYAN_BACKGROUND + String.format("%1s", "") + YELLOW_BACKGROUND + " " + RESET);
                        output.set(2, output.get(2) + CYAN_BACKGROUND + String.format("%3s", "") + YELLOW_BACKGROUND + String.format("%4s", "") + CYAN_BACKGROUND + String.format("%3s", "") + RESET);
                    }
                    case Plant -> {
                        output.set(0, output.get(0) + GREEN_BACKGROUND + String.format("%3s", "") + YELLOW_BACKGROUND + String.format("%4s", "") + GREEN_BACKGROUND + String.format("%3s", "") + RESET);
                        output.set(1, output.get(1) + YELLOW_BACKGROUND + " " + GREEN_BACKGROUND + String.format("%1s", "") + "PLANT" + GREEN_BACKGROUND + String.format("%2s", "") + YELLOW_BACKGROUND + " " + RESET);
                        output.set(2, output.get(2) + GREEN_BACKGROUND + String.format("%3s", "") + YELLOW_BACKGROUND + String.format("%4s", "") + GREEN_BACKGROUND + String.format("%3s", "") + RESET);
                    }
                }
                return;
            }

            switch (reign) {
                case Fungus -> {
                    output.set(0, output.get(0) + RED_BACKGROUND + String.format("%10s", "") + RESET);
                    output.set(1, output.get(1) + RED_BACKGROUND + String.format("%2s", "") + "FUNGUS" + RED_BACKGROUND + String.format("%2s", "") + RESET);
                    output.set(2, output.get(2) + RED_BACKGROUND + String.format("%10s", "") + RESET);
                }
                case Insect -> {
                    output.set(0, output.get(0) + PURPLE_BACKGROUND + String.format("%10s", "") + RESET);
                    output.set(1, output.get(1) + PURPLE_BACKGROUND + String.format("%2s", "") + "INSECT" + PURPLE_BACKGROUND + String.format("%2s", "") + RESET);
                    output.set(2, output.get(2) + PURPLE_BACKGROUND + String.format("%10s", "") + RESET);
                }
                case Animal -> {
                    output.set(0, output.get(0) + CYAN_BACKGROUND + String.format("%10s", "") + RESET);
                    output.set(1, output.get(1) + CYAN_BACKGROUND + String.format("%2s", "") + "ANIMAL" + CYAN_BACKGROUND + String.format("%2s", "") + RESET);
                    output.set(2, output.get(2) + CYAN_BACKGROUND + String.format("%10s", "") + RESET);
                }
                case Plant -> {
                    output.set(0, output.get(0) + GREEN_BACKGROUND + String.format("%10s", "") + RESET);
                    output.set(1, output.get(1) + GREEN_BACKGROUND + String.format("%2s", "") + "PLANT" + GREEN_BACKGROUND + String.format("%3s", "") + RESET);
                    output.set(2, output.get(2) + GREEN_BACKGROUND + String.format("%10s", "") + RESET);
                }
                case null -> {
                    output.set(0, output.get(0) + YELLOW_BACKGROUND + String.format("%10s", "") + RESET);
                    output.set(1, output.get(1) + YELLOW_BACKGROUND + String.format("%10s", "") + RESET);
                    output.set(2, output.get(2) + YELLOW_BACKGROUND + String.format("%10s", "") + RESET);
                }
            }

        }
    }

    /**
     * @param output ArrayList<String>
     */
    private void setOutput(ArrayList<String> output) {
        output.set(0, output.get(0) + BLACK_BACKGROUND + String.format("%10s", "") + RESET);
        output.set(1, output.get(1) + BLACK_BACKGROUND + String.format("%2s", "") + "EMPTY" + String.format("%3s", "") + RESET);
        output.set(2, output.get(2) + BLACK_BACKGROUND + String.format("%10s", "") + RESET);
    }

    /**
     * Adds a card to be printed in the ArrayList<String> output
     *
     * @param output ArrayList<String> to be printed
     * @param card   PlaceableCard to be printed
     */
    private void printCard(ArrayList<String> output, PlaceableCard card) {

        // Adds a space column between the cards
        output.set(0, output.get(0) + " ");
        output.set(1, output.get(1) + " ");
        output.set(2, output.get(2) + " ");


        if (card == null) {
            setOutput(output);
            return;
        }

        String BGColor = getBgColor(card);

        // Gets all the resources of the card
        ArrayList<String> resources = this.resourceToPrint(card);

        if (card.isGold()) {
            output.set(0, output.get(0) + BGColor + resources.get(0) + YELLOW_BACKGROUND + String.format("%4s", "") + resources.get(1) + RESET);
            if (card.isFront())
                output.set(1, output.get(1) + YELLOW_BACKGROUND + " " + BGColor + String.format("%3s%2d%3s", "", card.getID(), "") + YELLOW_BACKGROUND + " " + RESET);
            else
                output.set(1, output.get(1) + YELLOW_BACKGROUND + " " + BGColor + String.format("%1s%2s%1s%3s%1s", "", card.getID(), "", this.reignToPrint(card), "") + YELLOW_BACKGROUND + " " + RESET);
            output.set(2, output.get(2) + BGColor + resources.get(2) + YELLOW_BACKGROUND + String.format("%4s", "") + resources.get(3) + RESET);
            return;
        }

        output.set(0, output.get(0) + BGColor + resources.get(0) + BGColor + String.format("%4s", "") + resources.get(1) + RESET);
        if (card.isFront())
            output.set(1, output.get(1) + BGColor + String.format("%4s%2d%4s", "", card.getID(), "") + RESET);
        else if (card.isResource())
            output.set(1, output.get(1) + BGColor + String.format("%2s%2s%1s%3s%2s", "", card.getID(), "", this.reignToPrint(card), "") + RESET);
        else if (card.isStarter())
            output.set(1, output.get(1) + BGColor + String.format("%4s%2d%4s", "", card.getID(), "") + RESET);
        output.set(2, output.get(2) + BGColor + resources.get(2) + BGColor + String.format("%4s", "") + resources.get(3) + RESET);

    }

    /**
     * Converts the resources of a card to a string to be printed
     *
     * @param card the card to be converted
     * @return the string to be printed
     */
    private ArrayList<String> resourceToPrint(PlaceableCard card) {
        ArrayList<String> output = new ArrayList<>();
        for (Resource resource : card.getResource()) {
            switch (resource) {
                case Fungus -> output.add(BLACK_TEXT + BRIGHT_WHITE_BACKGROUND + "FUN" + RESET);
                case Insect -> output.add(BLACK_TEXT + BRIGHT_WHITE_BACKGROUND + "INS" + RESET);
                case Animal -> output.add(BLACK_TEXT + BRIGHT_WHITE_BACKGROUND + "ANI" + RESET);
                case Plant -> output.add(BLACK_TEXT + BRIGHT_WHITE_BACKGROUND + "PLA" + RESET);
                case Manuscript -> output.add(BLACK_TEXT + BRIGHT_WHITE_BACKGROUND + "MAN" + RESET);
                case Quill -> output.add(BLACK_TEXT + BRIGHT_WHITE_BACKGROUND + "QUI" + RESET);
                case Inkwell -> output.add(BLACK_TEXT + BRIGHT_WHITE_BACKGROUND + "INK" + RESET);
                case Empty -> output.add(BLACK_TEXT + BRIGHT_WHITE_BACKGROUND + "   " + RESET);
                case Blocked -> {
                    switch (card.getReign()) {
                        case Fungus -> output.add(RED_BACKGROUND + "   " + RESET);
                        case Insect -> output.add(PURPLE_BACKGROUND + "   " + RESET);
                        case Animal -> output.add(CYAN_BACKGROUND + "   " + RESET);
                        case Plant -> output.add(GREEN_BACKGROUND + "   " + RESET);
                        case null -> output.add(YELLOW_BACKGROUND + "   " + RESET);
                    }
                }
            }
        }
        return output;
    }

    /**
     * Converts the resources of a cell to a string to be printed
     *
     * @param cell the cell to be converted
     * @return the string to be printed
     */
    private String resourceToPrint(Cell cell) {
        String output = "";
        PlaceableCard card = cell.getTopCard();
        if (card == null) {
            card = cell.getBottomCard();
        }
        switch (cell.getResource()) {
            case Fungus -> output += (BLACK_TEXT + BRIGHT_WHITE_BACKGROUND + "FUN" + RESET);
            case Insect -> output += (BLACK_TEXT + BRIGHT_WHITE_BACKGROUND + "INS" + RESET);
            case Animal -> output += (BLACK_TEXT + BRIGHT_WHITE_BACKGROUND + "ANI" + RESET);
            case Plant -> output += (BLACK_TEXT + BRIGHT_WHITE_BACKGROUND + "PLA" + RESET);
            case Manuscript -> output += (BLACK_TEXT + BRIGHT_WHITE_BACKGROUND + "MAN" + RESET);
            case Quill -> output += (BLACK_TEXT + BRIGHT_WHITE_BACKGROUND + "QUI" + RESET);
            case Inkwell -> output += (BLACK_TEXT + BRIGHT_WHITE_BACKGROUND + "INK" + RESET);
            case Empty -> output += (BLACK_TEXT + BRIGHT_WHITE_BACKGROUND + "   " + RESET);
            case Blocked -> {
                switch (card.getReign()) {
                    case Fungus -> output += (RED_BACKGROUND + "   " + RESET);
                    case Insect -> output += (PURPLE_BACKGROUND + "   " + RESET);
                    case Animal -> output += (CYAN_BACKGROUND + "   " + RESET);
                    case Plant -> output += (GREEN_BACKGROUND + "   " + RESET);
                    case null -> output += (YELLOW_BACKGROUND + "   " + RESET);
                }
            }
        }
        return output;
    }

    /**
     * Converts the reign of a card to a string to be printed
     *
     * @param card the card to be converted
     * @return the string to be printed
     */
    private String reignToPrint(PlaceableCard card) {
        String output;
        switch (card.getReign()) {
            case Fungus -> output = "FUN";
            case Insect -> output = "INS";
            case Animal -> output = "ANI";
            case Plant -> output = "PLA";
            case null -> output = "   ";
        }
        return output;
    }


    /**
     * Prints the current state of the player area
     *
     * @param area PlayerArea to be printed
     */
    public void showPlayerArea(PlayerArea area) {
        System.out.println("\n");

        ArrayList<Integer> resourceList = area.getResources();
        String[] resourceNames = {RED_TEXT + "FUNGUS", PURPLE_TEXT + "INSECT", CYAN_TEXT + "ANIMAL", GREEN_TEXT + "PLANT", YELLOW_TEXT + "MANUSCRIPT", YELLOW_TEXT + "QUILL", YELLOW_TEXT + "INKWELL", WHITE_TEXT + "EMPTY"};


        for (int i = 0; i < resourceList.size() - 1; i++) {
            resourceNames[i] = String.format("%-25s", resourceNames[i] + ": " + resourceList.get(i) + RESET);
        }
        System.out.println(resourceNames[0] + " " + resourceNames[1] + " " + resourceNames[2] + " " + resourceNames[3]);
        System.out.println(resourceNames[4] + " " + resourceNames[5] + " " + resourceNames[6] + " " + resourceNames[7]);
        System.out.println();

        ArrayList<Cell> Cells = area.getAllCards().stream()
                .map(PlaceableCard::getCells)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Integer[]> availablePositions = area.getAvailablePosition();

        int minCellRow = Cells.stream()
                .mapToInt(Cell::getRow)
                .min()
                .orElse(0);
        int minAvailableRow = availablePositions.stream()
                .mapToInt(position -> position[0])
                .min()
                .orElse(0);
        int minRow = Math.min(minCellRow, minAvailableRow);

        int maxCellRow = Cells.stream()
                .mapToInt(Cell::getRow)
                .max()
                .orElse(0);
        int maxAvailableRow = availablePositions.stream()
                .mapToInt(position -> position[0])
                .max()
                .orElse(0);
        int maxRow = Math.max(maxCellRow, maxAvailableRow);

        int minCellColumn = Cells.stream()
                .mapToInt(Cell::getColumn)
                .min()
                .orElse(0);
        int minAvailableColumn = availablePositions.stream()
                .mapToInt(position -> position[1])
                .min()
                .orElse(0);
        int minColumn = Math.min(minCellColumn, minAvailableColumn);

        int maxCellColumn = Cells.stream()
                .mapToInt(Cell::getColumn)
                .max()
                .orElse(0);
        int maxAvailableColumn = availablePositions.stream()
                .mapToInt(position -> position[1])
                .max()
                .orElse(0);
        int maxColumn = Math.max(maxCellColumn, maxAvailableColumn);

        // Print the column numbers
        System.out.print("   ");
        for (int i = minColumn; i <= maxColumn; i++) {
            System.out.printf("%2d%5s", i, "");
        }
        System.out.println();

        for (int i = minRow; i <= maxRow; i++) {
            ArrayList<String> lines = new ArrayList<>();
            lines.add(String.format("%2d", i) + " ");
            lines.add("   ");

            for (int j = minColumn; j <= maxColumn; j++) {
                Cell cell = findCell(i, j, Cells);
                if (cell != null) {
                    /*
                     The reference card is always the card on top.
                     If the top card is null, the reference card is the bottom card,
                     so under the bottom card there is nothing to print
                    */
                    PlaceableCard card = cell.getTopCard();
                    boolean isTopCard = true;
                    if (card == null) {
                        card = cell.getBottomCard();
                        isTopCard = false;
                    }

                    Integer relativePosition = findRelativeCellPosition(cell);
                    String fillColor = reignColor(card.getReign());

                    switch (relativePosition) {
                        case 0 -> {
                            // The cell is the top left corner of the card
                            // So all the printed space is of the same card
                            lines.set(0, lines.get(0) + resourceToPrint(cell) + fillColor + String.format("%4s", "") + RESET);
                            if (card.isFront())
                                lines.set(1, lines.get(1) + fillColor + String.format("%1s%2s%1s%3s", "", card.getID(), "", "") + RESET);
                            else
                                lines.set(1, lines.get(1) + fillColor + String.format("%1s%2s%1s%3s", "", card.getID(), "", reignToPrint(card)) + RESET);
                        }
                        case 1 -> {
                            // The cell is the top right corner of the card
                            // So the top-right part to be printed is not of the same card
                            lines.set(0, lines.get(0) + resourceToPrint(cell) + otherCardColor(cell, isTopCard) + String.format("%4s", "") + RESET);
                            lines.set(1, lines.get(1) + fillColor + String.format("%3s", "") + BLACK_BACKGROUND + String.format("%4s", "") + RESET);
                        }
                        case 2 -> {
                            // The cell is the bottom left corner of the card
                            // So the bottom-left part to be printed is not of the same card
                            lines.set(0, lines.get(0) + resourceToPrint(cell) + fillColor + String.format("%4s", "") + RESET);
                            lines.set(1, lines.get(1) + otherCardColor(cell, isTopCard) + String.format("%3s", "") + BLACK_BACKGROUND + String.format("%4s", "") + RESET);
                        }
                        case 3 -> {
                            // The cell is the bottom right corner of the card
                            // So only the top right part is of the same card
                            lines.set(0, lines.get(0) + resourceToPrint(cell) + otherCardColor(cell, isTopCard) + String.format("%4s", "") + RESET);
                            if (isAvailablePosition(i, j, availablePositions))
                                lines.set(1, lines.get(1) + printAvailablePosition(i, j));
                            else
                                lines.set(1, lines.get(1) + otherCardColor(cell, isTopCard) + String.format("%7s", "") + RESET);
                        }
                    }
                } else if (isAvailablePosition(i, j, availablePositions)) {
                    // The cell is an available position
                    lines.set(0, lines.get(0) + BLACK_BACKGROUND + String.format("%7s", "") + RESET);
                    lines.set(1, lines.get(1) + printAvailablePosition(i, j));

                } else {
                    // The cell doesn't exist, so it is all black
                    for (int k = 0; k < 2; k++) {
                        lines.set(k, lines.get(k) + BLACK_BACKGROUND + String.format("%7s", "") + RESET);
                    }
                }
            }

            for (String line : lines) {
                System.out.println(line);
            }
        }

        System.out.println("\n" + YELLOW_TEXT + "The available positions [row, column] are on the graph." + RESET);

        System.out.println("\n");
    }

    /**
     * Checks if a position is in a list of available positions
     *
     * @param row                the row of the position
     * @param column             the column of the position
     * @param availablePositions the list of available positions
     * @return true if the position is available, false otherwise
     */
    private boolean isAvailablePosition(int row, int column, ArrayList<Integer[]> availablePositions) {
        for (Integer[] position : availablePositions) {
            if (position[0] == row && position[1] == column) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds a cell in a list of cells
     *
     * @param row    the row of the cell
     * @param column the column of the cell
     * @param cells  the list of cells
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
     *
     * @param cell the cell to find
     * @return the relative position of the cell in the card
     */
    private Integer findRelativeCellPosition(Cell cell) {
        if (cell == null)
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
     * Prints a label for the available positions
     *
     * @param row    the row of the position
     * @param column the column of the position
     * @return the string to be printed
     */
    private String printAvailablePosition(int row, int column) {
        return BLACK_BACKGROUND + String.format("[%2d,%2d]", row, column) + RESET;
    }

    /**
     * Prints the color of the bottom card of a cell if it exists
     *
     * @param cell      the cell to be printed
     * @param isTopCard if the cell is the top card
     * @return the string to be printed
     */
    private String otherCardColor(Cell cell, boolean isTopCard) {

        if (!isTopCard) {
            return BLACK_BACKGROUND;
        }
        PlaceableCard bottomCard = cell.getBottomCard();
        return reignColor(bottomCard.getReign());
    }

    /**
     * Prints two colored empty spaces
     *
     * @param res the reign of the card
     * @return the string to be printed
     */
    private String reignColor(Reign res) {
        String color;
        switch (res) {
            case Fungus -> color = RED_BACKGROUND;

            case Insect -> color = PURPLE_BACKGROUND;
            case Animal -> color = CYAN_BACKGROUND;
            case Plant -> color = GREEN_BACKGROUND;
            case null, default -> color = YELLOW_BACKGROUND;

        }
        return color;
    }


    /**
     * Prints the current state of the player hand
     *
     * @param currentPlayer the player whose hand is to be printed
     */
    protected void showPlayerHand(Player currentPlayer, String viewer) {
        //PlayerHand hand = message.getCurrentPlayer().getPlayerHand();
        PlayerHand hand = currentPlayer.getPlayerHand();
        String index = "";
        ArrayList<String> output = new ArrayList<>();
        output.add("");
        output.add("");
        output.add("");
        index += " (0)  " + "     ";
        index += " (1)  " + "     ";
        index += " (2)  " + "     ";
        System.out.println("\nPLAYER HAND: ");
        if (currentPlayer.getNickname().equals(viewer)) {
            hand.getPlaceableCards().forEach((card) -> this.printCard(output, card));


            System.out.println(index);
            System.out.println(output.get(0));
            System.out.println(output.get(1));
            System.out.println(output.get(2));
            int i = 0;
            for (PlaceableCard card : hand.getPlaceableCards()) {
                this.printRequirementCard(card, i);
                i += 1;
            }
            System.out.println("\n");

        } else {
            for (PlaceableCard card : hand.getPlaceableCards()) {
                this.printDeck(output, card);
            }

            System.out.println(index);
            System.out.println(output.get(0));
            System.out.println(output.get(1));
            System.out.println(output.get(2));
            System.out.println("\n");

        }
    }


    /**
     * Prints the requirements of a card
     *
     * @param card the card to be printed
     * @param i    the index of the card
     */
    private void printRequirementCard(PlaceableCard card, int i) {
        if (card.getPoints() > 0) {
            System.out.print("CARD (" + i + ") - ");
            if (card.getPoints() > 0)
                System.out.print("Points: " + card.getPoints());
            if (card.isGold())
                System.out.print(" - Requirements: " + card.getRequirement());
            System.out.println();
        }

    }

    /**
     * Prints the objectives of the game
     *
     * @param i      the index of the objective
     * @param output ArrayList<String>
     */
    protected void printObjective(int i, ArrayList<String> output) {
        output.set(0, output.get(0) + " ID: " + String.format("%2d", i) + " ");
        output.set(1, output.get(1) + " ");
        output.set(2, output.get(2) + " ");
        output.set(3, output.get(3) + " ");
        output.set(4, output.get(4) + " ");
        output.set(5, output.get(5) + " ");
        output.set(6, output.get(6) + " ");
        output.set(7, output.get(7) + " ");
        switch (i) {
            case 87 -> outputSet87_89(output, RED_BACKGROUND);

            case 88 -> outputSet88_90(output, GREEN_BACKGROUND);

            case 89 -> outputSet87_89(output, CYAN_BACKGROUND);

            case 90 -> outputSet88_90(output, PURPLE_BACKGROUND);

            case 91 -> {
                output.set(1, output.get(1) + " " + String.format("%6s", ""));
                output.set(2, output.get(2) + " " + RED_BACKGROUND + String.format("%3s", "") + RESET + String.format("%3s", ""));
                output.set(3, output.get(3) + " " + RED_BACKGROUND + String.format("%3s", "") + RESET + String.format("%3s", ""));
                outputSet91_94(output, GREEN_BACKGROUND, RED_BACKGROUND);
            }
            case 92 -> {
                output.set(1, output.get(1) + " " + String.format("%6s", ""));
                output.set(2, output.get(2) + " " + String.format("%2s", "") + GREEN_BACKGROUND + String.format("%3s", "") + RESET + " ");
                output.set(3, output.get(3) + " " + String.format("%2s", "") + GREEN_BACKGROUND + String.format("%3s", "") + RESET + " ");
                outputSet92_93(output, PURPLE_BACKGROUND, GREEN_BACKGROUND);
            }
            case 93 -> {
                output.set(1, output.get(1) + " " + String.format("%6s", ""));
                output.set(2, output.get(2) + " " + String.format("%2s", "") + RED_BACKGROUND + String.format("%3s", "") + RESET + " ");
                output.set(3, output.get(3) + " " + CYAN_BACKGROUND + String.format("%3s", "") + RESET + String.format("%2s", "") + " ");
                outputSet92_93(output, CYAN_BACKGROUND, CYAN_BACKGROUND);
            }
            case 94 -> {
                output.set(1, output.get(1) + " " + String.format("%6s", ""));
                output.set(2, output.get(2) + " " + CYAN_BACKGROUND + String.format("%3s", "") + RESET + String.format("%3s", ""));
                output.set(3, output.get(3) + " " + String.format("%2s", "") + PURPLE_BACKGROUND + String.format("%3s", "") + RESET + " ");
                outputSet91_94(output, PURPLE_BACKGROUND, PURPLE_BACKGROUND);
            }

            case 95 -> {
                output.set(1, output.get(1) + String.format("%7s", ""));
                output.set(2, output.get(2) + RED_BACKGROUND + String.format("%7s", "x3") + RESET);
                output.set(3, output.get(3) + RED_BACKGROUND + String.format("%7s", "FUNGUS") + RESET);
                outputSet95_96_96_97_98_100_101_102(output, RED_BACKGROUND);
            }
            case 96 -> {
                output.set(1, output.get(1) + String.format("%7s", ""));
                output.set(2, output.get(2) + GREEN_BACKGROUND + String.format("%7s", "x3") + RESET);
                output.set(3, output.get(3) + GREEN_BACKGROUND + String.format("%7s", "PLANT") + RESET);
                outputSet95_96_96_97_98_100_101_102(output, GREEN_BACKGROUND);
            }
            case 97 -> {
                output.set(1, output.get(1) + String.format("%7s", ""));
                output.set(2, output.get(2) + CYAN_BACKGROUND + String.format("%7s", "x3") + RESET);
                output.set(3, output.get(3) + CYAN_BACKGROUND + String.format("%7s", "ANIMAL") + RESET);
                outputSet95_96_96_97_98_100_101_102(output, CYAN_BACKGROUND);
            }
            case 98 -> {
                output.set(1, output.get(1) + String.format("%7s", ""));
                output.set(2, output.get(2) + PURPLE_BACKGROUND + String.format("%7s", "x3") + RESET);
                output.set(3, output.get(3) + PURPLE_BACKGROUND + String.format("%7s", "INSECT") + RESET);
                outputSet95_96_96_97_98_100_101_102(output, PURPLE_BACKGROUND);
            }
            case 99 -> {
                output.set(1, output.get(1) + String.format("%7s", ""));
                output.set(2, output.get(2) + YELLOW_BACKGROUND + String.format("%7s", "x1 QUI") + RESET);
                output.set(3, output.get(3) + YELLOW_BACKGROUND + String.format("%7s", "x1 INK") + RESET);
                output.set(4, output.get(4) + YELLOW_BACKGROUND + String.format("%7s", "x1 MAN") + RESET);
                output.set(5, output.get(5) + String.format("%7s", ""));
                output.set(6, output.get(6) + YELLOW_BACKGROUND + String.format("%7s", "+3 ") + RESET);
                output.set(7, output.get(7) + YELLOW_BACKGROUND + String.format("%7s", "Points") + RESET);
            }
            case 100 -> {
                output.set(1, output.get(1) + String.format("%7s", ""));
                output.set(2, output.get(2) + YELLOW_BACKGROUND + String.format("%7s", "x2 MAN") + RESET);
                output.set(3, output.get(3) + String.format("%7s", ""));
                outputSet95_96_96_97_98_100_101_102(output, YELLOW_BACKGROUND);
            }
            case 101 -> {
                output.set(1, output.get(1) + String.format("%7s", ""));
                output.set(2, output.get(2) + YELLOW_BACKGROUND + String.format("%7s", "x2 INK") + RESET);
                output.set(3, output.get(3) + String.format("%7s", ""));
                outputSet95_96_96_97_98_100_101_102(output, YELLOW_BACKGROUND);
            }
            case 102 -> {
                output.set(1, output.get(1) + String.format("%7s", ""));
                output.set(2, output.get(2) + YELLOW_BACKGROUND + String.format("%7s", "x2 QUI") + RESET);
                output.set(3, output.get(3) + String.format("%7s", ""));
                outputSet95_96_96_97_98_100_101_102(output, YELLOW_BACKGROUND);

            }
        }

    }

    /**
     * @param output        ArrayList<String>
     * @param redBackground String
     */
    private void outputSet95_96_96_97_98_100_101_102(ArrayList<String> output, String redBackground) {
        output.set(4, output.get(4) + String.format("%7s", ""));
        output.set(5, output.get(5) + String.format("%7s", ""));
        output.set(6, output.get(6) + redBackground + String.format("%7s", "+2 ") + RESET);
        output.set(7, output.get(7) + redBackground + String.format("%7s", "Points") + RESET);
    }

    /**
     * @param output          ArrayList<String>
     * @param greenBackground String
     */
    private void outputSet92_93(ArrayList<String> output, String purpleBackground, String greenBackground) {
        output.set(4, output.get(4) + " " + purpleBackground + String.format("%3s", "") + RESET + String.format("%2s", "") + " ");
        output.set(5, output.get(5) + " " + String.format("%6s", ""));
        output.set(6, output.get(6) + greenBackground + String.format("%7s", "+3 ") + RESET);
        output.set(7, output.get(7) + greenBackground + String.format("%7s", "Points") + RESET);
    }

    /**
     * @param output          ArrayList<String>
     * @param greenBackground String
     * @param redBackground   String
     */
    private void outputSet91_94(ArrayList<String> output, String greenBackground, String redBackground) {
        output.set(4, output.get(4) + " " + String.format("%2s", "") + greenBackground + String.format("%3s", "") + RESET + " ");
        output.set(5, output.get(5) + " " + String.format("%6s", ""));
        output.set(6, output.get(6) + redBackground + String.format("%7s", "+3 ") + RESET);
        output.set(7, output.get(7) + redBackground + String.format("%7s", "Points") + RESET);
    }

    /**
     * @param output          ArrayList<String>
     * @param greenBackground String
     */
    private void outputSet88_90(ArrayList<String> output, String greenBackground) {
        output.set(1, output.get(1) + String.format("%7s", ""));
        output.set(2, output.get(2) + greenBackground + String.format("%3s", "") + RESET + String.format("%4s", ""));
        output.set(3, output.get(3) + String.format("%2s", "") + greenBackground + String.format("%3s", "") + RESET + String.format("%2s", ""));
        output.set(4, output.get(4) + String.format("%4s", "") + greenBackground + String.format("%3s", "") + RESET);
        output.set(5, output.get(5) + String.format("%7s", ""));
        output.set(6, output.get(6) + greenBackground + String.format("%7s", "+2 ") + RESET);
        output.set(7, output.get(7) + greenBackground + String.format("%7s", "Points") + RESET);
    }

    /**
     * @param output        ArrayList<String>
     * @param redBackground String
     */
    private void outputSet87_89(ArrayList<String> output, String redBackground) {
        output.set(1, output.get(1) + String.format("%7s", ""));
        output.set(2, output.get(2) + String.format("%4s", "") + redBackground + String.format("%3s", "") + RESET);
        output.set(3, output.get(3) + String.format("%2s", "") + redBackground + String.format("%3s", "") + RESET + String.format("%2s", ""));
        output.set(4, output.get(4) + redBackground + String.format("%3s", "") + RESET + String.format("%4s", ""));
        output.set(5, output.get(5) + String.format("%7s", ""));
        output.set(6, output.get(6) + redBackground + String.format("%7s", "+2 ") + RESET);
        output.set(7, output.get(7) + redBackground + String.format("%7s", "Points") + RESET);
    }

    /**
     * This method allow the user to say if he wants to connect with socket or rmi
     *
     * @return boolean
     */
    public boolean askSocket() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Do you want to use Socket or RMI?");
            String choice = scanner.nextLine().toLowerCase();
            if (choice.equals("socket")) return true;
            else if (choice.equals("rmi")) return false;
        }
    }

    /**
     * This method allow the client to visualize the server response about the connection
     *
     * @param message the message received from the server
     */
    public void answerToConnection(connectionResponseMessage message) {
        if (message.getCorrect())
            System.out.println("the connection has been established");
    }

    /**
     * This method allow the client to choose if he wants to create a new match, join an existing one in waiting,
     * load a saved Match or join a running match
     *
     * @param message the message received from the server
     * @return message with the values chosen by the user
     */
    public serverOptionMessage serverOptions(serverOptionMessage message) {
        Scanner scanner = new Scanner(System.in);
        boolean newMatch;
        Integer matchID = null;
        Integer startedMatchID = null;
        boolean loadMatch = false;
        Integer savedMatchID = null;

        // Asks the user if he wants to join a new match
        while (true) {
            System.out.print("Do you want to join a new match? [YES/no] ");
            String choice = scanner.nextLine().toLowerCase();
            if (choice.equals("yes") || choice.equals("y") || choice.equals("ye")) {
                newMatch = true;
            } else if (choice.equals("no") || choice.equals("n")) {
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
            if (choice.equals("2")) {
                if (message.getWaitingMatches().isEmpty()) {
                    System.out.println("There are no matches to join: creating a new match");
                    matchID = null;
                    break;
                } else {
                    System.out.println("Here are the matches you can join: ");
                    int i = 1;
                    for (Integer id : message.getWaitingMatches()) {
                        System.out.println("(" + i + ") " + id);
                        i += 1;
                    }
                    System.out.print("Enter the match number: ");
                    try {
                        matchID = scanner.nextInt();
                        scanner.nextLine();
                        if (matchID < 1 || matchID > i) {
                            System.out.println("ANSWER NOT VALID");
                            continue;
                        }
                        matchID = message.getWaitingMatches().get(matchID - 1);
                        break;
                    } catch (Exception e) {
                        scanner.nextLine();
                        System.out.println("ANSWER NOT VALID");
                    }
                }

            } else if (choice.equals("1")) {
                matchID = null;
                break;
            } else {
                System.out.println("ANSWER NOT VALID");
            }
        }


        if (!newMatch) {

            boolean runMatch;
            while (true) {
                System.out.print("Join a running match? [YES/no] ");
                String choice = scanner.nextLine().toLowerCase();
                if (choice.equals("yes") || choice.equals("y")) {
                    runMatch = true;
                } else if (choice.equals("no") || choice.equals("n")) {
                    runMatch = false;
                } else {
                    System.out.println("ANSWER NOT VALID");
                    continue;
                }
                break;

            }

            if (runMatch) {

                // Asks the user which running match he wants to join
                while (true) {
                    if (message.getRunningMatches().isEmpty()) {
                        System.out.println("There are no matches to join.");
                        startedMatchID = null;
                        break;
                    } else {
                        System.out.println("Here are the matches you can join: ");
                        int i = 1;
                        for (Integer id : message.getRunningMatches()) {
                            System.out.println("(" + i + ") " + id);
                            i += 1;
                        }
                        System.out.print("Enter the match number: ");
                        try {
                            startedMatchID = scanner.nextInt();
                            scanner.nextLine();

                            if (startedMatchID < 1 || startedMatchID > i) {
                                System.out.println("ANSWER NOT VALID");
                                continue;
                            }
                            startedMatchID = message.getRunningMatches().get(startedMatchID - 1);
                            break;
                        } catch (Exception e) {
                            scanner.nextLine();
                            System.out.println("You didn't enter an int value");
                        }
                    }
                }

            } else {
                while (true) {
                    System.out.print("Join a saved match? [YES/no] ");
                    String choice = scanner.nextLine().toLowerCase();
                    if (choice.equals("yes") || choice.equals("y")) {
                        loadMatch = true;
                    } else if (choice.equals("no") || choice.equals("n")) {
                        break;
                    } else {
                        System.out.println("ANSWER NOT VALID");
                        continue;
                    }
                    break;
                }

                if (loadMatch) {
                    // Asks the user which saved match he wants to join
                    while (true) {
                        if (message.getSavedMatches().isEmpty()) {
                            System.out.println("There are no matches to join.");
                            savedMatchID = null;
                            break;
                        } else {
                            System.out.println("Here are the matches you can join: ");
                            int i = 1;
                            for (Integer id : message.getSavedMatches()) {
                                System.out.println("(" + i + ") " + id);
                                i += 1;
                            }
                            System.out.print("Enter the saved match number: ");
                            try {
                                savedMatchID = scanner.nextInt();
                                scanner.nextLine();

                                if (savedMatchID < 1 || savedMatchID > i) {
                                    System.out.println("ANSWER NOT VALID");
                                    continue;
                                }

                                savedMatchID = message.getSavedMatches().get(savedMatchID - 1);
                                break;
                            } catch (Exception e) {
                                scanner.nextLine();
                                System.out.println("ANSWER NOT VALID");
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
     * This method shows which nicknames are not available and allows the user to choose his nickname
     *
     * @param message the message received from the server
     * @return the chosen nickname
     */
    public String unavailableNames(unavailableNamesMessage message) {
        //the client can call the method view.unavailableNames passing as a parameter the arraylist of unavailable names received from server
        if (!message.toString().equals("[]")) {
            System.out.println("This nicknames are not available: " + message);
        } else {
            System.out.println("All nicknames are available");
        }

        String name;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a nickname:");
        name = scanner.nextLine();
        return name;
    }

    /**
     * This method visualizes the response received from the server about the value entered by the user
     *
     * @param message the message received from the server
     */
    public void answer(responseMessage message) {
        if (!message.getCorrect())
            System.out.println("You didn't entered a valid value, please try again");
    }

    /**
     * This method lets the user know that he has to wait for other players to join the game
     */
    public void waiting() {
        System.out.println(PURPLE_TEXT + "Waiting for" + BLUE_TEXT + " other players" + GREEN_TEXT + " to join" + RED_TEXT + " the game...");
    }


    /**
     * This method shows what colors are available and allows the user to choose his color
     *
     * @param message the message received from the server
     */
    public String availableColors(availableColorsMessage message) {
        //the client can call the method view.availableColors passing as a parameter the arraylist of available colors received from server
        System.out.println("This are the colors that are available: " + message.toString());

        String color;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a color:");
        color = scanner.nextLine();
        return color;
    }

    /**
     * This method allow the user to place his starter card
     *
     * @return the side of the card
     */
    public int placeStarter() {
        System.out.println("Place your STARTER card on the table");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("FRONT or BACK? ");
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
     * This method allows the user to choose how many players will play
     *
     * @return number of expected players
     */
    public int expectedPlayers() {
        int numExpected;
        Scanner scanner = new Scanner(System.in);
        while (true) {

            try {
                System.out.println("How many player do you want to be in the game");
                numExpected = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("You didn't enter an integer value");
            }
        }
        return numExpected;

    }

    /**
     * This method allows the user to choose his secret objective
     *
     * @return int representing the chosen objective
     */
    public int chooseObjective(ArrayList<ObjectiveCard> objectives) {
        int objective;
        System.out.println("You have to choose your personal objective");

        for (int i = 0; i < objectives.size(); i++) {
            System.out.println("(" + (i + 1) + ") Objective  ID: " + objectives.get(i).getID());
        }

        System.out.println("Choose your personal objective.");
        ArrayList<String> output = new ArrayList<>();
        addOutput(output);
        this.printObjective(objectives.get(0).getID(), output);
        this.printObjective(objectives.get(1).getID(), output);
        printOutput(output);

        System.out.print("FIRST (1) or SECOND (2) ?  ");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                objective = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("You didn't enter an integer value");
            }
        }
        return objective;
    }

    /**
     * @param output ArrayList<String>
     */
    private void printOutput(ArrayList<String> output) {
        System.out.println(output.get(0));
        System.out.println(output.get(1));
        System.out.println(output.get(2));
        System.out.println(output.get(3));
        System.out.println(output.get(4));
        System.out.println(output.get(5));
        System.out.println(output.get(6));
        System.out.println(output.get(7));
    }

    /**
     * This method allows the user to say what card he wants to play, front or back, and in which position
     *
     * @return Array of int representing the card chosen by the user, side, and position
     */
    public int[] placeCard() {
        int[] chosenCard = new int[4];
        chosenCard[0] = 1000;
        chosenCard[1] = 1000;
        chosenCard[2] = 1000;
        chosenCard[3] = 1000;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Enter the NUMBER of the card you want to place: ");
                chosenCard[0] = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("You didn't enter an integer value");
            }
        }

        boolean correct = false;
        while (!correct) {
            System.out.print("FRONT or BACK?");
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


        while (true) {
            try {
                System.out.print("ROW: ");
                chosenCard[2] = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("You didn't enter an integer value");
            }
        }


        while (true) {
            try {
                System.out.print("COLUMN: ");
                chosenCard[3] = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("You didn't enter an integer value");
            }
        }

        return chosenCard;
    }

    /**
     * This method allows the user to say what card he wants to pick
     *
     * @return int representing the card the user wants to pick
     */
    public int pickCard() {
        int choice = 1000;
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("enter the NUMBER of the card you want to pick: ");
            choice = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("you didn't enter an integer value");
        }
        return choice;
    }


    /**
     * This method visualizes the final information about the game, points and number of objectives achieved by the players
     *
     * @param message the message received from the server
     */
    public void endGame(declareWinnerMessage message) {
        HashMap<String, Integer> points;
        HashMap<String, Integer> numObjectives;

        points = message.getPlayersPoints();
        numObjectives = message.getNumberOfObjects();

        System.out.println("\n\nSCOREBOARD\n");
        for (String playerName : points.keySet()) {
            Integer playerPoints = points.get(playerName);
            Integer playerObjectives = numObjectives.get(playerName);
            System.out.println("Player Name: " + playerName + "  -  Points: " + playerPoints + "  -  Number of Objectives: " + playerObjectives);
        }
    }

    /**
     * This method visualizes the updated state of the current player
     *
     * @param update the message received from the server
     */
    @Override
    public void update(updatePlayerMessage update) {
        Player player = update.getPlayer();
        this.showCommonArea(player.getCommonArea());
        this.showPlayerArea(player.getPlayerArea());
        this.showPlayerHand(player, update.getNicknameViewer());
    }


    /**
     * This method allows the user to choose a nickname from the list of available names
     *
     * @param message the message containing the list of available names
     * @return the chosen nickname
     */
    @Override
    public String pickNameFA(unavailableNamesMessage message) {
        System.out.println("Please choose a nickname: ");
        int i = 1;
        for (String name : message.getNames()) {
            System.out.println("(" + i + ") " + name);
            i += 1;
        }
        while (true) {
            Scanner scanner = new Scanner(System.in);
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice > 0 && choice <= message.getNames().size()) {
                    return message.getNames().get(choice - 1);
                } else {
                    System.out.println("Please enter a valid number");
                }
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("Please enter a valid number");
            }
        }

    }
}
