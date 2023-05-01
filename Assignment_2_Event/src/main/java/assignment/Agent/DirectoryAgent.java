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

        EventBus eventBus = this.getVertx().eventBus();
        eventBus.consumer("directory-topic", (Message<MessageDirectory> message) -> {
            MessageDirectory mexDir = message.body();

            eventBus.publish("file-topic", new MessageFile(mexDir.getDirectory().getJavaFileList()));

            if (!mexDir.getDirectory().getDirectoryList().isEmpty()) {
                for (Directory dir : mexDir.getDirectory().getDirectoryList()) {
                    vertx.deployVerticle(new DirectoryAgent());
                    eventBus.send("directory-topic", new MessageDirectory(dir));
                    //this.getVertx().undeploy(deploymentID());
                }
            }
            //System.out.println(vertx.deploymentIDs().size());
        });

        startPromise.complete();

    }

    @Override
    public void stop(){
        //System.out.println(vertx.deploymentIDs().size());
        System.out.println("Stopped directory agent");
    }
}
