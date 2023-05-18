package assignment.Utility.Analyser;

import assignment.Agent.*;
import assignment.Agent.GUI.GuiFormAgent;
import assignment.Message.*;
import assignment.Message.Type.MessageType;
import assignment.Model.Directory;
import assignment.Utility.Codec.GenericCodec;
import assignment.Utility.Pair;
import io.vertx.core.*;
import io.vertx.core.eventbus.Message;
import io.vertx.core.shareddata.Counter;
import io.vertx.core.shareddata.SharedData;

import java.io.File;
import java.util.Map;
import java.util.TreeSet;

public class SourceAnalyzerImpl implements SourceAnalyzer {

    private Vertx vertx = Vertx.vertx();

    public SourceAnalyzerImpl(){
        this.initCodec();
    }

    @Override
    public Vertx getVertx(){
        return this.vertx;
    }

    private void initCodec(){
        vertx.eventBus().registerDefaultCodec(MessageDirectory.class,
                new GenericCodec<>(MessageDirectory.class));

        vertx.eventBus().registerDefaultCodec(MessageFile.class,
                new GenericCodec<>(MessageFile.class));

        vertx.eventBus().registerDefaultCodec(MessageFileLength.class,
                new GenericCodec<>(MessageFileLength.class));

        vertx.eventBus().registerDefaultCodec(MessageUpdate.class,
                new GenericCodec<>(MessageUpdate.class));
    }

    private void deployGuiVerticle(final GuiFormAgent guiForm){
        vertx.deployVerticle(guiForm);
    }



    private void deployDirectoryVerticles(final Promise<Directory> promiseDir, final int MAXL, final int NI){

        vertx.deployVerticle(new FileOperationAgent(MAXL, NI), res -> {
            System.out.println("File Length Verticle created");
        });

        vertx.deployVerticle(new DirectoryAgent(), res -> {
            System.out.println("Directory Verticle created");
            System.out.println("Send message in directory-topic");
            vertx.eventBus().publish("directory-topic", new MessageDirectory(promiseDir));
        });
    }



    @Override
    public Pair<Promise<TreeSet<Pair<File, Long>>>, Promise<Map<Pair<Integer,Integer>, Integer>>> getReport(final Directory d, final int MAXL, final int NI){

        Promise<Map<Pair<Integer,Integer>, Integer>> intervalMapPromise = Promise.promise();
        Promise<TreeSet<Pair<File, Long>>> fileTreePromise = Promise.promise();

        Promise<Directory> promiseDir = Promise.promise();
        this.deployDirectoryVerticles(promiseDir, MAXL, NI);


        promiseDir.complete(d);

        this.getVertx().eventBus().consumer("return-topic", (Message<MessageUpdate> message) -> {
            MessageUpdate mex = message.body();
            if (mex.getTypeMessage().equals(MessageType.FILE_LENGTH)) {
                fileTreePromise.complete(mex.getFileLengthMap());
            } else {
                intervalMapPromise.complete(mex.getIntervalMap());
            }
        });

        return new Pair<>(fileTreePromise, intervalMapPromise);

    }

    @Override
    public void analyzeSources(final Directory d, final int MAXL, final int NI, final GuiFormAgent guiForm) {

        this.restartVertx();
        this.deployGuiVerticle(guiForm);

        Promise<Directory> promiseDir = Promise.promise();
        this.deployDirectoryVerticles(promiseDir, MAXL, NI);
        promiseDir.complete(d);
    }

    @Override
    public void stopExecution(){
        this.vertx.close();
    }

    @Override
    public void restartVertx(){
        this.vertx = Vertx.vertx();
        this.initCodec();
    }

}
