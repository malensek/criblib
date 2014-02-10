
package jcrib;

import java.util.List;

public class Result {

    private GameState state = null;
    private List<Score> scores = null;

    public Result() { }

    public Result(GameState newState) {
        this.state = newState;
    }

    public Result(List<Score> scores) {
        this.scores = scores;
    }

    public boolean isAcknowledgment() {
        return (state == null && scores == null);
    }

    public boolean stateChanged() {
        return state != null;
    }

    public GameState getNewState() {
        return state;
    }

    public void setNewState(GameState state) {
        this.state = state;
    }

    public boolean hasScore() {
        return scores != null;
    }

    public List<Score> getScores() {
        return this.scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }
}
