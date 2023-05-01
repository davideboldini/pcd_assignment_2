package assignment.Message;

import java.io.File;
import java.util.List;

public class MessageFileLength {

    private List<Long> fileLengthList;

    public MessageFileLength(final List<Long> fileLengthList){
        this.fileLengthList = fileLengthList;
    }

    public List<Long> getFileLength(){
        return this.fileLengthList;
    }
}
