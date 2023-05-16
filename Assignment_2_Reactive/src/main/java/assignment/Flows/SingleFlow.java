package assignment.Flows;

import assignment.Model.Directory;
import assignment.Utility.Pair;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SingleFlow {

    private final DataStructureFlow dataStructureFlow;

    public SingleFlow(final DataStructureFlow dataStructureFlow){
        this.dataStructureFlow = dataStructureFlow;
    }

    public void observePath(final Directory dir){
        this.observeFiles(dir);
        Observable.create(emitter -> {
            for (Directory d : dir.getDirectoryList()) {
                emitter.onNext(d);
            }
            emitter.onComplete();
        }).subscribe(res -> {
           this.observePath((Directory) res);
        });
    }

    public void observeFiles(final Directory dir){
        Observable.create(emitter -> {
            for (File f : dir.getJavaFileList()) {
                emitter.onNext(f);
            }
            emitter.onComplete();
        })//.observeOn(Schedulers.computation())
                .subscribe(res -> {
            //dataStructureFlow.getFileLengthTree().add(new Pair<>(f, numRows));
            //System.out.println(dataStructureFlow.getFileLengthTree().size());
            dataStructureFlow.fillStructureFlow((File) res);
        });
    }


}
