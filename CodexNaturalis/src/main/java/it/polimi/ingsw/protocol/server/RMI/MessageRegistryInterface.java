package it.polimi.ingsw.protocol.server.RMI;

import java.rmi.Remote;

public interface MessageRegistryInterface extends Remote {
    void sendMessage(String messageId, Object message);
    Object getMessage(String messageId);
}
