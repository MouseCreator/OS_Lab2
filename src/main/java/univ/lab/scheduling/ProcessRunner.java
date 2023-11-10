package univ.lab.scheduling;

public class ProcessRunner {
    private ScheduledProcess process;
    private int timeStart;

    public void startProcess(ScheduledProcess process, int start) {
        this.process = process;
        timeStart = start;
        process.start();
    }

    public ScheduledProcess.State nextTick() {
        process.nextTick();
        return process.getState();
    }

    public int timeStart() {
        return timeStart;
    }
}
