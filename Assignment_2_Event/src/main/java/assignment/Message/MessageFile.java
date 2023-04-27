package assignment.Message;

import java.io.File;

public class MessageFile {

    public File file;

    public MessageFile(final File file){
        this.file = file;
    }

    public File getFile(){
        return this.file;
    }
}
