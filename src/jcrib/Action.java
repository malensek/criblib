package jcrib;

/**
 * Represents a player action: cutting the deck, placing cards in the crib, and
 * playing cards.
 *
 * @author malensek
 */
public class Action {

    /** Game state the Action is intended for */
    private Game.State state;

    /** In-hand card index (ID) */
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
