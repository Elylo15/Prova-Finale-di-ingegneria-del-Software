package it.polimi.ingsw.protocol.messages.ConnectionState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;
import java.util.ArrayList;

public class unavailableNamesMessage implements Message, Serializable {

    ArrayList<String> unavaibleNames = new ArrayList<>();

    /**
     * Constructor for unavailableNamesMessage
     * @param names: ArrayList<String>
     */
    public unavailableNamesMessage(ArrayList<String> names){
        this.unavaibleNames = names;
    }

    /**
     * Getter for unavaibleNames
     * @return ArrayList<String>
     */
    public ArrayList<String> getNames(){
        return unavaibleNames;
    }

    /**
     * Setter for unavaibleNames
     */
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
