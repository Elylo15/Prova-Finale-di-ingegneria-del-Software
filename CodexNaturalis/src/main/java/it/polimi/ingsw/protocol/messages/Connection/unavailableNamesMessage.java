package it.polimi.ingsw.protocol.messages.Connection;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;
import java.util.ArrayList;

public class unavailableNamesMessage implements Message, Serializable {

    ArrayList<String> unavaibleNames = new ArrayList<>();

    public unavailableNamesMessage(ArrayList<String> names){
        this.unavaibleNames = names;
    }
    public String toString(){
        return unavaibleNames.toString();
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
