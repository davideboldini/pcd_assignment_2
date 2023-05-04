package assignment.Agent;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;


public class ControllerAgent extends AbstractVerticle {

    private int count = 0;

    @Override
    public void start(final Promise<Void> startPromise){
        EventBus eventBus = getVertx().eventBus();

        eventBus.consumer("add-dir-topic", res -> this.count++);

        eventBus.consumer("remove-dir-topic", res -> {
            this.count--;
            System.out.println("Dir list size: " + count);
            if (count == 0){
                eventBus.publish("get-interval-topic", null);
                eventBus.publish("get-fileTree-topic", null);
                eventBus.publish("guiEnd-topic", null);
            }
        });
        startPromise.complete();
    }

    //TODO: Immaginare una lista con i deploymentID dei DirectoryVerticle inizializzati,
    // nell'add-dir-topic ricevere come messaggio il deploymentID da aggiungere alla lista
    // nel remove-dir-topic ricevere come messaggio il deploymentID del verticle da fare undeploy
    // nel verticle dei file passare anche il deploymentID della cartella da cui arrivano i file
}
