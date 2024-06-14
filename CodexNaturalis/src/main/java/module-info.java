module CodexNaturalis {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.rmi;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;

    opens it.polimi.ingsw.app to javafx.fxml;
    exports it.polimi.ingsw.app;
    opens it.polimi.ingsw.protocol.client.view.gui to javafx.fxml;
    exports it.polimi.ingsw.protocol.client.view.gui;

    exports  it.polimi.ingsw.model.cards;
    opens it.polimi.ingsw.model.cards to com.fasterxml.jackson.databind;

    exports it.polimi.ingsw.protocol.messages.endGameState;
    exports it.polimi.ingsw.protocol.messages.connectionState;
    exports it.polimi.ingsw.protocol.messages.serverOptionState;
    exports it.polimi.ingsw.protocol.messages.playerTurnState;
    exports it.polimi.ingsw.protocol.messages.objectiveState;
    exports it.polimi.ingsw.protocol.messages.staterCardState;
    exports it.polimi.ingsw.protocol.messages.waitingForPlayerState;
    exports it.polimi.ingsw.protocol.messages;

    exports it.polimi.ingsw.model;
    exports it.polimi.ingsw.model.cards.exceptions;
    exports it.polimi.ingsw.model.cards.enumeration;

    exports it.polimi.ingsw.protocol.server;
    exports it.polimi.ingsw.protocol.server.rmi;
    exports it.polimi.ingsw.protocol.server.exceptions;
    exports it.polimi.ingsw.protocol.server.fsm;
    exports it.polimi.ingsw.protocol.client.view.gui.controller;
    opens it.polimi.ingsw.protocol.client.view.gui.controller to javafx.fxml;
    exports it.polimi.ingsw.protocol.client.view.gui.message;
    opens it.polimi.ingsw.protocol.client.view.gui.message to javafx.fxml;
}