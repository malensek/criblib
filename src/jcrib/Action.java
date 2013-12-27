

package jcrib;

public class Action {

    private GameStateMachine.State state;
    private int stateToken;
    private int cardNumber;

    public Action(GameStateMachine.State state, int stateToken, int cardNumber) {
        this.state = state;
        this.stateToken = stateToken;
        this.cardNumber = cardNumber;
    }

    public GameStateMachine.State getState() {
        return state;
    }

    public int getStateToken() {
        return stateToken;
    }

    public int getCardNumber() {
        return cardNumber;
    }
}
