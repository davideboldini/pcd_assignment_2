package assignment.Agent;

import assignment.Message.MessageGuiUpdate;
import assignment.Utility.Pair;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

import java.io.File;
import java.util.HashMap;
import java.util.TreeSet;

public class GuiUpdateAgent extends AbstractVerticle {

    @Override
    public void start(){
        System.out.println("Gui update agent started");
        EventBus eventBus = this.getVertx().eventBus();

        eventBus.consumer("gui-update-topic", (Message<MessageGuiUpdate> message) -> {
            MessageGuiUpdate mex = message.body();
            if (mex.getTypeMessage().equals("File length mex")){
                TreeSet<Pair<File, Long>> fileLengthTree = mex.getFileLengthMap();
            }else {
                HashMap<Pair<Integer,Integer>, Integer> intervalMap = mex.getIntervalMap();
            }
        });
    }
}
