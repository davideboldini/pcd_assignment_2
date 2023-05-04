package assignment.Message;

import assignment.Model.Directory;

public class MessageDirectory {

    private final Directory directory;

    public MessageDirectory(final Directory directory){
        this.directory = directory;
    }

    public Directory getDirectory(){
        return this.directory;
    }
}
