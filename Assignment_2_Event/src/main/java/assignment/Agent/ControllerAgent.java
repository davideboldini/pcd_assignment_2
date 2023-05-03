package assignment.Agent;

import assignment.Message.MessageDirectory;
import assignment.Message.MessageInterval;
import assignment.Model.Directory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ControllerAgent extends AbstractVerticle {

    LinkedList<String> dirIdList = new LinkedList<>();
    @Override
    public void start(final Promise<Void> startPromise){
        EventBus eventBus = getVertx().eventBus();

        eventBus.consumer("add-dir-topic", res -> {
            dirIdList.add(res.body().toString());
        });

        eventBus.consumer("remove-dir-topic", res -> {
            dirIdList.removeFirst();
            System.out.println("Dir list size: " + dirIdList.size());
            if (dirIdList.size() == 0){
                eventBus.publish("get-interval-topic", null);
                eventBus.publish("get-fileTree-topic", null);
                eventBus.publish("guiEnd-topic", null);
            }
        });
        startPromise.complete();
    }
}
