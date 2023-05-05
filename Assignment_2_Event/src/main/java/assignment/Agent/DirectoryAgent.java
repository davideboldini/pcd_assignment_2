package assignment.Agent;

import assignment.Message.MessageDirectory;
import assignment.Message.MessageFile;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.shareddata.Counter;
import io.vertx.core.shareddata.SharedData;

public class DirectoryAgent extends AbstractVerticle {

    @Override
    public void start(final Promise<Void> startPromise){

        final SharedData sd = vertx.sharedData();
        sd.getCounter("numDir", ar -> {
            Counter counter = ar.result();
            counter.getAndIncrement();
        });

        EventBus eventBus = this.getVertx().eventBus();

        eventBus.consumer("directory-topic", (Message<MessageDirectory> message) -> {
            MessageDirectory mexDir = message.body();

            eventBus.publish("file-topic", new MessageFile(mexDir.getDirectory().getJavaFileList()));

            mexDir.getDirectory().getDirectoryList().forEach(dir -> vertx.deployVerticle(new DirectoryAgent(), res -> eventBus.send("directory-topic", new MessageDirectory(dir))));

        });
        startPromise.complete();
    }
}
