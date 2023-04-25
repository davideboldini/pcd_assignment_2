package Threads;

import Model.Directory;
import Monitor.FileMonitor;
import Monitor.IntervalMonitor;

import java.io.File;
import java.util.*;

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

    private void getDirectoryList(final Directory dir){
        for (Directory directory: dir.getDirectoryList()) {
            Thread t = Thread.ofVirtual().unstarted(new DirectoryThread(new Directory(directory.getDirPath()), fileMonitor, intervalMonitor, controller));
            controller.addThread(t);
        }
    }

    private void getFileList(final Directory dir) {
        if (!dir.getJavaFileList().isEmpty()){
            for (File file: dir.getJavaFileList()) {
                Thread t = Thread.ofVirtual().unstarted(new FileRangeThread(file, fileMonitor, intervalMonitor));
                controller.addThread(t);
            }
        }
    }

    @Override
    public void run() {
        this.getDirectoryList(dir);
        this.getFileList(dir);
    }
}
