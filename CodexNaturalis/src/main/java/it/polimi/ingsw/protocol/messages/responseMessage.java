package it.polimi.ingsw.protocol.messages;

import java.io.Serializable;

public class responseMessage implements Message, Serializable {
    private boolean correct;

    public responseMessage(boolean correct) {
        this.correct = correct;
    }

    public boolean getCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

}
