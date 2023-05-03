package assignment.Agent;

import assignment.Message.MessageFile;
import assignment.Message.MessageFileLength;
import assignment.Message.MessageUpdate;
import assignment.Utility.Pair;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileOperationAgent extends AbstractVerticle {

    private TreeSet<Pair<File, Long>> fileLengthTree;
    private List<Long> fileLengthList;

    @Override
    public void start(final Promise<Void> startPromise) {
        System.out.println("File operation agent started");
        this.initTreeSet();

        EventBus eventBus = this.getVertx().eventBus();

        eventBus.consumer("file-topic", (Message<MessageFile> message) -> {
            MessageFile mex = message.body();

            this.fileLengthList = new ArrayList<>();

            for (File file: mex.getListFile()) {
                Long numRows = this.countNumRows(file);
                fileLengthTree.add(new Pair<>(file, numRows));
                fileLengthList.add(numRows);
            }

            eventBus.publish("remove-dir-topic", null);
            eventBus.publish("interval-topic", new MessageFileLength(fileLengthList));
            eventBus.publish("gui-update-topic",  new MessageUpdate(new TreeSet<>(fileLengthTree), "File length mex"));

        });

        eventBus.consumer("get-fileTree-topic", res -> {
            eventBus.publish("return-topic", new MessageUpdate(new TreeSet<>(fileLengthTree), "File length mex"));
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
        //System.out.println(vertx.deploymentIDs().size());
        System.out.println("Stopped file operation agent");
    }
}
