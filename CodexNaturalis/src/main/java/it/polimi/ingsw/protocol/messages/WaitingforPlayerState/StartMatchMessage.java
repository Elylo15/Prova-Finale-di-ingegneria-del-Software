package it.polimi.ingsw.protocol.messages.WaitingforPlayerState;

import it.polimi.ingsw.protocol.messages.Message;
import java.io.Serializable;

public class StartMatchMessage implements Message, Serializable {
    private String hostNickname;
    private boolean startSignal;

    public StartMatchMessage(String hostNickname, boolean startSignal) {
        this.hostNickname = hostNickname;
        this.startSignal = startSignal;
    }
    public String getHostNickname() {
        return hostNickname;
    }

   public boolean getStartSignal() {
            return startSignal;
    }

}
