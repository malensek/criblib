package jcrib;

import java.util.ArrayList;
import java.util.List;

import jcrib.cards.Card;
import jcrib.cards.Hand;

public class Player {
    private String name;
    private int points;
    private Hand hand;
    private Card cut;
    private List<List<Score>> scores = new ArrayList<>();

    public Player(String name) {
        this.name = name;
        this.hand = new Hand();
    }

    public void addScores(List<Score> scores) {
        this.scores.add(scores);
    }

    public String getName() {
        return name;
    }

    public Hand getHand() {
        return hand;
    }

    public Card getCutCard() {
        return cut;
    }

    public void setCutCard(Card cut) {
        this.cut = cut;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return name + ": " + getPoints() + " pts; " + hand.toString();
    }
}
