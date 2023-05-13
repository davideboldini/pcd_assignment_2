package assignment.Model.Events;

import assignment.Utility.Pair;

import java.io.File;

public class RowsEvent extends Event{

    private Pair<File,Long> pairFileRows;

    public RowsEvent(final File file, final Long numRows){
        this.pairFileRows = new Pair<>(file, numRows);
    }

    @Override
    public EventEnum getEventType() {
        return EventEnum.ROWS;
    }

    @Override
    public Pair<File, Long> get() {
        return pairFileRows;
    }
}
