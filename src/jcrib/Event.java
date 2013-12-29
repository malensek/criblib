package jcrib;

import java.util.List;

public class Event {

    public enum EventType { OK, StateChange };
    private EventType eventType;

    private List<Score> scores;

    public Event(EventType eventType) {
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }
}
