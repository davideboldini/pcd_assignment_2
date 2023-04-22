package Task;

import Monitor.FileMonitor;
import Monitor.IntervalMonitor;
import Utility.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class FileTask implements Runnable{

    private final List<File> fileList;
    private final FileMonitor fileMonitor;
    private final IntervalMonitor intervalMonitor;

    public FileTask(final List<File> fileList, final FileMonitor fileMonitor, final IntervalMonitor intervalMonitor) {
        this.fileList = fileList;
        this.fileMonitor = fileMonitor;
        this.intervalMonitor = intervalMonitor;
    }

    @Override
    public void run() {
        List<Pair<File,Long>> intervalList = new LinkedList<>();
        try {
            for (File file : this.fileList){
                Long numRows = countNumRows(file);
                this.fileMonitor.addFile(file, numRows);
                this.intervalMonitor.addElementInInterval(numRows);
            }
        } catch (Exception ignored){}
    }

    private Long countNumRows(final File file) throws IOException {
        return Files.lines(Path.of(file.getPath())).count();
    }
}
