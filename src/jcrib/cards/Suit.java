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

/**
 * Represents the four suits of a standard 52-card deck: spades, hearts,
 * diamonds, and clubs.  The suits can be displayed as symbols or by a single
 * letter.
 *
 * @author malensek
 */
public enum Suit {
    Spade,
    Heart,
    Diamond,
    Club;

    private static boolean fancyChars = false;

    public static Suit fromString(String s) {
        s = s.toLowerCase();
        switch (s) {
            case "s": return Spade;
            case "h": return Heart;
            case "d": return Diamond;
            case "c": return Club;
        }
        return null;
    }

    public static void enableFancyChars() {
        fancyChars = true;
    }

    @Override
    public String toString() {
        if (fancyChars) {
            switch (this) {
                case Spade: return "♠";
                case Heart: return "♥";
                case Diamond: return "♦";
                case Club: return "♣";
            }
        } else {
            switch (this) {
                case Spade: return "s";
                case Heart: return "h";
                case Diamond: return "d";
                case Club: return "c";
            }
        }
        return null;
    }
}
