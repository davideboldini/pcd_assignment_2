package assignment.Agent;

import assignment.Message.MessageFile;
import assignment.Message.MessageFileLength;
import assignment.Message.Type.MessageType;
import assignment.Message.MessageUpdate;
import assignment.Utility.Pair;
import io.vertx.core.AbstractVerticle;
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

    public FileOperationAgent(){
        this.initTreeSet();
    }

    @Override
    public void start(final Promise<Void> startPromise) {
        System.out.println("File operation agent started");

        final SharedData sd = this.getVertx().sharedData();

        EventBus eventBus = this.getVertx().eventBus();

        eventBus.consumer("file-topic", (Message<MessageFile> message) -> {
            MessageFile mex = message.body();

            List<Long> fileLengthList = new ArrayList<>();

            for (File file: mex.getListFile()) {
                Long numRows = this.countNumRows(file);
                this.fileLengthTree.add(new Pair<>(file, numRows));
                fileLengthList.add(numRows);
            }

            eventBus.publish("interval-topic", new MessageFileLength(fileLengthList));
            eventBus.publish("gui-update-topic",  new MessageUpdate(new TreeSet<>(fileLengthTree), MessageType.FILE_LENGTH));

            sd.getCounter("numDir", ar -> {
                Counter counter = ar.result();
                counter.decrementAndGet(val -> {
                    if (val.result() == 0){
                        System.out.println("End");
                        eventBus.publish("end-topic", null);
                    }
                });
            });
        });

        eventBus.consumer("end-topic", res -> {
            eventBus.publish("return-topic", new MessageUpdate(new TreeSet<>(fileLengthTree), MessageType.FILE_LENGTH));
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

    @Override
    public void stop(){
        System.out.println("Stopped file operation agent");
    }
}
