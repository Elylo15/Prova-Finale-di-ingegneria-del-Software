package it.polimi.ingsw.protocol.server;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.protocol.server.FSM.PlayerFSM;

import java.io.Serializable;

public class PlayerInfo implements Serializable {
    private Player player;
    private PlayerFSM fsm;
    private ClientConnection connection;

    public PlayerInfo(Player player, PlayerFSM fsm, ClientConnection connection) {
        this.player = player;
        this.fsm = fsm;
        this.connection = connection;
    }

    public Player getPlayer() { return player;}
    public PlayerFSM getFsm() { return fsm;}
    public ClientConnection getConnection() { return connection;}
}
