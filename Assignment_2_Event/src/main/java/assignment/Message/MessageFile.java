package assignment.Message;

import io.vertx.core.Promise;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MessageFile {

    private Promise<List<File>> listFilePromise;

    public MessageFile(final Promise<List<File>> listFilePromise){
        this.listFilePromise = listFilePromise;
    }

    public Promise<List<File>> getListFilePromise() {
        return listFilePromise;
    }
}
