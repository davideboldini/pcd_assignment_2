package assignment.Agent;

import assignment.Message.MessageDirectory;
import assignment.Message.MessageFile;
import assignment.Model.Directory;
import assignment.Utility.Codec.GenericCodec;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

import java.io.File;

public class DirectoryAgent extends AbstractVerticle {

    @Override
    public void start(final Promise<Void> startPromise){

        System.out.println("Directory agent started");
        EventBus eventBus = this.getVertx().eventBus();
        eventBus.consumer("directory-topic", (Message<MessageDirectory> message) -> {
            MessageDirectory mexDir = message.body();
            //System.out.println("[Directory Agent] new message: " + mexDir.getDirectory().getDirPath());

            for (File elem: mexDir.getDirectory().getAllElementsList()) {
                if (elem.isDirectory()){
                    eventBus.publish("directory-topic", new MessageDirectory(new Directory(elem.getAbsolutePath())));
                }else {
                    eventBus.publish("file-topic", new MessageFile(elem));
                }
            }
        });

        startPromise.complete();

    }
}
