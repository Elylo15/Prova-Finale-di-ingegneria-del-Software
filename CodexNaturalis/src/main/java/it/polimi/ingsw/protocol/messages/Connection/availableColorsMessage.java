package it.polimi.ingsw.protocol.messages.Connection;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;
import java.util.ArrayList;

public class availableColorsMessage implements Message, Serializable {
    ArrayList<String> colors = new ArrayList<>();
    public availableColorsMessage(ArrayList<String> color){
        this.colors = color;
    }
    public String toString(){
        return colors.toString();
    }
    @Override
    public void setLock() {

    }

    @Override
    public boolean isLocked() {
        return false;
    }

    @Override
    public void unlock() {

    }
}
