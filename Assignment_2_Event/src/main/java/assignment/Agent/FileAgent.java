package assignment.Agent;

import assignment.Message.MessageFile;
import assignment.Message.MessageFileLength;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileAgent extends AbstractVerticle {

    @Override
    public void start(final Promise<Void> startPromise){
        System.out.println("File agent started");
        EventBus eventBus = this.getVertx().eventBus();

        eventBus.consumer("file-topic", (Message<MessageFile> message) -> {
            MessageFile mexFile = message.body();
            //System.out.println("[File Agent] new message: " + mexFile.getFile().getAbsoluteFile());
            Long numRows = this.countNumRows(mexFile.file);
            eventBus.publish("file-length-topic", new MessageFileLength(numRows));
            eventBus.publish("interval-topic", new MessageFileLength(numRows));
        });

        startPromise.complete();
    }

    private Long countNumRows(final File file) {
        try {
            return Files.lines(Path.of(file.getPath())).count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
