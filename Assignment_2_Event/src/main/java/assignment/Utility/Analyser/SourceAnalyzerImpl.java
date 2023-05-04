package assignment.Utility.Analyser;


import assignment.Agent.*;
import assignment.Agent.GUI.GuiFormAgent;
import assignment.Message.*;
import assignment.Model.Directory;
import assignment.Utility.Codec.GenericCodec;
import assignment.Utility.Pair;
import io.vertx.core.*;
import io.vertx.core.eventbus.Message;

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
                new GenericCodec<MessageDirectory>(MessageDirectory.class));

        vertx.eventBus().registerDefaultCodec(MessageFile.class,
                new GenericCodec<MessageFile>(MessageFile.class));

        vertx.eventBus().registerDefaultCodec(MessageFileLength.class,
                new GenericCodec<MessageFileLength>(MessageFileLength.class));

        vertx.eventBus().registerDefaultCodec(MessageInitInterval.class,
                new GenericCodec<MessageInitInterval>(MessageInitInterval.class));

        vertx.eventBus().registerDefaultCodec(MessageUpdate.class,
                new GenericCodec<MessageUpdate>(MessageUpdate.class));
    }

    private void deployIntervalVerticle(final int MAXL, final int NI){
        vertx.deployVerticle(new IntervalAgent(), res -> {
            System.out.println("Interval Verticle created");
            vertx.eventBus().publish("init-interval-topic", new MessageInitInterval(MAXL, NI));
        });
    }

    private void deployGuiVerticle(final GuiFormAgent guiForm){
        DeploymentOptions options = new DeploymentOptions().setWorker(true);
        vertx.deployVerticle(guiForm, options);
    }

    private void deployDirectoryVerticles(final Directory d){

        vertx.deployVerticle(new FileOperationAgent(), res -> {
            System.out.println("File Length Verticle created");
        });

        vertx.deployVerticle(new DirectoryAgent(), res -> {
            System.out.println("Directory Verticle created");
            System.out.println("Send message in directory-topic");
            vertx.eventBus().publish("directory-topic", new MessageDirectory(d));
        });
    }

    private void deployControllerVerticle(){
        vertx.deployVerticle(new ControllerAgent(), res -> {
            System.out.println("Controller Verticle created");
        });
    }

    @Override
    public Pair<Promise<TreeSet<Pair<File, Long>>>, Promise<Map<Pair<Integer,Integer>, Integer>>> getReport(final Directory d, final int MAXL, final int NI){

        Promise<Map<Pair<Integer,Integer>, Integer>> intervalMapPromise = Promise.promise();
        Promise<TreeSet<Pair<File, Long>>> fileTreePromise = Promise.promise();

        this.deployControllerVerticle();
        this.deployIntervalVerticle(MAXL, NI);
        this.deployDirectoryVerticles(d);

        this.getVertx().eventBus().consumer("return-topic", (Message<MessageUpdate> message) -> {
            MessageUpdate mex = message.body();
            if (mex.getTypeMessage().equals("File length mex")) {
                fileTreePromise.complete(mex.getFileLengthMap());
            } else {
                intervalMapPromise.complete(mex.getIntervalMap());
            }
        });

        return new Pair<>(fileTreePromise, intervalMapPromise);

    }

    @Override
    public void analyzeSources(final Directory d, final int MAXL, final int NI, final GuiFormAgent guiForm) {
        this.deployControllerVerticle();
        this.deployGuiVerticle(guiForm);
        this.deployIntervalVerticle(MAXL, NI);
        this.deployDirectoryVerticles(d);
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
