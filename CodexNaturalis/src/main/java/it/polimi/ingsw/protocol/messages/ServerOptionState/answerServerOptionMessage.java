package it.polimi.ingsw.protocol.messages.ServerOptionState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;

public class answerServerOptionMessage implements Message, Serializable {
    String message = "Your turn starts";
    public String toString(){
        return message.toString();
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

