package jcrib;

public class Action {

    private Game.State state;
    private int cardId;

    public Action(Game.State state, int cardId) {
        this.state = state;
        this.cardId = cardId;
    }

    public Game.State getActionState() {
        return state;
    }

    public int getCardId() {
        return cardId;
    }

    @Override
    public String toString() {
        return state + ": Card " + cardId;
    }
}
