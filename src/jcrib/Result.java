
package jcrib;

import java.util.List;

public class Result {

    private GameState state;
    private List<Score> scores;

    public Result() { }

    public Result(GameState newState) {
        this.state = newState;
    }
}
