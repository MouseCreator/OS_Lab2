package univ.lab.scheduling;

public class ProcessRunner {
    private int timeEnd;
    private ScheduledProcess process;
    private int timeStart;

    public static ProcessRunner runProcess(ScheduledProcess process, int start) {
        ProcessRunner runner = new ProcessRunner();
        runner.process = process;
        runner.timeEnd = start + process.getMaxQuantum();
        runner.timeStart = start;
        return runner;
    }

    public ScheduledProcess.State run() {
        process.nextTick();
        return process.getState();
    }

    public boolean isFinished(int currentTimeStamp) {
        return currentTimeStamp > timeEnd;
    }

    public int timeStart() {
        return timeStart;
    }
}
