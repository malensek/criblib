package jcrib;

public class Score {

    public enum Type { Fifteen, ThirtyOne, Pair, Run, Flush, Nobs, Heels, Go };

    private Type type;
    private Card[] cards;
    private int points;

    public Score(Type type, Card[] cards, int points) {
        this.type = type;
        this.cards = cards;
        this.points = points;
    }

    public Card[] getCards() {
        return cards;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public String toString() {
        String str = "";
        for (Card card : cards) {
            str += card + " ";
        }
        return type + " ( " + str + ") = " + points;
    }
}
