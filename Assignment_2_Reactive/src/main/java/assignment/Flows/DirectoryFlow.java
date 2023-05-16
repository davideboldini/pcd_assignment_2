package assignment.Flows;

import assignment.Model.Directory;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.File;

public class DirectoryFlow {


    private Observable<Directory> observeDirectory(final Directory directory){

        return Observable.merge(
                Observable.just(directory),
                Observable.fromIterable(directory.getDirectoryList())
                        .flatMap(this::observeDirectory)
        ).subscribeOn(Schedulers.io());
    }

    public Observable<File> observeAllFiles(final Directory directory){
        return this.observeDirectory(directory).flatMap(dir -> Observable.fromIterable(dir.getJavaFileList()));
    }

}
