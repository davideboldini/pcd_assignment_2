package assignment.Model.Events;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilesEvent extends Event{

    private List<File> fileList;

    public FilesEvent(final List<File> fileList){
        this.fileList = new ArrayList<>(fileList);
    }

    @Override
    public EventEnum getEventType() {
        return EventEnum.FILE;
    }

    @Override
    public List<File> get() {
        return fileList;
    }
}
