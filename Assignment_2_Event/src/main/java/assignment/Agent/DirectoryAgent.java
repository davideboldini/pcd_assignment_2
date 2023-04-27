package assignment.Agent;

import assignment.Message.MessageDirectory;
import assignment.Message.MessageFile;
import assignment.Model.Directory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

public class DirectoryAgent extends AbstractVerticle {

    public void start(){
        System.out.println("Directory agent started");
        EventBus eventBus = this.getVertx().eventBus();
        eventBus.consumer("directory-topic", (Message<MessageDirectory> message) -> {
            System.out.println("new message: " + message.body().getDirectory().getDirPath());
        });

    }
}
