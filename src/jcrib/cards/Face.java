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
 * Represents the face cards (jack, queen, king) in a standard 52-card deck.
 *
 * @author malensek
 */
public enum Face {
    Jack,
    Queen,
    King;

    /**
     * Converts a String-based representation of a face card (J, Q, K) into its
     * {@link Face} enum representation.
     *
     * @param s The String-based representation of the face card (J, Q, K).
     * Input strings are converted to uppercase.
     *
     * @return {@link Face} representation of the face card String, or null if
     * an unknown String was provided.
     */
    public static Face fromString(String s) {
        s = s.toUpperCase();
        switch (s) {
            case "K": return King;
            case "Q": return Queen;
            case "J": return Jack;
        }
        return null;
    }

    @Override
    public String toString() {
        switch (this) {
            case Jack: return "J";
            case Queen: return "Q";
            case King: return "K";
        }
        return null;
    }
}
