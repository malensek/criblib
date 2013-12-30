

package jcrib;

public class Action {

    private GameState state;
    private int stateToken;
    private int cardNumber;

    public Action(GameState state, int stateToken, int cardNumber) {
        this.state = state;
        this.stateToken = stateToken;
        this.cardNumber = cardNumber;
    }

    public GameState getState() {
        return state;
    }

    public int getStateToken() {
        return stateToken;
    }

    public int getCardNumber() {
        return cardNumber;
    }
}
