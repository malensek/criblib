package jcrib.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {

    protected List<Card> cards = new ArrayList<>();

    public Deck(boolean standardDeck) {
        if (standardDeck) {
            cards.clear();
            for (Suit suit : Suit.values()) {
                for (int i = 1; i <= 10; ++i) {
                    addCard(new Card(i, suit));
                }

                for (Face face : Face.values()) {
                    addCard(new Card(face, suit));
                }
            }
        }
    }

    public Deck() {
        this(true);
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public Card removeCard() {
        return cards.remove(0);
    }

    public Card removeCard(int index) {
        return cards.remove(index);
    }

    public boolean removeCard(Card card) {
        return cards.remove(card);
    }

    public Card getCard() {
        return cards.get(0);
    }

    public Card getCard(int index) {
        return cards.get(index);
    }

    public int getIndex(Card card) {
        return cards.indexOf(card);
    }

    public List<Card> getCards() {
        return cards;
    }

    /*
    public void deal(int numCards, Player... players) {
        deal(numCards, players);
    }

    public void deal(int numCards, Iterable<Player> players) {
        for (int i = 0; i < numCards; ++i) {
            for (Player player : players) {
                Card card = cards.remove(0);
                player.getHand().addCard(card);
            }
        }
    }
*/
    /**
     * Performs a Fisher–Yates shuffle on the deck, in place.
     */
    public void shuffle() {
        int idx;
        Card temp, current;
        Random random = new Random();
        for (int i = cards.size() - 1; i > 0; i--) {
            idx = random.nextInt(i + 1);
            current = cards.get(i);
            temp = cards.get(idx);

            /* Swap the two cards */
            cards.set(idx, current);
            cards.set(i, temp);
         }
    }

    public int numCards() {
        return cards.size();
    }

    public Card[] toCardArray() {
        return cards.toArray(new Card[cards.size()]);
    }

    public String toString() {
        String str = "";
        for (Card card : cards) {
            str += card + ", ";
        }
        return str;
    }
}
