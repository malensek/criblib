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

import jcrib.cards.Card;

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
