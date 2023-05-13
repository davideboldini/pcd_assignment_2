package assignment.Flows;

import assignment.Model.Directory;
import assignment.Model.Events.Event;
import assignment.Model.Events.EventEnum;
import assignment.Utility.Analyser.SourceAnalyzerImpl;
import assignment.Utility.Pair;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.File;
import java.util.List;

public class MasterFlow {


    public void receiveEvents(final Event event){
        /*
        if (event.getEventType().equals(EventEnum.DIRECTORY)){
            this.directoryEvent((Directory) event.get());
        } else if (event.getEventType().equals(EventEnum.FILE)){
            this.fileListEvent((List<File>) event.get());
        } else {
            this.rowsEvent((Pair<File, Long>) event.get());
        }

         */


        Observable<Pair<EventEnum, ?>> source = Observable.just(event)
                .map(e -> {
                    if (event.getEventType().equals(EventEnum.DIRECTORY)){
                        //this.directoryEvent((Directory) event.get());
                        return new Pair<>(EventEnum.DIRECTORY, e.get());
                    } else if (event.getEventType().equals(EventEnum.FILE)){
                        //this.fileListEvent((List<File>) event.get());
                        return new Pair<>(EventEnum.FILE, e.get());
                    } else {
                        //this.rowsEvent((Pair<File, Long>) event.get());
                        return new Pair<>(EventEnum.ROWS, e.get());
                    }
                });

        source.blockingSubscribe(res -> {
            if(res.getX().equals(EventEnum.DIRECTORY)){
                this.directoryEvent((Directory) res.getY());
            } else if (event.getEventType().equals(EventEnum.FILE)){
                this.fileListEvent((List<File>) res.getY());
            } else {
                this.rowsEvent((Pair<File, Long>) res.getY());
            }
        });

    }

    private void directoryEvent(final Directory d){
        Observable<DirectoryFlow> source = Observable.just(d)
                //.subscribeOn(Schedulers.computation())
                .map(dir -> {
                   return new DirectoryFlow(d);
                });
        source.subscribe(res -> {
           res.observePath();
        });
    }

    private void fileListEvent(final List<File> listFile){
        Observable<FileFlow> source = Observable.just(listFile)
                .map(dir -> {
                    return new FileFlow(listFile);
                });

        source.subscribe(res -> {
            res.observeFileList();
        });
    }

    private void rowsEvent(final Pair<File, Long> pairFileRows){
        SourceAnalyzerImpl.dataStructureFlow.fillStructureFlow(pairFileRows);
    }
}
