package Monitor;

public interface ModelObserver {


    void modelFileUpdated(FileMonitor fileMonitor);

    void modelIntervalUpdated(IntervalMonitor intervalMonitor);
}
