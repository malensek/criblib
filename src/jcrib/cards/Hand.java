package jcrib.cards;

/**
 * Represents a hand (set of cards in a player's hand).  A hand is modeled as a
 * small {@link Deck}.
 *
 * @author malensek
 */
public class Hand extends Deck {

    public Hand() {
        super(false);
        cards.clear();
    }

    @Override
    public String toString() {
        String str = "";
        for (Card card : cards) {
            str += card + " ";
        }
        return str;
    }
}
