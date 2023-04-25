package utility.Analyser;

import Controller.GuiController;
import Model.Directory;
import Monitor.FileMonitor;
import Monitor.GuiObserver;
import Monitor.IntervalMonitor;
import Threads.ControllerThread;
import Threads.DirectoryThread;
import utility.Pair;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SourceAnalyzerImpl implements SourceAnalyzer{

    private int N;
    private FileMonitor fileMonitor;
    private IntervalMonitor intervalMonitor;
    private ControllerThread controller;
    private GuiController guiController;
    Thread guiControllerTh;

    public SourceAnalyzerImpl(final GuiObserver guiObserver){
        this.guiController = new GuiController();
        this.guiController.addObserver(guiObserver);
    }

    public SourceAnalyzerImpl(){}

    @Override
    public void initSource(final int MAXL, final int NI, final int N){
        this.N = N;
        this.fileMonitor = new FileMonitor();
        this.intervalMonitor = new IntervalMonitor(MAXL, NI);
        this.controller = new ControllerThread();

        fileMonitor.addObserver(guiController);
        intervalMonitor.addObserver(guiController);
    }


    @Override
    public Pair<Map<Pair<Integer, Integer>, Integer>, List<Pair<File,Long>>> getReport(Directory d) throws InterruptedException {
        Thread controllerTh = Thread.ofVirtual().unstarted(controller);
        Thread firstDir = Thread.ofVirtual().unstarted(new DirectoryThread(d, fileMonitor, intervalMonitor, controller));

        controller.addThread(firstDir);
        controllerTh.start();
        controllerTh.join();

        List<Pair<File, Long>> maxFile = fileMonitor.getFileLengthMap().stream().toList().subList(0,N);
        Map<Pair<Integer, Integer>, Integer> intervalMap = intervalMonitor.getIntervalMap();

        return new Pair<>(intervalMap, maxFile);
    }


    @Override
    public void analyzeSources(Directory d) throws InterruptedException {
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
        Thread controllerTh = Thread.ofVirtual().unstarted(controller);
        Thread firstDir = Thread.ofVirtual().unstarted(new DirectoryThread(d, fileMonitor, intervalMonitor, controller));

        executorService.execute(() -> {
            guiController.processStart();
            controller.addThread(firstDir);
            controller.addGuiThread(guiController);
            controllerTh.start();
            try {
                controllerTh.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void stopAnalyze(){
        //guiController.processStop();
        controller.stopThreads();
    }

    @Override
    public void addGuiObserver(final GuiObserver observer){
        this.guiController.addObserver(observer);
    }

}
