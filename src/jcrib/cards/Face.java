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
