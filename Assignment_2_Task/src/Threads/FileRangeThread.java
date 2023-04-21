package Threads;

import Monitor.FileMonitor;
import Monitor.IntervalMonitor;
import Monitor.ThreadMonitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileRangeThread implements Runnable{

    private final List<File> fileList;
    private final FileMonitor fileMonitor;
    private final IntervalMonitor intervalMonitor;
    private final ThreadMonitor threadMonitor;


    public FileRangeThread(final List<File> fileList, final FileMonitor fileMonitor, final IntervalMonitor intervalMonitor,
                           final ThreadMonitor threadMonitor){
        this.fileList = fileList;
        this.fileMonitor = fileMonitor;
        this.intervalMonitor = intervalMonitor;
        this.threadMonitor = threadMonitor;
    }

    @Override
    public void run() {
        try {
            for (File file : this.fileList){
                Long numRows = this.countNumRows(file);
                //Thread.sleep(ThreadLocalRandom.current().nextInt(500, 800));
                this.fileMonitor.addFile(file, numRows);
                //Thread.sleep(ThreadLocalRandom.current().nextInt(500, 800));
                this.intervalMonitor.addElementInInterval(numRows);
            }

        } catch (Exception ignored){
            threadMonitor.putThreadInList();
        }
        threadMonitor.putThreadInList();
    }

    private Long countNumRows(final File file) throws IOException {
        return Files.lines(Path.of(file.getPath())).count();
    }
}
