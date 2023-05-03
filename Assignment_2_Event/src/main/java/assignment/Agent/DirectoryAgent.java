package assignment.Agent;

import assignment.Message.MessageDirectory;
import assignment.Message.MessageFile;
import assignment.Model.Directory;
import assignment.Utility.Analyser.SourceAnalyzerImpl;
import assignment.Utility.Codec.GenericCodec;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

import javax.xml.transform.Result;
import java.io.File;
import java.util.List;

public class DirectoryAgent extends AbstractVerticle {

    @Override
    public void start(final Promise<Void> startPromise){

        EventBus eventBus = this.getVertx().eventBus();

        eventBus.publish("add-dir-topic", deploymentID());

        eventBus.consumer("directory-topic", (Message<MessageDirectory> message) -> {
            MessageDirectory mexDir = message.body();

            eventBus.publish("file-topic", new MessageFile(mexDir.getDirectory().getJavaFileList()));

            for (Directory dir: mexDir.getDirectory().getDirectoryList()) {
                vertx.deployVerticle(new DirectoryAgent(), res -> {
                    eventBus.send("directory-topic", new MessageDirectory(dir));
                });
            }

        });
        startPromise.complete();
    }

    @Override
    public void stop(){
        //System.out.println("Stopped directory agent");
    }
}
