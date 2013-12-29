
package jcrib;

import java.util.ArrayList;
import java.util.List;

import jcrib.Event.EventType;
import jcrib.cards.Card;
import jcrib.cards.Deck;
import jcrib.cards.Hand;

public class GameStateMachine {

    public enum State { Cut, Crib, Play };
    private State currentState;
    private int stateToken;

    private List<Player> players = new ArrayList<>();
    private Player turn;
    private Player dealer;

    private Deck deck;
    private Hand crib;
    private Card starter;

    private Play play;

    public GameStateMachine(Player player1, Player player2) {
        players.add(player1);
        players.add(player2);

        prepareCut();
    }

    private void changeState(State state) {
        currentState = state;
    }

    private void incrementStateToken() {
        stateToken++;
    }

    public Event executeAction(Player player, Action action)
        throws IllegalPlayException, InvalidStateException,
                          InvalidStateTokenException {

        if (action.getState() != currentState) {
            throw new InvalidStateException(
                    "Action state does not apply to the current game state");
        }

        if (action.getStateToken() != stateToken) {
            throw new InvalidStateTokenException(
                    "Action state token does not match the current token");
        }

        switch (action.getState()) {
            case Cut:
                return cutDeck(player, action.getCardNumber());

            case Crib:
                return null;

            case Play:
                return null;

            default:
                throw new InvalidStateException("Unknown state!");
        }
    }

    /**
     * Prepares for players to cut the deck: the deck is shuffled and the game
     * state is updated.
     */
    private void prepareCut() {
        deck = new Deck();
        deck.shuffle();
        changeState(State.Cut);
    }

    private Event cutDeck(Player player, int cardNumber)
    throws IllegalPlayException {
        /* For the sake of simplicity, we assume that an out-of-bounds card
         * index selects either the first or last card.  This eliminates the
         * need to inform clients of the card indexes other clients have
         * selected. */
        if (cardNumber < 0) {
            cardNumber = 0;
        }
        if (cardNumber >= deck.numCards()) {
            cardNumber = deck.numCards() - 1;
        }

        Card cutCard = deck.removeCard(cardNumber);
        player.setCutCard(cutCard);

        for (Player p : players) {
            if (p.getCutCard() == null) {
                /* Keep cutting */
                return new Event(EventType.OK);
            }
        }

        /* Done cutting */
        return whoDealsFirst();
    }

    /**
     * Once each player has cut the deck, this method determines which player
     * will deal first.  If there is a tie (players cut the same value of card)
     * then a re-cut will take place.
     *
     * @return StateEvent informing clients to either move on to selecting cards
     * to place in the crib, or re-cut.
     */
    private StateEvent whoDealsFirst() {
        int lowest = Integer.MAX_VALUE;
        boolean tie = false;
        Player first = null;

        for (Player player : players) {
            int cardValue = player.getCutCard().getOrdinal();
            if (cardValue == lowest) {
                tie = true;
            } else if (cardValue < lowest) {
                lowest = cardValue;
                tie = false;
                first = player;
            }
        }

        if (tie) {
            /* There was a tie: Reset players' cut cards */
            for (Player p : players) {
                p.setCutCard(null);
            }
            prepareCut();
            return new StateEvent(State.Cut);
        } else {
            dealer = first;
            changeState(State.Play);
            return new StateEvent(State.Play);
        }
    }

}
