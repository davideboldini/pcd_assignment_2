package assignment.Agent;

import assignment.Message.MessageFile;
import assignment.Message.MessageFileLength;
import assignment.Message.Type.MessageType;
import assignment.Message.MessageUpdate;
import assignment.Utility.Pair;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.shareddata.Counter;
import io.vertx.core.shareddata.SharedData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileOperationAgent extends AbstractVerticle {

    private TreeSet<Pair<File, Long>> fileLengthTree;
    private HashMap<Pair<Integer,Integer>, Integer> intervalMap;

    public FileOperationAgent(final int MAXL, final int NI){
        this.initTreeSet();
        this.initMap(MAXL, NI);
    }

    @Override
    public void start(final Promise<Void> startPromise) {
        System.out.println("File operation agent started");

        final SharedData sd = this.getVertx().sharedData();

        EventBus eventBus = this.getVertx().eventBus();

        eventBus.consumer("file-topic", (Message<MessageFile> message) -> {
            MessageFile mex = message.body();

            Promise<List<File>> listFilePromise = mex.getListFilePromise();
            Future<List<File>> listFileFuture = listFilePromise.future();

            listFileFuture.onComplete((AsyncResult<List<File>> res) -> {
               List<File> listFile = res.result();

                for (File file: listFile) {
                    Long numRows = this.countNumRows(file);
                    this.fileLengthTree.add(new Pair<>(file, numRows));
                    this.addMap(numRows);
                }

                eventBus.publish("gui-update-topic",  new MessageUpdate(fileLengthTree, MessageType.FILE_LENGTH));
                eventBus.publish("gui-update-topic",  new MessageUpdate(intervalMap, MessageType.INTERVAL));

                sd.getCounter("numDir", ar -> {
                    Counter counter = ar.result();
                    counter.decrementAndGet(val -> {
                        System.out.println(val);
                        if (val.result() == 0){
                            System.out.println("End");
                            eventBus.publish("end-topic", null);
                        }
                    });
                });
            });
        });

        eventBus.consumer("end-topic", res -> {
            eventBus.publish("return-topic", new MessageUpdate(fileLengthTree, MessageType.FILE_LENGTH));
            eventBus.publish("return-topic", new MessageUpdate(intervalMap, MessageType.INTERVAL));
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

    private void initTreeSet(){
        this.fileLengthTree = new TreeSet<>((o1, o2) -> {
            int countCompare = o2.getY().compareTo(o1.getY());
            if (countCompare == 0){
                return o2.getX().compareTo(o1.getX());
            }
            return countCompare;
        });
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
        System.out.println("Stopped file operation agent");
    }
}
