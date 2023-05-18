package assignment.Message;

import assignment.Model.Directory;
import io.vertx.core.Promise;

public class MessageDirectory {

    private final Promise<Directory> directoryPromise;

    public MessageDirectory(final Promise<Directory> directoryPromise){
        this.directoryPromise = directoryPromise;
    }

    public Promise<Directory> getDirectoryPromise() {
        return directoryPromise;
    }
}
