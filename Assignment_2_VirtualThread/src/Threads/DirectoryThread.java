package Threads;

import Model.Directory;
import Monitor.FileMonitor;
import Monitor.IntervalMonitor;

import java.io.File;

public class DirectoryThread implements Runnable{

    private final Directory dir;
    private final FileMonitor fileMonitor;
    private final IntervalMonitor intervalMonitor;
    private final ControllerThread controller;


    public DirectoryThread(final Directory dir, final FileMonitor fileMonitor, final IntervalMonitor intervalMonitor,
                           final ControllerThread controller){
        this.dir = dir;
        this.fileMonitor = fileMonitor;
        this.intervalMonitor = intervalMonitor;
        this.controller = controller;
    }

    private void getDirectoryList(){
        for (Directory directory: dir.getDirectoryList()) {
            Runnable r = new DirectoryThread(new Directory(directory.getDirPath()), fileMonitor, intervalMonitor, controller);
            controller.addThread(r);
        }
    }

    private void getFileList() {
        if (!dir.getJavaFileList().isEmpty()){
            for (File file: dir.getJavaFileList()) {
                Runnable r = new FileRangeThread(file, fileMonitor, intervalMonitor);
                controller.addThread(r);
            }
        }
    }

    @Override
    public void run() {
        this.getDirectoryList();
        this.getFileList();
    }

}
