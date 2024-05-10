package it.polimi.ingsw.protocol.server.RMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class MessageRegistry extends UnicastRemoteObject implements MessageRegistryInterface {
    private final Map<String, Object> messageMap;

    public MessageRegistry() throws RemoteException {
        messageMap = new HashMap<>();
    }

    @Override
    public void sendMessage(String messageId, Object message) {
        messageMap.put(messageId, message);
    }

    @Override
    public Object getMessage(String messageId) {
        return messageMap.get(messageId);
    }
}
