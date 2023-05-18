package assignment.Flows;

import assignment.Utility.Analyser.SourceAnalyzerImpl;
import assignment.Utility.Pair;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Flow;

public class FileFlow {

    public Observable<Pair<File,Long>> observeFile(final File file){
        return Observable.just(file)
                .observeOn(Schedulers.computation())
                .flatMap(f -> Observable.just(new Pair<>(file, this.countNumRows(file))));
    }

    private Long countNumRows(final File file) {
        try {
            return Files.lines(Path.of(file.getPath())).count();
        } catch (IOException ignored) {
        }
        return null;
    }
}
