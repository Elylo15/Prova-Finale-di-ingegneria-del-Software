package it.polimi.ingsw.protocol.client.controller;

import it.polimi.ingsw.protocol.messages.ConnectionState.availableColorsMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.chosenColorMessage;
import it.polimi.ingsw.protocol.messages.ConnectionState.connectionResponseMessage;
import it.polimi.ingsw.protocol.messages.currentStateMessage;
import it.polimi.ingsw.protocol.messages.responseMessage;
import it.polimi.ingsw.protocol.server.ClientSocket;
import it.polimi.ingsw.protocol.server.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ControllerSocketTest {
    ControllerSocket controller;

     //in order to see if the controller can correctly send and receive messages we create a server to exchange them
    private ServerSocket serverSocket;
    private Socket Ssocket;
    ObjectOutputStream serveroutput;
    ObjectInputStream serverinput;
    private ThreadPoolExecutor executor;
    ControllerSocket mycontroller;



    @BeforeEach
    void setUp() throws IOException {
        int corePoolSize = 15;
        int maximumPoolSize = 200;
        long keepAliveTime = 300;
        TimeUnit unit = TimeUnit.SECONDS;
        serverSocket = new ServerSocket(1024);
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());

        executor.submit(() -> {
            try {
                Ssocket = serverSocket.accept();
                serverinput = new ObjectInputStream(Ssocket.getInputStream());
                serveroutput = new ObjectOutputStream(Ssocket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        mycontroller = new ControllerSocket("localhost", "1024");
        mycontroller.connectToServer("localhost", "1024");
    }
    @AfterEach
    void tearDown() throws IOException {
         executor.shutdown();
    }

    @Test
    void connectToServer() {
    }

    @Test
    void answerConnection() {
        //controller should receive a connectionResponseMessage
        connectionResponseMessage message = new connectionResponseMessage(true);
        executor.submit(() -> {
            try {
                serveroutput.reset();
                serveroutput.writeObject(message);
                serveroutput.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        connectionResponseMessage messageToController = mycontroller.answerConnection();
        assertEquals(message, messageToController);

    }

    @Test
    void getCurrent() {
        //controller should receive a currentStateMessage

    }

    @Test
    void serverOptions() {
        //controller should receive an empty serverOptionMessage
    }

    @Test
    void sendOptions() {
        //controller should send a serverOptionMessage
    }

    @Test
    void correctAnswer() throws IOException {
        //the controller should correctly receive a responseMessage
        responseMessage message = new responseMessage(true);
        executor.submit(() -> {
            try {
                serveroutput.reset();
                serveroutput.writeObject(message);
                serveroutput.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        responseMessage messageToController = mycontroller.correctAnswer();
        System.out.println(message.getCorrect());
        System.out.println(messageToController.getCorrect());
        assertEquals(message.getCorrect(),messageToController.getCorrect());

    }

    @Test
    void getUnavailableName() {
    }

    @Test
    void chooseName() {
    }

    @Test
    void getAvailableColor() {
        //controller should receive an availableColorsMessage
        ArrayList<String> color = new ArrayList<>();
        color.add("red");
        color.add("blue");
        availableColorsMessage message = new availableColorsMessage(color);
        executor.submit(() -> {
            try {
                serveroutput.reset();
                serveroutput.writeObject(message);
                serveroutput.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        availableColorsMessage messageReceived = mycontroller.getAvailableColor();
        System.out.println(message.toString());
        System.out.println(messageReceived.toString());
        assertEquals(message.getColors(),messageReceived.getColors());

    }

    @Test
    void chooseColor() {
        //controller should send a chosenColorMessage
        String color = "red";
        mycontroller.chooseColor(color);
        String colorReceived = null;
            try {
                chosenColorMessage messageToServer = (chosenColorMessage) serverinput.readObject();
                colorReceived=  messageToServer.getColor();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        System.out.println(colorReceived);
        System.out.println(color);

        assertEquals(color, colorReceived);


    }

    @Test
    void newHost() {
    }

    @Test
    void expectedPlayers() {
    }

    @Test
    void placeStarter() {
    }

    @Test
    void getObjectiveCards() {
    }

    @Test
    void chooseObjective() {
    }

    @Test
    void placeCard() {
    }

    @Test
    void pickCard() {
    }

    @Test
    void updatePlayer() {
    }

    @Test
    void endGame() {
    }

    @Test
    void sendAnswerToPing() {
    }
}