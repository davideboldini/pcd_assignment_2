package Task;

import Model.Directory;
import Monitor.FileMonitor;
import Monitor.IntervalMonitor;

import java.io.File;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DirectoryTask {

    private final FileMonitor fileMonitor;
    private final IntervalMonitor intervalMonitor;
    private final ExecutorService executor;

    public DirectoryTask(final Directory directory, final ExecutorService executor, final FileMonitor fileMonitor,
                         final IntervalMonitor intervalMonitor){
        this.fileMonitor = fileMonitor;
        this.executor = executor;
        this.intervalMonitor = intervalMonitor;
        this.getDirectoryList(directory);
    }

    private void getDirectoryList(final Directory dir){
        List<Directory> dirList = dir.getDirectoryList();
        dirList.forEach(this::getDirectoryList);
        this.getFileList(dir);
    }

    private void getFileList(final Directory dir) {
        List<File> fileList = dir.getJavaFileList();
        try {
            for (File file: fileList) {
                executor.execute(new FileTask(file, fileMonitor, intervalMonitor));
            }
        }catch (Exception ignored){}

    }

}
