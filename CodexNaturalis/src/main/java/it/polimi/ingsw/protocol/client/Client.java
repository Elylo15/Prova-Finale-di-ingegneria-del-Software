package it.polimi.ingsw.protocol.client;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.protocol.client.controller.*;
import it.polimi.ingsw.protocol.client.view.*;

import java.util.ArrayList;
import java.util.concurrent.*;

public class Client {
    private String serverIP;
    private String serverPort;
    private boolean isSocket;
    private boolean guiEnabled;
    private boolean isConnected;
    private final Controller controller;
    private final View view;

    public Client(View view, Controller controller) {
    this.view = view;
    this.controller = controller;
    }

    public void startGame() throws InterruptedException {
        connection();

        boolean isHost = controller.isHost(); //ask controller if first player connected, return true if it is
        if(isHost)
            view.displayYouAreHost();

        choseNickname();
        choseColor();

        if(isHost)
            startIfHost();

        playingFirstTurn();

        playingTurn();

        playingLastTurn();

    }

    private void playingLastTurn() throws InterruptedException {
        while(controller.lastTurnSignal()) {
            if (controller.yourLastTurnSignal()) {
                view.yourLastTurnStarts();
                playLastTurn();
            } else
                view.notYourTurn();
        }
        Thread.sleep(2000);
    }

    private void playingTurn() throws InterruptedException {
        while(!controller.yourLastTurnSignal()) {
            if (controller.yourTurnSignal()) {
                view.yourTurnStarts();
                playYourTurn();
            } else
                view.notYourTurn();
            
            Thread.sleep(2000);
        }
    }

    private void playingFirstTurn() throws InterruptedException {
        while(controller.firstTurnSignal()) {
            if (controller.yourFirstTurnSignal()) {
                view.yourFirstTurnStarts();
                playFirstTurn();
            } else
                view.notYourTurn();
            
            Thread.sleep(2000);
        }
    }

    private void connection(){
        setSocket(view.askSocket());
        enableGUI(view.askGui());

        controller.connectToServer(serverIP, serverPort);
        String connectionMessage = controller.sendAnswerToConnection();
        view.answerToConnection(connectionMessage);
        isConnected = true;
    }

    private void choseNickname(){
        String nickname;
        ArrayList<String> unavailableNickname;
        boolean validNickname;

        do {
            unavailableNickname = controller.unaviableNicknames();
            view.displayUnaviableNicknames(unavailableNickname);
            nickname = view.askNickname();
            controller.chooseNickname(nickname);
            validNickname = controller.nicknameConfirmation();

            if (!validNickname)
                view.nicknameError();

            validNickname = view.nicknameCorrect();

        } while (!validNickname);
    }

    public void choseColor(){
        String color;
        ArrayList<String> availableColor;
        boolean validColor;

        do {
            availableColor = controller.aviableColors();
            view.displayAviableColors(availableColor);
            color = view.askColor();
            controller.chooseColor(color);
            validColor = controller.colorConfirmation();

            if (!validColor)
                view.colorError();

            validColor = view.colorCorrect();

        } while (!validColor);
    }

    public void startIfHost(){
            boolean startMatch = false;

            do {
                boolean moreThanTwo = controller.enoughPlayers();
                boolean everyoneReady = controller.everyoneCorrectColor();
                if(moreThanTwo && everyoneReady) {
                    boolean readyStartMatch = waitForMatchStart(); //player chooses if he wants to start
                    startMatch = controller.startingMatch(readyStartMatch); //the controller checks if the match should start
                    //it returns false for example if another player connected, and will ask again the host
                }

            }  while (!startMatch);
    }

    private boolean waitForMatchStart() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            Future<Boolean> result = executor.submit(() -> view.displayStartMatch());
            return result.get(5, TimeUnit.MINUTES); // Wait for 5 minutes for user input
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return true;
        } finally {
            executor.shutdown();
        }
    }

    public void playFirstTurn() {

        boolean placedCorrectly;
        do {
            view.displayStarter();
            int side = view.getSideStarter();
            placedCorrectly = controller.placeStarter(side);
            view.answerToPlaceStarter();
        } while(!placedCorrectly);

        boolean pickedCorrectly;
        do {
            ArrayList<ObjectiveCard> objective = controller.getObjectives();
            int pick = view.pickObjective(objective);
            pickedCorrectly = controller.pickedObjective(pick);
            view.answerToPickObjective(pickedCorrectly);
        } while (!pickedCorrectly);

    }

    public void playYourTurn(){

        placeCard();

        boolean pickedCorrectly;
        do{
            view.displayOptionsDraw();
            int pick = view.getOptionDraw();
            pickedCorrectly = controller.pickCard(pick);
            view.answerToPickCard();
        } while (!pickedCorrectly);
    }

    public void playLastTurn(){
        placeCard();
    }

    private void placeCard() {
        boolean placedCorrectly;
        do {
            view.displayPlayerHand();
            int card = view.getCard();
            int side = view.getSide();
            String position = controller.getAvailablePosition();
            view.displayAvailablePosition(availablePosition);
            int x = view.getPosition();
            int y = view.getPosition();
            placedCorrectly = controller.placeCard(card, side, x, y);
            view.answerToPlaceStarter(placedCorrectly);
        } while(!placedCorrectly);
    }


    public void setIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public void setSocket(boolean isSocket) {
        this.isSocket = isSocket;
    }

    public void enableGUI(boolean guiEnabled) {
        this.guiEnabled = guiEnabled;
    }

    public String getIP() {
        return serverIP;
    }

    public String getPort() {
        return serverPort;
    }
}