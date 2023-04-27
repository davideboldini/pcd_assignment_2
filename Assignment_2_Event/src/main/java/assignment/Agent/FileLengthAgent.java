package assignment.Agent;

import assignment.Message.MessageFile;
import assignment.Message.MessageFileLength;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

import java.util.ArrayList;
import java.util.List;

public class FileLengthAgent extends AbstractVerticle {

    List<Long> fileLengthList = new ArrayList<>();

    @Override
    public void start(final Promise<Void> startPromise) {
        System.out.println("File length agent started");

        EventBus eventBus = this.getVertx().eventBus();

        eventBus.consumer("file-length-topic", (Message<MessageFileLength> message) -> {
            MessageFileLength mex = message.body();
            //System.out.println("[File-length Agent] new message: " + mex.getFileLength());
            fileLengthList.add(mex.getFileLength());
            //System.out.println(fileLengthList.size());
        });

        startPromise.complete();
    }
}
