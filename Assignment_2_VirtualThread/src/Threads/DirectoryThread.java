package Threads;

import Monitor.FileMonitor;
import Monitor.IntervalMonitor;

import java.io.File;
import java.util.*;

public class DirectoryThread implements Runnable{

    private final String dirName;
    private final FileMonitor fileMonitor;
    private final IntervalMonitor intervalMonitor;
    private final ControllerThread controller;


    public DirectoryThread(final String dirName, final FileMonitor fileMonitor, final IntervalMonitor intervalMonitor,
                           final ControllerThread controller){
        this.dirName = dirName;
        this.fileMonitor = fileMonitor;
        this.intervalMonitor = intervalMonitor;
        this.controller = controller;
    }

    private void getDirectoryList(final String dirName){
        File thisDir = new File(dirName);

        List<File> listFile = List.of(Objects.requireNonNull(thisDir.listFiles()));

        listFile.stream().filter(File::isDirectory).toList().stream().map(dir -> Thread.ofVirtual().unstarted(new DirectoryThread(dir.getAbsolutePath(), fileMonitor, intervalMonitor, controller))).forEach(controller::addThread);
    }

    private void getFileList(final String dirName) {
        List<File> fileList = new ArrayList<>(Arrays.stream(Objects.requireNonNull(new File(dirName).listFiles((dir, name) -> name.endsWith(".java")))).toList());

        if (!fileList.isEmpty()){

            /*
            LinkedList<Thread> threadList = threadMonitor.getThreadsFile();

            int numInterval = threadList.size();
            int sizeInterval = (fileList.size() / numInterval) + 1;

            List<File> tempList = new ArrayList<>(fileList.stream().toList());

            while(!tempList.isEmpty() && !threadList.isEmpty()){
                List<File> subList = new LinkedList<>();
                if (tempList.size() < 2*sizeInterval || tempList.size() == sizeInterval) {
                    subList.addAll(tempList);
                    tempList.clear();
                } else {
                    subList.addAll(tempList.subList(0, sizeInterval));
                    tempList.subList(0, sizeInterval).clear();
                }
                Thread t = threadList.poll();
                Runnable r = new FileRangeThread(subList, fileMonitor, intervalMonitor, threadMonitor);
                t = new Thread(r);
                controller.addThread(t);
            }

             */
            for (File file: fileList) {
                Thread t = Thread.ofVirtual().unstarted(new FileRangeThread(file, fileMonitor, intervalMonitor));
                controller.addThread(t);
            }
        }
    }

    @Override
    public void run() {
        this.getDirectoryList(dirName);
        getFileList(dirName);
    }
}
