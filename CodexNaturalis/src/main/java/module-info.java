module CodexNaturalis {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    opens it.polimi.ingsw.app to javafx.fxml;
    exports it.polimi.ingsw.app;
    opens it.polimi.ingsw.protocol.client.view.GUI to javafx.fxml;
    exports it.polimi.ingsw.protocol.client.view.GUI;

    exports  it.polimi.ingsw.model.cards;
    opens it.polimi.ingsw.model.cards to com.fasterxml.jackson.databind;

    exports it.polimi.ingsw.protocol.messages.EndGameState;
    exports it.polimi.ingsw.protocol.messages.ConnectionState;
    exports it.polimi.ingsw.protocol.messages.ServerOptionState;
    exports it.polimi.ingsw.protocol.messages.PlayerTurnState;
    exports it.polimi.ingsw.protocol.messages.ObjectiveState;
    exports it.polimi.ingsw.protocol.messages.StaterCardState;
    exports it.polimi.ingsw.protocol.messages.WaitingforPlayerState;
    exports it.polimi.ingsw.protocol.messages;

    exports it.polimi.ingsw.model;
    exports it.polimi.ingsw.model.cards.exceptions;
    exports it.polimi.ingsw.model.cards.enumeration;

    exports it.polimi.ingsw.protocol.server;
    exports it.polimi.ingsw.protocol.server.RMI;
    exports it.polimi.ingsw.protocol.server.exceptions;
    exports it.polimi.ingsw.protocol.server.FSM;
    exports it.polimi.ingsw.protocol.client.view.GUI.controller;
    opens it.polimi.ingsw.protocol.client.view.GUI.controller to javafx.fxml;
    exports it.polimi.ingsw.protocol.client.view.GUI.message;
    opens it.polimi.ingsw.protocol.client.view.GUI.message to javafx.fxml;
}