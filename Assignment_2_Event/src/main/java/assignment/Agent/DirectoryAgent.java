package assignment.Agent;

import assignment.Message.MessageDirectory;
import assignment.Message.MessageFile;
import assignment.Model.Directory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.shareddata.Counter;
import io.vertx.core.shareddata.SharedData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryAgent extends AbstractVerticle {

    @Override
    public void start(final Promise<Void> startPromise){

        final SharedData sd = this.getVertx().sharedData();
        final EventBus eventBus = this.getVertx().eventBus();

        eventBus.consumer("directory-topic", (Message<MessageDirectory> message) -> {
            MessageDirectory mexDir = message.body();

            Promise<Directory> directoryPromise = mexDir.getDirectoryPromise();
            Future<Directory> directoryFuture = directoryPromise.future();

            Promise<List<File>> listFilePromise = Promise.promise();
            eventBus.publish("file-topic", new MessageFile(listFilePromise));

            directoryFuture.onComplete((AsyncResult<Directory> res) -> {
                Directory directory = res.result();

                for (Directory dir: directory.getDirectoryList()) {

                    sd.getCounter("numDir", ar -> {
                        Counter counter = ar.result();
                        counter.getAndIncrement();
                    });


                    Promise<Directory> promiseDir = Promise.promise();

                    MessageDirectory mex = new MessageDirectory(promiseDir);
                    eventBus.publish("directory-topic", mex);

                    promiseDir.complete(dir);
                }

                listFilePromise.complete(new ArrayList<>(directory.getJavaFileList()));

            });

        });
        startPromise.complete();
    }
}
