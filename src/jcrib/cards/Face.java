package jcrib.cards;

public enum Face {
    Jack,
    Queen,
    King;

    public static Face fromString(String s) {
        s = s.toUpperCase();
        switch (s) {
            case "K":
                return King;
            case "Q":
                return Queen;
            case "J":
                return Jack;
        }
        return null;
    }

    @Override
    public String toString() {
        switch (this) {
            case Jack:
                return "J";
            case Queen:
                return "Q";
            case King:
                return "K";
        }
        return null;
    }
}
