package assignment.Agent;

import assignment.Message.MessageFile;
import assignment.Message.MessageFileLength;
import assignment.Message.MessageGuiUpdate;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class FileLengthAgent extends AbstractVerticle {

    List<Long> fileLengthList = new ArrayList<>();
    //TreeSet<Pair<File, Long>> fileLengthTree;

    @Override
    public void start(final Promise<Void> startPromise) {
        System.out.println("File length agent started");
        //this.initTreeSet();

        EventBus eventBus = this.getVertx().eventBus();

        eventBus.consumer("file-length-topic", (Message<MessageFileLength> message) -> {
            MessageFileLength mex = message.body();
            //System.out.println("[File-length Agent] new message: " + mex.getFileLength());
            fileLengthList.add(mex.getFileLength());
            System.out.println(fileLengthList.size());
            //eventBus.publish("gui-update-topic", new MessageGuiUpdate(fileLengthTree, "File))
        });

        startPromise.complete();
    }

    private void initTreeSet(){
        /*
        this.fileLengthTree = new TreeSet<>((o1, o2) -> {
            int countCompare = o2.getY().compareTo(o1.getY());
            if (countCompare == 0){
                return o2.getX().compareTo(o1.getX());
            }
            return countCompare;
        });

         */
    }
}
