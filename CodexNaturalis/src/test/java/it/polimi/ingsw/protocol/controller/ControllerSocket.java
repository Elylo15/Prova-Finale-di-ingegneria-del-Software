package it.polimi.ingsw.protocol.controller;

import it.polimi.ingsw.protocol.client.controller.ControllerSocket;
import it.polimi.ingsw.protocol.messages.*;
import it.polimi.ingsw.protocol.messages.ConnectionState.*;
import it.polimi.ingsw.protocol.messages.EndGameState.*;
import it.polimi.ingsw.protocol.messages.PlayerTurnState.*;
import it.polimi.ingsw.protocol.messages.ServerOptionState.*;
import it.polimi.ingsw.protocol.messages.StaterCardState.*;
import it.polimi.ingsw.protocol.messages.WaitingforPlayerState.*;
import it.polimi.ingsw.protocol.messages.ObjectiveState.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
/*
public class ControllerSocketTest {
    ControllerSocket controllerSocket;
    String serverPort;
    String serverIp;

    @BeforeEach
    void setUp() {
        controllerSocket = new ControllerSocket(serverIp, serverPort);
        controllerSocket.connectToServer(serverIp, serverPort);
    }

    @Test
    void connection() {

    }

    @Test
    void answerConnectionTest() {
        connectionResponseMessage answer = controllerSocket.answerConnection();
        assertEquals();

    }

    @Test
    void serverOptionTest() {
        serverOptionMessage options = controllerSocket.serverOptions();
        assertEquals();
    }

    @Test
    void sendOptionsTest() {
        serverOptionMessage options = new serverOptionMessage(false, 23, "Bianca", true, "");
        controllerSocket.sendOptions(options);
    }

    @Test
    void correctOptionTest() {
        serverOptionResponseMessage correct = controllerSocket.correctOption();
        assertEquals();
    }

    @Test
    void getUnavailableNameTest() {
        unavailableNamesMessage names = controllerSocket.getUnavailableName();
        assertEquals();
    }

    @Test
    void chooseNameTest() {
        controllerSocket.chooseName("Bianca");
    }

    @Test
    void correctNameTest(){
        nameResponseMessage correct = controllerSocket.correctName();
        assertEquals();
    }

    @Test
    void getAvailableColorTest(){
        availableColorsMessage colors = controllerSocket.getAvailableColor();
    }

    @Test
    void chooseColorTest(){
        controllerSocket.chooseColor("Blue");
    }

    @Test
    void correctColorTest(){
        colorResponseMessage correct = controllerSocket.correctColor();
        assertEquals();
    }

    @Test
    void newHostTest(){
        newHostMessage host = controllerSocket.newHost();
        assertEquals();
    }

    @Test
    void expectedPlayersTest(){
        controllerSocket.expectedPlayers(4);
    }

    @Test
    void correctExpectedPlayers(){
        expectedPlayersResponseMessage correct = controllerSocket.correctExpectedPlayers();

    }

    @Test
    void placeStarterTest(){
        controllerSocket.placeStarter(1);

    }

    @Test
    void correctStarter(){
        starterCardResponseMessage correct =
    }


}*/