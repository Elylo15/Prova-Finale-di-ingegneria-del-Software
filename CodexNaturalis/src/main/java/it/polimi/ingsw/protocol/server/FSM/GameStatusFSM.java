package it.polimi.ingsw.protocol.server.FSM;

public class GameStatusFSM {
    private State state;

    /**
     * Start the FSM from default "Creation" state.
     */
    public GameStatusFSM() {
        state = State.Creation;
    }

    /**
     * Start the FSM from a custom state.
     * @param state
     */
    public GameStatusFSM(State state) {
        this.state = state;
    }

    public State getState() {return this.state;}

    public void transition(Event event) {
        switch (this.state) {
            case Creation -> {
                if(event == Event.Created)
                    this.state = State.WaitingForNewPlayers;
            }
            case WaitingForNewPlayers -> {
                if(event == Event.AllPlayerReady)
                    this.state = State.StarterCard;
            }
            case StarterCard -> {
                if(event == Event.AllStarterCardPlaced)
                    this.state = State.Objective;
            }
            case Objective -> {
                if(event == Event.AllObjectivePicked)
                    this.state = State.Player1Turn
            }
            case Player1Turn -> {
                if(event == Event.SkipPlayer1 || event == Event.Player1TurnEnded)
                    this.state = State.Player2Turn;
            }
            case Player2Turn -> {
                if(event == Event.SkipPlayer2 || event == Event.Player2TurnEnded)
                    this.state = State.Player3Turn;
            }
            case Player3Turn -> {
                if(event == Event.SkipPlayer3 || event == Event.Player3TurnEnded)
                    this.state = State.Player4Turn;
            }
            case Player4Turn -> {
                if(event == Event.gameEnded)
                    this.state = State.EndGame;
                else if(event == Event.SkipPlayer4 || event == Event.Player4TurnEnded)
                    this.state = State.Player1Turn;

            }
            case EndGame -> {
                if(event == Event.destroyGame)
                    this.state = State.Cleaning;
            }
            case Cleaning -> {}
        }
    }
}
