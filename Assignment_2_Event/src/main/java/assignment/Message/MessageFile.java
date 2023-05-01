package assignment.Message;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MessageFile {

    private File file;
    private List<File> listFile;

    public MessageFile(final File file){
        this.file = file;
    }

    public MessageFile(final List<File> listFile){
        this.listFile = new ArrayList<>(listFile);
    }

    public File getFile(){
        return this.file;
    }

    public List<File> getListFile() {
        return listFile;
    }
}
