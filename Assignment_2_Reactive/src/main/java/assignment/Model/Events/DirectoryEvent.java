package assignment.Model.Events;

import assignment.Model.Directory;

public class DirectoryEvent extends Event{

    private Directory directory;

    public DirectoryEvent(final Directory directory){
        this.directory = directory;
    }

    @Override
    public EventEnum getEventType() {
        return EventEnum.DIRECTORY;
    }

    @Override
    public Directory get() {
        return directory;
    }
}
