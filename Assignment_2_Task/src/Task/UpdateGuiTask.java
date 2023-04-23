package Task;

import Monitor.FileMonitor;
import Monitor.IntervalMonitor;
import Utility.Pair;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class UpdateGuiTask implements Runnable{

    private final FileMonitor fileMonitor;
    private final IntervalMonitor intervalMonitor;
    private final JTextArea textAreaFileLength;
    private final JTextArea textAreaInterval;
    private final int N;

    public UpdateGuiTask(final FileMonitor fileMonitor, final IntervalMonitor intervalMonitor,
                         final JTextArea textAreaFileLength, final JTextArea textAreaInterval, final int N){
        this.fileMonitor = fileMonitor;
        this.intervalMonitor = intervalMonitor;
        this.textAreaFileLength = textAreaFileLength;
        this.textAreaInterval = textAreaInterval;
        this.N = N;
    }

    private void updateFile(){
        TreeSet<Pair<File, Long>> fileSet = fileMonitor.getFileLengthMap();
        if (fileSet.size() > N){
            textAreaFileLength.setText("");
            List<Pair<File, Long>> fileList = fileSet.stream().toList().subList(0,N);
            fileList.forEach(pair -> textAreaFileLength.setText(textAreaFileLength.getText() + "\n" + "File: " + pair.getX().getName() + " - Len: " + pair.getY() + "\n"));
        }
    }

    private void updateInterval(){
        HashMap<Pair<Integer,Integer>, Integer> intervalMap = intervalMonitor.getIntervalMap();
        textAreaInterval.setText("");
        intervalMap.forEach((key, value) -> {
            if (key.getY().equals(-1)) {
                textAreaInterval.setText(textAreaInterval.getText() + "Intervallo [ " + key.getX() + " - inf ]: " + value + "\n");
            } else {
                textAreaInterval.setText(textAreaInterval.getText() + "Intervallo [ " + key.getX() + " - " + key.getY() + " ]: " + value + "\n");
            }
        });
    }

    @Override
    public void run() {
        updateFile();
        updateInterval();
    }
}
