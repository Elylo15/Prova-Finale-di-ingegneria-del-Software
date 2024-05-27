package it.polimi.ingsw.protocol.messages.ConnectionState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class chosenColorMessage implements Message, Serializable {
    private String color;

    public chosenColorMessage(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


}
