module CodexNaturalis {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.rmi;

    opens it.polimi.ingsw.app to javafx.fxml;
    exports it.polimi.ingsw.app;
    opens it.polimi.ingsw.protocol.client.view.GUI to javafx.fxml;
    exports it.polimi.ingsw.protocol.client.view.GUI;

    exports it.polimi.ingsw.protocol.messages.EndGameState;
    exports it.polimi.ingsw.protocol.messages.ConnectionState;
    exports it.polimi.ingsw.protocol.messages.ServerOptionState;
    exports it.polimi.ingsw.protocol.messages.PlayerTurnState;
    exports it.polimi.ingsw.protocol.messages.ObjectiveState;
    exports it.polimi.ingsw.protocol.messages.StaterCardState;
    exports it.polimi.ingsw.protocol.messages.WaitingforPlayerState;
    exports it.polimi.ingsw.protocol.messages;
}