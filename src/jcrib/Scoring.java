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
package jcrib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jcrib.cards.Card;
import jcrib.cards.Face;
import jcrib.cards.Hand;
import jcrib.cards.Suit;

public class Scoring {
    public int scoreHand(Hand hand, Card starter) {
        List<Score> scores = new ArrayList<>();
        List<Card> cards = hand.getCards();
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


    public static List<Score> nobs(Card[] cards, Card starter) {
        List<Score> scores = new ArrayList<>();
        if (starter.getFace() == Face.Jack) {
            /* Dealer scored 2 for his heels; no need to check further */
            return scores;
        }

        for (Card card : cards) {
            if (card.getFace() != Face.Jack) {
                continue;
            }

            if (card.getSuit() == starter.getSuit()) {
                Card[] scoringCard = { card };
                scores.add(new Score(Score.Type.Nobs, scoringCard, 1));
                return scores;
            }
        }

        return scores;
    }

    public static List<Score> runs(Card[] cards) {
        List<Score> scores = new ArrayList<>();
        for (int i = 0; i < cards.length; ++i) {
            List<Card> runCards = new ArrayList<>();
            runCards.add(cards[i]);
            int ord = cards[i].getOrdinal();
            int run = 1;
            int multiplier = 1;
            boolean quadFlag = false;
            for (int j = i + 1; j < cards.length; ++j) {
                int nextOrd = cards[j].getOrdinal();
                int difference = nextOrd - ord;
                if (difference == 0) {
                    runCards.add(cards[j]);
                    multiplier++;
                    if (quadFlag) {
                        multiplier++;
                        quadFlag = false;
                    }
                    continue;
                } else if (difference == 1) {
                    runCards.add(cards[j]);
                    run++;
                    ord = nextOrd;
                    if (multiplier > 1) {
                        quadFlag = true;
                    }
                    continue;
                }
            }
            if (run >= 3) {
                int points = run * multiplier;
                scores.add(new Score(
                            Score.Type.Run,
                            runCards.toArray(new Card[runCards.size()]),
                            points));
                return scores;
            }
        }

        return scores;
    }

    public static List<Score> flush(Card[] cards) {
        List<Score> scores = new ArrayList<>();
        Suit suit = cards[0].getSuit();
        for (int i = 1; i < cards.length; ++i) {
            if (cards[i].getSuit() != suit) {
                return scores;
            }
        }

        scores.add(new Score(Score.Type.Flush, cards, cards.length));
        return scores;
    }

    public static List<Score> pairs(Card[] cards) {
        List<Score> scores = new ArrayList<>();
        for (int i = 0; i < cards.length; ++i) {
            for (int j = i + 1; j < cards.length; ++j) {
                if (cards[i].equals(cards[j])) {
                    Card[] pair = { cards[i], cards[j] };
                    scores.add(new Score(Score.Type.Pair, pair, 2));
                }
            }
        }
        return scores;
    }

    public static List<Score> fifteens(Card[] cards) {
        List<Score> scores = new ArrayList<>();
        int sum = sumCards(cards);
        if (sum == 15) {
            /* All the cards in the hand sum up to 15.  We're done here. */
            scores.add(new Score(Score.Type.Fifteen, cards, 2));
            return scores;
        }

        for (int i = 0; i < cards.length - 2; ++i) {
            checkCombinations(i, sum, cards, scores);
        }
        return scores;
    }

    public static void checkCombinations(int i, int sum, Card[] cards, List<Score> scores) {
        final int[][][] combinations = {
            { {0}, {1}, {2}, {3}, {4} },

            { {0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, 2},
              {1, 3}, {1, 4}, {2, 3}, {2, 4}, {3, 4} },

            { {0, 1, 2}, {0, 1, 3}, {0, 1, 4}, {0, 2, 3},
              {0, 2, 4}, {0, 3, 4}, {1, 2, 3}, {1, 2, 4},
              {1, 3, 4}, {2, 3, 4} }
        };

        for (int j = 0; j < combinations[i].length; ++j) {
            int total = sum;
            for (int k = 0; k < combinations[i][j].length; ++k) {
                if (combinations[i][j][k] >= cards.length) {
                    /* Index is out of bounds; we can stop looking here */
                    return;
                }
                total -= cards[combinations[i][j][k]].getValue();
            }
            if (total == 15) {
                Card[] scoringCards
                    = removeCards(cards, combinations[i][j]);
                scores.add(new Score(Score.Type.Fifteen, scoringCards, 2));
            }
        }

    }

    public static Card[] removeCards(Card[] cards, int[] idxs) {
        List<Card> result = new ArrayList<Card>(Arrays.asList(cards));
        for (int i = idxs.length - 1; i >= 0; --i) {
            result.remove(idxs[i]);
        }

        return result.toArray(new Card[result.size()]);
    }

    public static int sumCards(Card[] cards) {
        int total = 0;
        for (Card card : cards) {
            total += card.getValue();
        }
        return total;
    }

    public static int sumCards(List<Card> cards) {
        int total = 0;
        for (Card card : cards) {
            total += card.getValue();
        }
        return total;
    }

    public static boolean consecutive(Card[] cards) {
        int prevOrd = cards[0].getOrdinal();
        for (int i = 1; i < cards.length; ++i) {
            int currentOrd = cards[i].getOrdinal();
            if (currentOrd - prevOrd != 1) {
                return false;
            }
            prevOrd = currentOrd;
        }
        return true;
    }
}
