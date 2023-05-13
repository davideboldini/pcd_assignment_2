package assignment.Model.Events;

public abstract class Event<S> {

    public abstract EventEnum getEventType();

    public abstract S get();


}
