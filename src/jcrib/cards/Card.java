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
package jcrib.cards;

public class Card implements Comparable<Card> {
    private Suit suit;
    private Face face;
    private int value;

    public Card(String cardName) {
        if (cardName.length() > 3) {
            throw new IllegalArgumentException("Malformed Card identifier");
        }

        int len = cardName.length();

        String id = cardName.substring(0, len - 1).toUpperCase();
        String suit = cardName.substring(len - 1, len).toUpperCase();

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

        Card other = (Card) obj;

        if (this.suit != other.suit) {
            return false;
        }

        return compareTo((Card) obj) == 0;
    }

    @Override
    public int compareTo(Card other) {
        return (this.getOrdinal() - other.getOrdinal()) * 10
            + this.getSuit().ordinal() - other.getSuit().ordinal();
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
