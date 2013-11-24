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
