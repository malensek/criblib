package jcrib;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jcrib.cards.Card;
import jcrib.cards.Deck;
import jcrib.cards.Hand;

public class Game {

    private static final Logger logger = Logger.getLogger("jcrib");

    public enum State { Cut, Crib, Play };

    private List<Player> players = new ArrayList<>();

    private State currentState;

    private int cuts;
    private Player turn;
    private Player dealer;
    private Hand crib;
    private Card starter;
    private Play play;
    private Deck deck;

    public Game(Player player1, Player player2) {
        players.add(player1);
        players.add(player2);

        deck = new Deck();
        cuts = 0;
        startCut();
        turn = player1;
    }

    public static void deal(Deck deck, int numCards, Player... players) {
        deal(deck, numCards, players);
    }

    public static void deal(Deck deck, int numCards, Iterable<Player> players) {
        for (int i = 0; i < numCards; ++i) {
            for (Player player : players) {
                Card card = deck.removeCard();
                player.getHand().addCard(card);
            }
        }
    }
    private void startCut() {
        deck.shuffle();
        this.currentState = State.Cut;
    }

    private void startPlay() {
        deck = new Deck();
        deck.shuffle();

        int cards = 6;
        if (players.size() > 2) {
            cards = 5;
        }
        deal(deck, cards, players);

        crib = new Hand();
        currentState = State.Crib;
    }

    public Card drawStarter() {
        starter = deck.removeCard();
        return starter;
    }

    private void verifyAction(Player player, Game.State state)
    throws IllegalPlayException {
        if (state != currentState) {
            throw new IllegalPlayException("Incorrect game state");
        }

        if (player != turn) {
            throw new IllegalPlayException("Wrong player's turn");
        }
    }

    public void playCard(Player player, int cardNumber) {
        if (logger.isLoggable(Level.INFO)) {
            logger.info(player.getName() + " plays card " + cardNumber);
        }

        List<Score> scores =
            play.playCard(player.getHand().removeCard(cardNumber));
        player.addScores(scores);

        Player next = getNextPlayer();
        if (play.checkPlayable(next)) {
            nextPlayer();
        } else if (play.checkPlayable(player)) {
            /* Current player keeps playing */
            return;
        } else {
            /* Go */
            player.addScores(play.go());
            nextPlayer();
        }
    }

    public void moveToCrib(Player player, int cardNumber)
    throws IllegalPlayException {
        if (logger.isLoggable(Level.INFO)) {
            logger.info(player.getName() + " moves card " + cardNumber
                    + " to crib");
        }

        if (cardNumber >= player.getHand().numCards()) {
            throw new IllegalPlayException("Invalid card index: " + cardNumber);
        }

        crib.addCard(player.getHand().removeCard(cardNumber));

        if (logger.isLoggable(Level.INFO)) {
            logger.info("Crib: " + crib.toString());
        }

        /* Crib is ready to go, start playing. */
        if (crib.numCards() == 4) {
            currentState = State.Play;
            turn = dealer;
            nextPlayer();
            play = new Play();
        }
    }

    public void selectCutCard(Player player, int cardNumber)
    throws IllegalPlayException {
        if (cardNumber >= deck.numCards()) {
            cardNumber = deck.numCards() - 1;
        }

        player.setCutCard(deck.removeCard(cardNumber));
        cuts++;

        if (cuts == players.size()) {
            determineFirst();
        }
    }

    private boolean determineFirst() {
        int lowest = Integer.MAX_VALUE;
        boolean tie = false;
        Player first = null;

        for (Player player : players) {
            if (logger.isLoggable(Level.INFO)) {
                logger.info(player.getName() + " drew " + player.getCutCard());
            }

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
            if (logger.isLoggable(Level.INFO)) {
                logger.info(first.getName() + " deals first");
            }

            dealer = first;
            startPlay();
            return true;
        } else {
            cuts = 0;
            return false;
        }
    }

    public void nextPlayer() {
        turn = players.get((players.indexOf(turn) + 1) % players.size());
    }

    public Player getNextPlayer() {
        return players.get((players.indexOf(turn) + 1) % players.size());
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Game.State getState() {
        return currentState;
    }

    public Play getPlayState() {
        return play;
    }

    public Player getCurrentPlayer() {
        return turn;
    }

    public Player getDealer() {
        return dealer;
    }
}
