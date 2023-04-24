package Threads;

import Monitor.FileMonitor;
import Monitor.IntervalMonitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileRangeThread implements Runnable{

    private final File file;
    private final FileMonitor fileMonitor;
    private final IntervalMonitor intervalMonitor;


    public FileRangeThread(final File file, final FileMonitor fileMonitor, final IntervalMonitor intervalMonitor){
        this.file = file;
        this.fileMonitor = fileMonitor;
        this.intervalMonitor = intervalMonitor;
    }

    @Override
    public void run() {
        try {
            Long numRows = this.countNumRows(file);
            this.fileMonitor.addFile(file, numRows);
            this.intervalMonitor.addElementInInterval(numRows);
        } catch (Exception ignored){
        }
    }

    private Long countNumRows(final File file) throws IOException {
        return Files.lines(Path.of(file.getPath())).count();
    }
}
