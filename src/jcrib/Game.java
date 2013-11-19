package jcrib;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game {

    private static final Logger logger = Logger.getLogger("jcrib");

    public enum State { Cut, Crib, Play };

    private List<Player> players = new ArrayList<>();

    private State currentState;

    private int cuts;
    private Player turn;
    private Player dealer;
    private Hand crib;

    private Play play;

    private Deck deck;

    public Game(Player player1, Player player2) {
        players.add(player1);
        players.add(player2);

        deck = new Deck();
        startCut();
        turn = player1;
    }

    public boolean playerAction(Player player, Action action) {
        if (logger.isLoggable(Level.INFO)) {
            logger.info(player.getName() + " -> " + action);
        }

        State state = action.getActionState();
        if (state != currentState) {
            logger.warning("Illegal play: wrong game state");
            return false;
        }
        if (player != turn) {
            logger.warning("Illegal play: wrong player's turn");
            return false;
        }

        switch (state) {
            case Cut:
                return selectCutCard(player, action.getCardId());

            case Crib:
                return moveToCrib(player, action.getCardId());

            case Play:
                return playCard(player, action.getCardId());

            default:
                return false;
        }
    }

    private boolean playCard(Player player, int cardNumber) {
        List<Score> scores =
            play.playCard(player.getHand().removeCard(cardNumber));
        player.addScores(scores);

        Player next = getNextPlayer();
        if (play.checkPlayable(next)) {
            nextPlayer();
            return true;
        } else if (play.checkPlayable(player)) {
            return true;
        } else {
            /* Go */
            player.addScores(play.go());
            nextPlayer();
        }
        return true;
    }

    private void startPlay() {
        deck = new Deck();
        deck.shuffle();

        int cards = 6;
        if (players.size() > 2) {
            cards = 5;
        }
        deck.deal(cards, players);

        crib = new Hand();
        currentState = State.Crib;
    }

    private boolean moveToCrib(Player player, int cardNumber) {
        if (cardNumber >= player.getHand().numCards()) {
            logger.warning("Invalid card index: " + cardNumber);
            return false;
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

        return true;
    }

    private void startCut() {
        deck.shuffle();
        this.currentState = State.Cut;
    }

    public boolean selectCutCard(Player player, int cardNumber) {
        if (cardNumber >= deck.numCards()) {
            cardNumber = deck.numCards() - 1;
        }

        player.setCutCard(deck.removeCard(cardNumber));
        cuts++;

        if (cuts == players.size()) {
            return determineFirst();
        }

        return true;
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
}
