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
import java.util.List;

import jcrib.cards.Card;
import jcrib.cards.Hand;

public class Player {
    private String name;
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

    public void addScore(Score score) {
        List<Score> scoreList = new ArrayList<>();
        scoreList.add(score);
        this.scores.add(scoreList);
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
        int total = 0;
        for (List<Score> score : scores) {
            for (Score subScore : score) {
                total += subScore.getPoints();
            }
        }
        return total;
    }

    @Override
    public String toString() {
        return name + ": " + getPoints() + " pts; " + hand.toString();
    }
}
