
package jcrib;

import java.util.List;

public class Result {

    public enum Type { OK, Recut, Score }
    private Type type;

    private List<Score> scores;

    public Result(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
