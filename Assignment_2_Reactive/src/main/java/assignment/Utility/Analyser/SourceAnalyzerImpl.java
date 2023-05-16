package assignment.Utility.Analyser;


import assignment.Flows.*;
import assignment.GUI.Gui;
import assignment.Model.Directory;
import assignment.Utility.Chrono;
import assignment.Utility.Pair;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class SourceAnalyzerImpl implements SourceAnalyzer {

    public DataStructureFlow dataStructureFlow;

    public CompletableFuture<Pair<TreeSet<Pair<File, Long>>, Map<Pair<Integer,Integer>, Integer>>> getReport(final Directory d, final int MAXL, final int NI){
        return CompletableFuture.supplyAsync(() -> {

            FileLengthStructure fileLengthStructure = new FileLengthStructure();
            IntervalDataStructure intervalStructure = new IntervalDataStructure(MAXL, NI);

            Observable<File> files = new DirectoryFlow().observeAllFiles(d);
            Observable<Pair<File, Long>> pairObservable = files.flatMap(f -> new FileFlow().observeFile(f));

            pairObservable.blockingSubscribe(pair -> {
                fileLengthStructure.addElement(pair);
                intervalStructure.addElement(pair.getY());
            });

            return new Pair<>(fileLengthStructure.getFileLengthTree(), intervalStructure.getIntervalMap());
        });

    }


    public void analyzeSources(final Directory d, final int MAXL, final int NI,
                               final PublishSubject<Pair<TreeSet<Pair<File, Long>>, Map<Pair<Integer,Integer>, Integer>>> guiPublishSubject){
        FileLengthStructure fileLengthStructure = new FileLengthStructure();
        IntervalDataStructure intervalStructure = new IntervalDataStructure(MAXL, NI);

        Observable<File> files = new DirectoryFlow().observeAllFiles(d);
        Observable<Pair<File, Long>> pairObservable = files.observeOn(Schedulers.computation()).flatMap(f -> new FileFlow().observeFile(f));

        /*
        pairObservable.subscribe((pair -> {
            fileLengthStructure.addElement(pair);
            intervalStructure.addElement(pair.getY());
            guiPublishSubject.onNext(new Pair<>(new TreeSet<>(fileLengthStructure.getFileLengthTree()), new HashMap<>(intervalStructure.getIntervalMap())));
        }), Throwable::printStackTrace);

         */

        pairObservable.subscribeWith(new Observer<Pair<File, Long>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Pair<File, Long> fileLongPair) {
                fileLengthStructure.addElement(fileLongPair);
                intervalStructure.addElement(fileLongPair.getY());
                guiPublishSubject.onNext(new Pair<>(new TreeSet<>(fileLengthStructure.getFileLengthTree()), new HashMap<>(intervalStructure.getIntervalMap())));
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                try {
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                guiPublishSubject.onComplete();
            }
        });

    }

}
