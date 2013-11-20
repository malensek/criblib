package jcrib;

import java.util.ArrayList;
import java.util.List;

public class Hand extends Deck {

    public Hand() {
        super(false);
        cards.clear();
    }

    public int score(Card starter) {
        List<Score> scores = new ArrayList<>();
        Card[] allCards = cards.toArray(new Card[cards.size() + 1]);
        allCards[cards.size()] = starter;

        scores.addAll(Scoring.nobs(allCards, starter));
        scores.addAll(Scoring.fifteens(allCards));
        scores.addAll(Scoring.pairs(allCards));
        scores.addAll(Scoring.flush(allCards));
        scores.addAll(Scoring.runs(allCards));

        int points = 0;
        for (Score score : scores) {
            points += score.getPoints();
            System.out.println(score);
        }
        return points;
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
