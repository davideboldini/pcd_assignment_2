package assignment.Utility.Analyser;


import assignment.Agent.DirectoryAgent;
import assignment.Agent.FileAgent;
import assignment.Agent.FileLengthAgent;
import assignment.Agent.IntervalAgent;
import assignment.Message.MessageDirectory;
import assignment.Message.MessageFile;
import assignment.Message.MessageFileLength;
import assignment.Message.MessageInitInterval;
import assignment.Model.Directory;
import assignment.Utility.Codec.GenericCodec;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SourceAnalyzerImpl implements SourceAnalyzer{

    Vertx vertx = Vertx.vertx();

    public void getReport(final Directory d /*, final int MAXL, final int NI */){

        this.initCodec();

        vertx.deployVerticle(new FileLengthAgent(), res -> {
            System.out.println("File Length Verticle created");
        });

        /*
        vertx.deployVerticle(new IntervalAgent(), res -> {
            System.out.println("Interval Verticle created");
            vertx.eventBus().publish("init-interval-topic", new MessageInitInterval(MAXL, NI));
        });

         */

        vertx.deployVerticle(new DirectoryAgent(), res -> {
            System.out.println("Directory Verticle created");
            System.out.println("Send message in directory-topic");
            vertx.eventBus().publish("directory-topic", new MessageDirectory(d));
        });

        vertx.deployVerticle(new FileAgent(), res -> {
            System.out.println("File verticle created");
        });



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
    }

}
