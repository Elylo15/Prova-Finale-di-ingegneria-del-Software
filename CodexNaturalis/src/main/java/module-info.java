module CodexNaturalis {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.rmi;

    opens it.polimi.ingsw.app to javafx.fxml;
    exports it.polimi.ingsw.app;
    opens it.polimi.ingsw.protocol.client.view.GUI to javafx.fxml;
    exports it.polimi.ingsw.protocol.client.view.GUI;
}