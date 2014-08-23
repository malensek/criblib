/*
Copyright (c) 2013, Matthew Malensek
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

This software is provided by the copyright holders and contributors "as is" and
any express or implied warranties, including, but not limited to, the implied
warranties of merchantability and fitness for a particular purpose are
disclaimed. In no event shall the copyright holder or contributors be liable for
any direct, indirect, incidental, special, exemplary, or consequential damages
(including, but not limited to, procurement of substitute goods or services;
loss of use, data, or profits; or business interruption) however caused and on
any theory of liability, whether in contract, strict liability, or tort
(including negligence or otherwise) arising in any way out of the use of this
software, even if advised of the possibility of such damage.
*/
package jcrib.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Models an instance of a standard 52-card deck of playing cards.
 *
 * @author malensek
 */
public class Deck {

    protected List<Card> cards = new ArrayList<>();

    /**
     * Creates a new Deck instance.  The deck can be initially empty or
     * populated with the cards from a standard deck.
     *
     * @param standardDeck if true, the standard 52 cards will be placed in the
     * deck.
     */
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

    /**
     * Creates a new Deck with the standard 52 cards.
     */
    public Deck() {
        this(true);
    }

    /**
     * Constructs a copy of a Deck.
     */
    public Deck(Deck d) {
        this.cards = new ArrayList<>(d.getCards());
    }

    /**
     * Places a card at the end of the deck.
     *
     * @param card Card to place in the deck.
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Removes the card at the top of the deck.
     *
     * @return top {@link Card}
     */
    public Card removeCard() {
        return cards.remove(0);
    }

    /**
     * Removes a particular card from the deck.
     *
     * @param index integer index of the card to remove.
     *
     * @return the referenced {@link Card}
     */
    public Card removeCard(int index) {
        return cards.remove(index);
    }

    /**
     * Removes a particular unique card from the deck; for example, the 6 of
     * spades could be removed with this method.
     *
     * @param card {@link Card} to remove
     *
     * @return true if the remove operation was successful
     */
    public boolean removeCard(Card card) {
        return cards.remove(card);
    }

    /**
     * Retrieves the top card of the deck, but does not remove it.
     *
     * @return reference to the top card of the deck.
     */
    public Card getCard() {
        return cards.get(0);
    }

    /**
     * Retrives a particular card from the deck without removing it.
     *
     * @param index integer index of the card to retrieve.
     *
     * @return the referenced {@link Card}
     */
    public Card getCard(int index) {
        return cards.get(index);
    }

    /**
     * Retrieves the array index of the referenced {@link Card}, or -1 if the
     * Card is not in the deck.
     *
     * @param card Card to find the index of.
     *
     * @return index of the referenced Card, or -1 if the Card was not found in
     * the deck.
     */
    public int getIndex(Card card) {
        return cards.indexOf(card);
    }

    /**
     * Retrieves the deck of cards in List form.
     *
     * @return the backing List of Card instances in the deck.
     */
    public List<Card> getCards() {
        return cards;
    }

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

    /**
     * Retrieves the number of cards in the deck.
     *
     * @return number of cards that are currently in the deck.
     */
    public int size() {
        return cards.size();
    }

    /**
     * Converts this deck to an array of {@link Card}s.
     *
     * @return array of Cards
     */
    public Card[] toCardArray() {
        return cards.toArray(new Card[cards.size()]);
    }

    @Override
    public String toString() {
        String str = "";
        for (Card card : cards) {
            str += card + ", ";
        }
        return str;
    }
}
