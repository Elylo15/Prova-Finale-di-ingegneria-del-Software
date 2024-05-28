package it.polimi.ingsw.protocol.messages.ConnectionState;

import it.polimi.ingsw.protocol.messages.Message;

import java.io.Serializable;
import java.util.Objects;

public class connectionResponseMessage implements Message, Serializable {
    private boolean correct;

    public connectionResponseMessage(boolean correct) {
        this.correct = correct;
    }

    public boolean getCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        connectionResponseMessage that = (connectionResponseMessage) o;
        return correct == that.correct;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(correct);
    }
}
