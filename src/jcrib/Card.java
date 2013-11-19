package jcrib;

public class Card implements Comparable<Card> {
    private Suit suit;
    private Face face;
    private int value;

    public Card(String cardName) {
        String id = cardName.substring(0, 1).toUpperCase();
        String suit = cardName.substring(1, 2).toUpperCase();

        try {
            value = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            value = 10;
            face = Face.fromString(id);
        }

        this.suit = Suit.fromString(suit);
        // TODO verify correctness here
    }

    public Card(int value, Suit suit) {
        this.suit = suit;
        this.face = null;
        this.value = value;
    }

    public Card(Face face, Suit suit) {
        this.suit = suit;
        this.face = face;
        this.value = 10;
    }

    public Suit getSuit() {
        return suit;
    }

    public Face getFace() {
        return face;
    }

    public int getValue() {
        return value;
    }

    public int getOrdinal() {
        if (isFaceCard()) {
            return value + face.ordinal() + 1;
        } else {
            return value;
        }
    }

    public boolean isFaceCard() {
        return !(face == null);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        return compareTo((Card) obj) == 0;
    }

    @Override
    public int compareTo(Card other) {
        return this.getOrdinal() - other.getOrdinal();
    }

    @Override
    public String toString() {
        if (face == null) {
            return value + suit.toString();
        } else {
            return face.toString() + suit.toString();
        }
    }
}
