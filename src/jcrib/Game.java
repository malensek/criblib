
package jcrib;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jcrib.cards.Card;
import jcrib.cards.Deck;
import jcrib.cards.Face;
import jcrib.cards.Hand;

public class Game {

    private static final int DEFAULT_TARGET_SCORE = 121;
    private int targetScore = DEFAULT_TARGET_SCORE;

    private GameState currentState;
    private int stateToken;

    private List<Player> players = new ArrayList<>();
    private Player dealer;

    private Deck deck;
    private Hand crib;
    private Card starter;

    private List<Score> cribScore;
    private Map<String, List<Score>> handScores;

    private Play playState;

    public Game(Player... players) {
        this(DEFAULT_TARGET_SCORE, players);
    }

    public Game(int targetScore, Player... players) {
        if (players.length > 4) {
            throw new IllegalArgumentException("Cannot support more than "
                    + "four players.");
        }

        this.targetScore = targetScore;

        for (Player player : players) {
            this.players.add(player);
        }

        prepareCut();
    }

    private Result changeState(GameState state) {
        currentState = state;
        stateTransition(state);
        Result result = new Result(state);
        return result;
    }

    private void incrementStateToken() {
        stateToken++;
    }

    /**
     * Prepares for a state transition --- this can include state-dependent
     * initialization.
     */
    private void stateTransition(GameState state) {
        switch (state) {
            case Cut:
                deck = new Deck();
                deck.shuffle();
                return;

            case Crib:
                handScores = new HashMap<>();
                preparePlay();
                return;

            case Play:
                drawStarter();
                scoreHands();
                playState = new Play(players, dealer);
                return;

            case Score: return;
        }
    }

    public Result executeAction(Player player,
            GameState state, int stateToken, int cardNumber)
    throws IllegalPlayException, InvalidStateException,
            InvalidStateTokenException {
        return executeAction(player, new Action(state, stateToken, cardNumber));
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
                return moveToCrib(player, action.getCardNumber());

            case Play:
                return playCard(player, action.getCardNumber());

            default:
                throw new InvalidStateException("Unknown state");
        }
    }

    /**
     * Prepares for players to cut the deck: the deck is shuffled and the game
     * state is updated.
     */
    private void prepareCut() {
        changeState(GameState.Cut);
    }

    public static void deal(Deck deck, int numCards, Iterable<Player> players) {
        for (int i = 0; i < numCards; ++i) {
            for (Player player : players) {
                Card card = deck.removeCard();
                player.getHand().addCard(card);
            }
        }
    }

    private void preparePlay() {
        deck = new Deck();
        deck.shuffle();

        int cards = 6;
        if (players.size() > 2) {
            cards = 5;
        }
        deal(deck, cards, players);

        crib = new Hand();
    }

    private Result cutDeck(Player player, int cardNumber) {
        /* For the sake of simplicity, we assume that an out-of-bounds card
         * index selects either the first or last card.  This eliminates the
         * need to inform clients of the card indexes other clients have
         * selected. */
        if (cardNumber < 0) {
            cardNumber = 0;
        }
        if (cardNumber >= deck.size()) {
            cardNumber = deck.size() - 1;
        }

        Card cutCard = deck.removeCard(cardNumber);
        player.setCutCard(cutCard);

        for (Player p : players) {
            if (p.getCutCard() == null) {
                /* Keep cutting */
                return new Result();
            }
        }

        /* Everyone has cut.  Can we start? */
        return whoDealsFirst();
    }

    public Result moveToCrib(Player player, int cardNumber)
    throws IllegalPlayException {
        checkHandIndex(player, cardNumber);

        Card cribCard = player.getHand().removeCard(cardNumber);
        crib.addCard(cribCard);

        if (crib.size() == 4) {
            return changeState(GameState.Play);
        }
        return new Result();
    }

    private void drawStarter() {
        starter = deck.removeCard();

        /* If the dealer draws a Jack, he/she gets 2 points for his heels. */
        if (starter.getFace() == Face.Jack) {
            dealer.addScore(new Score(Score.Type.Heels,
                        new Card[] { starter }, 2));
        }
    }

    public Result playCard(Player player, int cardNumber)
    throws IllegalPlayException {
        checkHandIndex(player, cardNumber);
        Card card = player.getHand().removeCard(cardNumber);
        List<Score> scores = playState.playCard(player, card);
        addScores(player, scores);

        Result result = new Result();
        if (playState.isFinished()) {
            result = changeState(GameState.Score);
        }

        if (scores.size() > 0) {
            result.setScores(scores);
        }

        return result;
    }

    private void scoreHands() {
        for (Player player : players) {
            List<Score> scores = Scoring.scoreHand(player.getHand(), starter);
            handScores.put(player.getName(), scores);
        }
        cribScore = Scoring.scoreHand(crib, starter);
    }

    public void finishRound()
    throws InvalidStateException {
        if (currentState == GameState.Score) {
            Player player = playState.getNextPlayer(dealer);
            for (int i = 0; i < players.size(); ++i) {
                List<Score> scores = handScores.get(player.getName());
                addScores(player, scores);
                player = playState.getNextPlayer(player);
            }
            addScores(dealer, cribScore);
            dealer = playState.getNextPlayer(dealer);
            changeState(GameState.Crib);
        } else {
            throw new InvalidStateException("Round not over");
        }
    }

    /**
     * Once each player has cut the deck, this method determines which player
     * will deal first.  If there is a tie (players cut the same value of card)
     * then a re-cut will take place.
     *
     * @return StateEvent informing clients to either move on to selecting cards
     * to place in the crib, or re-cut.
     */
    private Result whoDealsFirst() {
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
            return new Result();
        } else {
            dealer = first;
            return changeState(GameState.Crib);
        }
    }

    private void addScores(Player player, List<Score> scores) {
        player.addScores(scores);
        if (player.getPoints() >= targetScore) {
            // winner winner chicken dinner?
        }
    }

    private void checkHandIndex(Player player, int cardNumber)
    throws IllegalPlayException {
        if (cardNumber >= player.getHand().size()) {
            throw new IllegalPlayException("Invalid card index: "
                    + cardNumber);
        }
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public Player getCurrentPlayer() {
        return playState.getCurrentPlayer();
    }

    public Player getDealer() {
        return dealer;
    }

    public Card getStarterCard() {
        return starter;
    }

    public int getCurrentPlayTotal() {
        return playState.getCurrentSum();
    }

    public List<Card> getCardsInPlay() {
        return playState.getCardsInPlay();
    }

    public int getLargestPlayableCard() {
        return playState.getLargestPlayableCard();
    }

    public List<Score> getCribScore() {
        return cribScore;
    }

    public Map<String, List<Score>> getHandScores() {
        return handScores;
    }
}
