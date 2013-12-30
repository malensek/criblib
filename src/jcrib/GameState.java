package jcrib;

import java.util.HashMap;
import java.util.Map;

public enum GameState {
    Cut(0),
    Crib(1),
    Play(2),
    Score(3);

    private final int state;

    private GameState(int state) {
        this.state = state;
    }

    public int toInt() {
        return state;
    }

    static Map<Integer, GameState> stateMap = new HashMap<>();

    static {
        for (GameState t : GameState.values()) {
            stateMap.put(t.toInt(), t);
        }
    }

    public static GameState fromInt(int i) {
        GameState s = stateMap.get(i);
        return s;
    }
}
