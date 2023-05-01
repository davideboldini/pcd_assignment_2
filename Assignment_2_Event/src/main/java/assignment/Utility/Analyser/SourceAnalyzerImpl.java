package assignment.Utility.Analyser;


import assignment.Agent.*;
import assignment.Agent.GUI.GuiFormAgent;
import assignment.Message.*;
import assignment.Model.Directory;
import assignment.Utility.Codec.GenericCodec;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class SourceAnalyzerImpl implements SourceAnalyzer{

    private Vertx vertx = Vertx.vertx();

    public SourceAnalyzerImpl(){
        this.initCodec();
    }

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

        vertx.eventBus().registerDefaultCodec(MessageGuiUpdate.class,
                new GenericCodec<MessageGuiUpdate>(MessageGuiUpdate.class));
    }

    private void deployIntervalVerticle(final int MAXL, final int NI){
        DeploymentOptions options = new DeploymentOptions().setWorker(true);
        vertx.deployVerticle(new IntervalAgent(), options, res -> {
            System.out.println("Interval Verticle created");
            vertx.eventBus().publish("init-interval-topic", new MessageInitInterval(MAXL, NI));
        });
    }

    public void deployGuiVerticle(final GuiFormAgent guiForm){
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

    public void getReport(final Directory d, final int MAXL, final int NI){

        this.deployIntervalVerticle(MAXL, NI);
        this.deployDirectoryVerticles(d);

    }

    public void analyzeSources(Directory d, final int MAXL, final int NI, final GuiFormAgent guiForm) {

        this.deployGuiVerticle(guiForm);
        this.deployIntervalVerticle(MAXL, NI);
        this.deployDirectoryVerticles(d);

    }

    public void stopExecution(){
        vertx.close();
    }

    public void restartVertx(){
        vertx = Vertx.vertx();
        this.initCodec();
    }

}
