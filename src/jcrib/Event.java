package jcrib;

import java.util.List;

public class Event {

    public enum EventType { OK, StateChange };

    protected EventType eventType;
    protected GameStateMachine.State state;
    protected List<Score> scores;

    public Event(EventType eventType) {
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }
}
