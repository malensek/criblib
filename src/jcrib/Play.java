package jcrib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jcrib.cards.Card;

public class Play {

    private List<Card> cards = new ArrayList<>();

    public List<Score> playCard(Card card) {
        cards.add(card);

        List<Score> scores = new ArrayList<>();
        Card[] scoringCards = cards.toArray(new Card[cards.size()]);

        /* Check for 15s and 31s */
        int sum = Scoring.sumCards(cards);
        if (sum == 15) {
            Score fifteen = new Score(Score.Type.Fifteen, scoringCards, 2);
            scores.add(fifteen);
        } else if (sum == 31) {
            Score thirtyOne = new Score(Score.Type.ThirtyOne, scoringCards, 2);
            scores.add(thirtyOne);
            cards.clear();
        }

        scores.addAll(pairs());
        scores.addAll(runs());

        for (Score score : scores) {
            System.out.println(score);
        }
        return scores;
    }

    public List<Score> go() {
        System.out.println("GO!");
        cards.clear();
        List<Score> goScore = new ArrayList<>();
        goScore.add(new Score(Score.Type.Go,
                    cards.toArray(new Card[cards.size()]), 1));
        return goScore;
    }

    public boolean checkPlayable(Player player) {
        int maxCard = 31 - Scoring.sumCards(cards);
        for (Card card : player.getHand().getCards()) {
            if (card.getValue() <= maxCard) {
                return true;
            }
        }
        return false;
    }

    private List<Card> subset(int numCards) {
        List<Card> subset = new ArrayList<>();
        for (int i = cards.size() - numCards; i < cards.size(); ++i) {
            subset.add(cards.get(i));
        }
        return subset;
    }

    private List<Score> pairs() {
        Card lastCard = cards.get(cards.size() - 1);
        List<Card> pairCards = new ArrayList<>();
        pairCards.add(lastCard);
        for (int i = cards.size() - 2; i >= 0; --i) {
            if (cards.get(i).getOrdinal() == lastCard.getOrdinal()) {
                pairCards.add(cards.get(i));
            } else {
                break;
            }
        }

        List<Score> scores
            = Scoring.pairs(pairCards.toArray(new Card[pairCards.size()]));

        return scores;
    }

    private List<Score> runs() {
        int largestRun = 0;

        for (int subsetSize = 3; subsetSize <= cards.size(); ++subsetSize) {
            List<Card> sub = subset(subsetSize);
            Card[] subset = sub.toArray(new Card[sub.size()]);
            Arrays.sort(subset);
            if (Scoring.consecutive(subset)) {
                largestRun = subsetSize;
            }
        }

        List<Score> scores = new ArrayList<>();
        if (largestRun > 0) {
            Card[] runCards = subset(largestRun).toArray(new Card[largestRun]);
            Arrays.sort(runCards);
            scores.add(new Score(Score.Type.Run, runCards, largestRun));
        }

        return scores;
    }

    public int getCurrentSum() {
        return Scoring.sumCards(cards);
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
