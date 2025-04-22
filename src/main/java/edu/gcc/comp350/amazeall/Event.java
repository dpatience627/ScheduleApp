package edu.gcc.comp350.amazeall;

import java.sql.Time;

public class Event {
    private String eventName;
    private Time StartTime;
    private Time EndTime;

    public Event(String newName, Time timeToStart, Time timeToEnd){
        this.eventName = newName;
        this.StartTime = timeToStart;
        this.EndTime = timeToEnd;
    }
    public Time getEndTime() {
        return EndTime;
    }
    public Time getStartTime() {
        return StartTime;
    }
    public String getEventName() {
        return eventName;
    }
}
