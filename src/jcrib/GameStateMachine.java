
package jcrib;

import java.util.ArrayList;
import java.util.List;

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

        deck = new Deck();
        deck.shuffle();
        changeState(State.Cut);
    }

    private void changeState(State state) {
        currentState = state;
    }

    private void incrementStateToken() {
        stateToken++;
    }

    public Result executeAction(Player player, Action action)
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
        }
        return null;
    }

    private Result cutDeck(Player player, int cardNumber)
    throws IllegalPlayException {
        if (cardNumber >= deck.numCards()) {
            cardNumber = deck.numCards() - 1;
        }

        player.setCutCard(deck.removeCard(cardNumber));

        for (Player p : players) {
            if (p.getCutCard() == null) {
                /* Keep cutting */
                return new Result(Result.Type.OK);
            }
        }

        /* Done cutting */
        return whoGoesFirst();
    }

    private Result whoGoesFirst() {
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

        if (!tie) {
            dealer = first;
            changeState(State.Play);
            return new Result(Result.Type.OK);
        } else {
            return new Result(Result.Type.Recut);
        }
    }

}
