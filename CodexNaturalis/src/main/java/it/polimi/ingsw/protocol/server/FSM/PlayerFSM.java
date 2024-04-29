package it.polimi.ingsw.protocol.server.FSM;

public class PlayerFSM {
    private State state;

    /**
     * Default constructor for a new player
     */
    public PlayerFSM() {
        this.state = State.WaitingForPlayers;
    }

    /**
     * Start the FSM from a custom state
     * @param state initial custom state
     */
    public PlayerFSM(State state) {
        this.state = state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {return this.state;}
}
