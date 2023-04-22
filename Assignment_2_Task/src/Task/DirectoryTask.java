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

    private final int nInterval;
    private final Directory directory;
    private final FileMonitor fileMonitor;
    private final IntervalMonitor intervalMonitor;
    private final ExecutorService executor;
    //private final CountDownLatch latch;

    public DirectoryTask(final Directory directory, final ExecutorService executor, final int nInterval, final FileMonitor fileMonitor,
                         final IntervalMonitor intervalMonitor){
        this.directory = directory;
        this.fileMonitor = fileMonitor;
        this.executor = executor;
        this.nInterval = nInterval;
        this.intervalMonitor = intervalMonitor;
        this.getDirectoryList(directory);
    }

    private void getDirectoryList(final Directory dir){
        List<Directory> dirList = dir.getDirectoryList();
        dirList.forEach(this::getDirectoryList);
        getFileList(dir);
    }

    private void getFileList(final Directory dir) {
        List<File> fileList = dir.getJavaFileList();

        if (!fileList.isEmpty()){

            int sizeInterval = (fileList.size() / nInterval) + 1;

            List<File> tempList = new ArrayList<>(fileList.stream().toList());

            while(!tempList.isEmpty()){
                List<File> subList = new LinkedList<>();
                if (tempList.size() < 2*sizeInterval || tempList.size() == sizeInterval) {
                    subList.addAll(tempList);
                    tempList.clear();
                } else {
                    subList.addAll(tempList.subList(0, sizeInterval));
                    tempList.subList(0, sizeInterval).clear();
                }
                executor.execute(new FileTask(subList, fileMonitor, intervalMonitor));
            }
        }
    }
}
