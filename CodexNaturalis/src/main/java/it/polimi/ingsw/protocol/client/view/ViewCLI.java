package it.polimi.ingsw.protocol.client.view;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.CommonArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerArea;
import it.polimi.ingsw.model.cards.Cell;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.cards.PlayerHand;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.*;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.messages.responseMessage;
import it.polimi.ingsw.protocol.server.PlayerInfo;


import java.util.*;
import java.util.stream.Collectors;

public class ViewCLI extends View {
    //the purpose of ViewCLI is to handle the interaction with the user and
    // visualize what he needs to see in order to play the game
    // by printing it in the command line
    public ViewCLI() {
        super();

    }

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
    public void showCommonArea(CommonArea area) {
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
    public void showPlayerArea(PlayerArea area) {
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

        for(int i = minRow; i <= maxRow; i++) {
            for(int j = minColumn; j <= maxColumn; j++) {
                Cell cell = findCell(i, j, Cells);
                if(cell != null) {
                    PlaceableCard card = cell.getTopCard();
                    if(card != null) {
                        System.out.print(" " + String.format("%02d", card.getID()) + (cell.getTopCard().isFront() ? "F" : "B") + "/");
                    } else {
                        System.out.print(" " + "   " + "/");
                    }

                    PlaceableCard bottomCard = cell.getBottomCard();
                    if(bottomCard != null) {
                        System.out.print(String.format("%02d", bottomCard.getID()) + (cell.getBottomCard().isFront() ? "F" : "B"));
                    } else {
                        System.out.print("   ");
                    }
                } else {
                    System.out.print("        ");
                }
            }
            System.out.println(" ");
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
     * Prints the current state of the player hand
     * @param currentPlayer the player whose hand is to be printed
     */
    public void showPlayerHand(Player currentPlayer, String viewer) {
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
        Integer startedMatchID = null;
        String nickname = null;
        boolean loadMatch = false;
        String pathToLoad = null;
        while(true) {
            try {
                System.out.println("Enter 'true' if this is a new match or 'false' if it is not");
                newMatch = scanner.nextBoolean();
                scanner.nextLine();
                break;
            } catch (Exception e) {
                System.out.println("You didn't enter a boolean");
            }
        }

        if(!newMatch) {
            boolean runMatch = false;
            while (true)  {
                try {
                    System.out.println("Enter 'true' if you want to join an already started match or 'false' if it is not");
                    runMatch = scanner.nextBoolean();
                    scanner.nextLine();
                    break;
                } catch (Exception e) {
                    System.out.println("You didn't enter a boolean");
                }
            }


            if(runMatch) {
                try {
                    System.out.println("Enter the started match ID");
                    startedMatchID = scanner.nextInt();
                    scanner.nextLine();
                } catch (Exception e) {
                    System.out.println("You didn't enter an int value");
                }

                System.out.println("Enter your nickname");
                nickname = scanner.nextLine();
            } else {
                try {
                    System.out.println("Enter true if you want to load the match");
                    loadMatch = scanner.nextBoolean();
                    scanner.nextLine();
                } catch (Exception e) {
                    System.out.println("You didn't enter a boolean");
                }
                System.out.println("Enter the path to load");
                pathToLoad = scanner.nextLine();
            }

        }

        message = new serverOptionMessage(newMatch, startedMatchID, nickname, loadMatch, pathToLoad);
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
