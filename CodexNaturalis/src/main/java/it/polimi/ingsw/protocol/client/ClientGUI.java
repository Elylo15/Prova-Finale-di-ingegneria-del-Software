package it.polimi.ingsw.protocol.client;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.client.controller.Controller;
import it.polimi.ingsw.protocol.client.controller.ControllerRMI;
import it.polimi.ingsw.protocol.client.controller.ControllerSocket;
import it.polimi.ingsw.protocol.client.view.GUI.controller.SceneManager;
import it.polimi.ingsw.protocol.client.view.GUI.message.GUIMessages;
import it.polimi.ingsw.protocol.client.view.View;
import it.polimi.ingsw.protocol.client.view.ViewGUI;
import it.polimi.ingsw.protocol.messages.ConnectionState.availableColorsMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.connectionResponseMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.unavailableNamesMessage;
import it.polimi.ingsw.protocol.messages.EndGameState.declareWinnerMessage;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.updatePlayerMessage;
import it.polimi.ingsw.protocol.messages.ServerOptionState.serverOptionMessage;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.newHostMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.messages.responseMessage;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class ClientGUI extends Client implements Runnable{

    /**
     * method {@code Client}: constructs a new Client
     *
     * @param view: default ViewGUI
     */
    public ClientGUI(ViewGUI view) {
        super(view);
    }

    @Override
    public void run() {
        ((ViewGUI) getView()).startMain();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while(true) {
            try {
                String server = getView().askIP();
                setIP(server);

                boolean isSocket = getView().askSocket();
                setController(server, isSocket);
                connection(isSocket);
            } catch (Exception e) {
                System.out.println("\033[31mConnection failed.\033[0m");
                continue;
            }

            try {
                while (true) {

//                    // REMOVE THIS
//                    System.out.println("\n\033[41mWaiting for current state");


                    currentStateMessage current = getController().getCurrent();
                    String state = current.getStateName();

//                    // REMOVE THIS
//                    System.out.println("Current state: " + state + "\n\033[0m");

                    switch (state) {
                        case "ServerOptionState": {
                            serverOptions();
                            break;
                        }
                        case "ConnectionState": {
                            name();
                            color();
                            break;
                        }
                        case "WaitingForPlayerState": {
                            waitingPlayer(current);
                            break;
                        }
                        case "StarterCardState": {
                            getView().updatePlayer(current);
                            if (Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname()))
                                starter();
                            updatePlayerMessage update = getController().updatePlayer();
                            getView().update(update);
                            break;
                        }
                        case "ObjectiveState": {

                            getView().updatePlayer(current);
                            if (Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname()))
                                pickObjective();
                            updatePlayerMessage update = getController().updatePlayer();
                            getView().update(update);
                            break;
                        }
                        case "PlaceTurnState": {
                            getView().updatePlayer(current);
                            if (Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname()))
                                placeCard();
                            updatePlayerMessage update = getController().updatePlayer();
                            getView().update(update);
                            break;
                        }
                        case "PickTurnState": {
                            getView().updatePlayer(current);
                            if (Objects.equals(current.getCurrentPlayer().getNickname(), current.getPlayer().getNickname()))
                                pickCard();
                            updatePlayerMessage update = getController().updatePlayer();
                            getView().update(update);
                            break;
                        }
                        case "EndGameState": {
                            declareWinnerMessage end = getController().endGame();
                            getView().endGame(end);
                            throw new Exception("Game ended.");
                        }

                        case "ConnectionFAState": {
                            pickNameFA();
                            break;
                        }

                        case "AnswerCheckConnection": {
                            getController().sendAnswerToPing();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                getView().playerDisconnected();
            }
        }
    }

    /*
    public void showPlayerArea(PlayerArea area) {
        // Clear previous content
        playerAreaGrid.getChildren().clear();

        ArrayList<Cell> cells = area.getAllCards().stream()
                .map(PlaceableCard::getCells)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Integer[]> availablePositions = area.getAvailablePosition();

        // Calculate grid dimensions
        int rows = calculateGridRows(cells, availablePositions);
        int columns = calculateGridColumns(cells, availablePositions);

        // Initialize grid layout
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        // Add column numbers
        for (int i = 0; i < columns; i++) {
            Text columnNumber = new Text(String.valueOf(i));
            gridPane.add(columnNumber, i + 1, 0);
        }

        // Add row numbers
        for (int i = 0; i < rows; i++) {
            Text rowNumber = new Text(String.valueOf(i));
            gridPane.add(rowNumber, 0, i + 1);
        }

        // Add cards and available positions to the grid
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Cell cell = findCell(row, col, cells);
                if (cell != null) {
                    // Placeable card exists in this cell
                    PlaceableCard card = cell.getTopCard();
                    if (card != null) {
                        ImageView imageView = new ImageView(new Image(card.getImagePath()));
                        imageView.setFitWidth(100); // Set image width
                        imageView.setFitHeight(150); // Set image height

                        // Create a stack pane to allow overlaying multiple cards
                        StackPane stackPane = new StackPane();

                        // Add card image to the stack pane
                        stackPane.getChildren().add(imageView);

                        // Position the card image within the cell (adjust as needed)
                        stackPane.setAlignment(Pos.TOP_LEFT);

                        // Add the stack pane to the grid
                        gridPane.add(stackPane, col + 1, row + 1);
                    }
                } else if (isAvailablePosition(row, col, availablePositions)) {
                    // No card in this cell, but it's an available position
                    Rectangle rectangle = new Rectangle(100, 150); // Placeholder for available position
                    rectangle.setFill(Color.BLACK);

                    // Add rectangle to the grid
                    gridPane.add(rectangle, col + 1, row + 1);
                }
            }
        }

        // Add the grid to the JavaFX scene
        playerAreaGrid.getChildren().add(gridPane);
    }
    */

}
