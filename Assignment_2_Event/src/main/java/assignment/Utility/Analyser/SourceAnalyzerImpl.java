package assignment.Utility.Analyser;


import assignment.Agent.DirectoryAgent;
import assignment.Message.MessageDirectory;
import assignment.Message.MessageFile;
import assignment.Model.Directory;
import assignment.Utility.Codec.GenericCodec;
import io.vertx.core.Vertx;

public class SourceAnalyzerImpl implements SourceAnalyzer{

    public void getReport(Directory d){
        Vertx vertx = Vertx.vertx();

        vertx.eventBus().registerDefaultCodec(MessageDirectory.class,
                new GenericCodec<MessageDirectory>(MessageDirectory.class));

        vertx.eventBus().registerDefaultCodec(MessageFile.class,
                new GenericCodec<MessageFile>(MessageFile.class));

        vertx.deployVerticle(new DirectoryAgent(), res -> {
            System.out.println("Verticle created");
            System.out.println("Send message in directory-topic");
            vertx.eventBus().publish("directory-topic", new MessageDirectory(d));
        });

    }
}
