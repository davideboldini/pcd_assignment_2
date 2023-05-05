package assignment.Agent;

import assignment.Message.MessageFileLength;
import assignment.Message.Type.MessageType;
import assignment.Message.MessageUpdate;
import assignment.Message.MessageInitInterval;
import assignment.Utility.Pair;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class IntervalAgent extends AbstractVerticle {

    private Map<Pair<Integer,Integer>, Integer> intervalMap;

    @Override
    public void start(final Promise<Void> startPromise){
        System.out.println("Interval Agent created");
        EventBus eventBus = this.getVertx().eventBus();

        eventBus.consumer("init-interval-topic", (Message<MessageInitInterval> message) -> {
            MessageInitInterval mex = message.body();
            this.initMap(mex.getMAXL(), mex.getNI());
        });

        eventBus.consumer("interval-topic", (Message<MessageFileLength> message) -> {
            MessageFileLength mex = message.body();

            for (Long numRows: mex.getFileLength()) {
                this.addMap(numRows);
            }
            eventBus.publish("gui-update-topic",  new MessageUpdate(new HashMap<>(intervalMap), MessageType.INTERVAL));
        });

        eventBus.consumer("end-topic", res -> {
            eventBus.publish("return-topic", new MessageUpdate(new HashMap<>(intervalMap), MessageType.INTERVAL));
        });

        startPromise.complete();
    }

    private void initMap(final int MAXL, final int NI){
        this.intervalMap = new LinkedHashMap<>();

        int intervalSize = MAXL;

        if (NI > 1) {
            intervalSize = MAXL / (NI - 1);
        } else {
            this.intervalMap.put(new Pair<>(0, MAXL), 0);
            return;
        }

        for (int i = 0; i < (NI - 1); i++){
            if ( ((i + 1) * intervalSize)-1 != (MAXL - 1) && i == (NI - 1)-1){
                this.intervalMap.put(new Pair<>(i * intervalSize, (MAXL - 1)), 0);
            }else {
                this.intervalMap.put(new Pair<>(i * intervalSize, ((i + 1) * intervalSize)-1), 0);
            }
        }

        this.intervalMap.put(new Pair<>(MAXL, -1), 0);
    }

    private void addMap(final Long numRows){
        this.intervalMap.keySet().stream().filter(interval -> numRows < interval.getY() || (numRows >= interval.getX() && interval.getY().equals(-1))).findFirst().ifPresent(interval -> intervalMap.put(interval, intervalMap.get(interval) + 1));
    }

    @Override
    public void stop(){
        System.out.println("Stopped interval agent");
    }
}
