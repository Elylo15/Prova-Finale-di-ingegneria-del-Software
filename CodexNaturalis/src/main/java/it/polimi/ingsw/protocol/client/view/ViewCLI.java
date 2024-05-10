package it.polimi.ingsw.protocol.client.view;

import it.polimi.ingsw.model.PlayerArea;
import it.polimi.ingsw.model.cards.Cell;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.*;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.messages.responseMessage;


import java.util.*;

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
        System.out.println("An error occurred, connection interrupted");
    }

    /**
     * visualize the player area of the player printing a matrix. Every position of the matrix has the id of
     * its top card followed by F if it is front placed or B if it is back placed / the id of the bottom card
     * followed by F or B  or null if there is no card
     *
     * @param playerArea
     * @return void
     */
    public String[][] showPlayerArea(PlayerArea playerArea) {

        ArrayList<PlaceableCard> cards = playerArea.getAllCards();
        ArrayList<Cell> position = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            for (int j = 0; j < 4; j++) {   //every card possesses four cells
                position.add(cards.get(i).getCells().get(j));
            }
        }

        int minRow = position.stream()
                .mapToInt(Cell::getRow).min().orElse(0);
        int maxRow = position.stream().mapToInt(Cell::getRow).max().orElse(0);

        int minColumn = position.stream()
                .mapToInt(Cell::getColumn).min().orElse(0);
        int maxColumn = position.stream()
                .mapToInt(Cell::getColumn).max().orElse(0);

        //now we have size of the matrix
        int sizeRow = maxRow - minRow;
        int sizeColumn = maxColumn - minColumn;
        String[][] showArea = new String[sizeRow+1][sizeColumn+1];

       // String[][] showArea = new String[maxRow+1][maxColumn+1];  //gli indici della matrice partono da 0 e arrivano a alle coordinate massime
        String TopFront = null;
        String BottomFront = null;
        Integer TopID = null; //initialize the top card id
        Integer BottomID = null; //initialize the bottom card id

        for (int i = 0; i < sizeRow+1; i++) {  //scorro ogni cella della matrice da ritornare
            for (int j = 0; j < sizeColumn+1; j++) {
                for (PlaceableCard card : cards) { //per ogni carta
                    for (int p = 0; p < 4; p++) {   //per ogni cella della carta

                        if (i == module(minRow - card.getCells().get(p).getRow()) && j == module(minColumn - card.getCells().get(p).getColumn())) {
                            if(card.getCells().get(p).getBottomCard()==card){
                                BottomID = card.getID();
                                if(card.isFront()){
                                    BottomFront = "F";
                                }
                                else {
                                    BottomFront = "B";
                                }
                            }
                            if(card.getCells().get(p).getTopCard()==card){
                                TopID = card.getID();
                                if(card.isFront()){
                                    TopFront = "F";
                                }
                                else {
                                    TopFront = "B";
                                }
                            }
                            if(TopID!=BottomID) {
                                showArea[i][j] = TopID + TopFront + "/" + BottomID + BottomFront;
                            }
                            if(TopID ==BottomID){
                                showArea[i][j] = TopID + TopFront + "/" + null + null;
                            }
                            if(TopID==null){
                                showArea[i][j] = BottomID + BottomFront + "/" + null + null;
                            }
                            if(BottomID==null){
                                showArea[i][j] = TopID + TopFront + "/" + null + null;
                            }
                        }
                    }
                }
                }
            }

        this.printMatrix(showArea,sizeRow+1,sizeColumn+1);

        return showArea;


    }

    /**
     * method for printing the matrix that visualize the player area
     * @param matrix
     * @param r
     * @param c
     */
    public void printMatrix(String[][] matrix, int r, int c) {
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                System.out.print(matrix[i][j] + " "); //print per non andare a capo dopo ogni valore
            }
            System.out.println("\n"); //staccare le righe
        }
    }
    public int module(int a){
        if(a>0){
            return a;
        }
        else {
            return  -a;
        }
    }


        /**
         * visualizes the current state of the players
         * @param message
         */
        public void updatePlayer (currentStateMessage message){
            System.out.println("player " + message.getPlayer() + "is in the state " + message.getStateName());
            if (message.isLastTurn()) {
                System.out.println("this is the last turn");
            }
        }


        /**
         * this method allow the user to say if he wants to connect with socket or rmi
         * @return boolean
         */
        public boolean askSocket () {
            //if the user doesn't enter a boolean, an exception is thrown and the method return default value true
            Scanner scanner = new Scanner(System.in);
            boolean socket = true;
            try {
                System.out.println("enter true if yo want to connect with socket, enter false if you want to connect with rmi");
                socket = scanner.nextBoolean();
            } catch (Exception e) {
                System.out.println("you didn't enter a boolean");
            }

            return socket;
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
            try {
                System.out.println("Enter 'true' if this is a new match or 'false' if it is not");
                newMatch = scanner.nextBoolean();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("You didn't enter a boolean");
            }

            if(!newMatch) {
                boolean runMatch = false;
                try {
                    System.out.println("Enter 'true' if you want to join an already started match or 'false' if it is not");
                    runMatch = scanner.nextBoolean();
                    scanner.nextLine();
                } catch (Exception e) {
                    System.out.println("You didn't enter a boolean");
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
            System.out.println("You need to choose a nickname to play the game");
            name = scanner.nextLine();
            return name;
        }

        /**
         * visualize the response about the value entered
         * @param message
         */
        public void answer (responseMessage message){
            if (message.getCorrect()) {
                System.out.println("You have entered a valid value");
            } else {
                System.out.println("You didn't entered a valid value, please try again");
            }
        }


        /**
         * this method shows what colors are available and allows the user to choose his color
         * @param message
         */
        public String availableColors (availableColorsMessage message){
            //the client can call the method view.avaibleColors passing as a parameter the arraylist of available colors received from server
            System.out.println("This are the colors that are available: " + message.toString());

            String color;
            Scanner scanner = new Scanner(System.in);
            System.out.println("You need to choose a color, please enter a valid color to play");
            color = scanner.nextLine();
            return color;
        }

        /**
         *this method allow the user to place his starter card
         * @return int
         */
        public int placeStarter () {
            //if the user does not enter an int an exception is thrown and the method return the initialized value
            int side = 1000;
            System.out.println("You have to place your starter");
            System.out.println("Enter 1 if you want to play the front side, 0 if you want to play the back side");
            try {
                Scanner scanner = new Scanner(System.in);
                side = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("You didn't enter an integer value");
            }
            return side;
        }


        /**
         * allow the user to choose how many players will play
         * @return number of expected players
         */
        public int expectedPlayers () {
            int numExpected = 0;
            Scanner scanner = new Scanner(System.in);
            try {
                System.out.println("how many player do you want to be in the game");
                numExpected = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("you didn't enter an integer value");
            }
            return numExpected;

        }

        /**
         * allow the user to choose his secret objective
         * @return objective
         */
        public int chooseObjective () {
            int objective = 1000;
            System.out.println("You have to choose your personal objective");
            System.out.println("Enter 1 if you want to choose the first one, 2 if you want to choose the second one");
            try {
                Scanner scanner = new Scanner(System.in);
                objective = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("You didn't enter an integer value");
            }
            return objective;
        }

        /**
         * allow the user to say what card he wants to play, front or back, and in which position
         * @return
         */
        public int[] placeCard () {
            int[] chosenCard = new int[4];
            //if the user does not enter integer values, the method will return an array of invalid values for placing the card
            chosenCard[0] = 1000;
            chosenCard[1] = 1000;
            chosenCard[2] = 1000;
            chosenCard[3] = 1000;
            Scanner scanner = new Scanner(System.in);
            try {
                System.out.println("enter the ID of the card you want to place");
                chosenCard[0] = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("you didn't enter an integer value");
            }
            try {
                System.out.println("enter 1 if you want to place the card front, 0 if you want to place the card back");
                chosenCard[1] = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("you didn't enter an integer value");
            }
            try {
                System.out.println("enter the x coordinate of the cell where you want to place the card");
                chosenCard[2] = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("you didn't enter an integer value");
            }
            try {
                System.out.println("enter the y coordinate of the cell where you want to place the card");
                chosenCard[3] = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("you didn't enter an integer value");
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
                System.out.println("enter the ID of the card you want to pick");
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
            System.out.println("the points of the players are: " + points);
            System.out.println("the players have achieved this number of objectives:  " + numObjectives);

        }

    @Override
    public void update(updatePlayerMessage update) {

    }


}
