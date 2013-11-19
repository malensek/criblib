package jcrib;

public enum Suit {
    Spade,
    Heart,
    Diamond,
    Club;

    public static Suit fromString(String s) {
        s = s.toLowerCase();
        switch (s) {
            case "s":
                return Spade;
            case "h":
                return Heart;
            case "d":
                return Diamond;
            case "c":
                return Club;
        }
        return null;
    }

    @Override
    public String toString() {
        switch (this) {
            case Spade:
                return "s";
            case Heart:
                return "h";
            case Diamond:
                return "d";
            case Club:
                return "c";
        }
        return null;
    }
}
