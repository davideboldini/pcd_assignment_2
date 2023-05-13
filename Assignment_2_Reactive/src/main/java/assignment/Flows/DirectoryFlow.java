package assignment.Flows;

import assignment.Model.Directory;
import assignment.Model.Events.DirectoryEvent;
import assignment.Model.Events.FilesEvent;
import assignment.Utility.Analyser.SourceAnalyzerImpl;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DirectoryFlow {

    private Directory directory;

    public DirectoryFlow(final Directory directory){
        this.directory = directory;
    }

    public void observePath(){
        this.observeFile();
        this.observeDirectory();
    }

    private void observeDirectory(){
        /*
        Observable.create(emitter -> {
            new Thread(() -> {
                for (Directory d: directory.getDirectoryList()) {
                    emitter.onNext(d);
                }
                emitter.onComplete();
            }).start();
                })
                .blockingSubscribe(res -> {
                    SourceAnalyzerImpl.masterFlow.receiveEvents(new DirectoryEvent((Directory) res));
                });


         */

        Flowable.fromIterable(directory.getDirectoryList())
                .flatMap(list -> Flowable.just(list)
                        .subscribeOn(Schedulers.computation())
                        .map(dir -> {
                            return new DirectoryEvent(dir);
                        })
                )
                .blockingSubscribe(event -> {
                    SourceAnalyzerImpl.masterFlow.receiveEvents(event);
                });
    }



    private void observeFile(){
        Observable.just(directory)
                .map(d -> {
                    return d.getJavaFileList();
                }).subscribe(res -> {
                    System.out.println(Thread.currentThread());
                    SourceAnalyzerImpl.masterFlow.receiveEvents(new FilesEvent(res));
                });
    }

}
