package univ.lab.scheduling;

import java.io.PrintStream;

public class ProcessManager {
    public RunningProcess currentProcess;
    private int quantumDuration;
    private int currentTimeRunning = 0;
    public void startProcess(RunningProcess process, PrintStream outStream) {
        this.currentProcess = process;
        currentTimeRunning = 0;
        outStream.printf("Process %s started! Current priority: %d. Current quantum remain: %d. Current breaks: %d\n",
                name(), currentProcess.getCurrentPriority(), currentProcess.getQuantumRemain(), currentProcess.getBreaks());
        process.start();
    }

    public void setQuantumDuration(int quantumDuration) {
        this.quantumDuration = quantumDuration;
    }
    public ScheduledProcess.State run(PrintStream outStream) {
        currentTimeRunning++;
        if (currentProcess == null) {
            throw new IllegalStateException("Process Manager is not initialized");
        }
        currentProcess.getScheduledProcess().nextTick();
        ScheduledProcess.State state = currentProcess.getScheduledProcess().getState();

        switch (state) {
            case READY -> throw new IllegalStateException("Process is running in READY state");
            case TERMINATED -> {
                outStream.println("Process " + name() + " was terminated!");
                return state;
            }
            case BLOCKED -> {
                outStream.println("Process " + name() + " was blocked!");
                currentProcess.setWasBlocked(true);
                return state;
            }
            case RUNNING -> {
                //continue
            }
        }

        if (currentTimeRunning >= quantumDuration) {
            currentProcess.getScheduledProcess().stop();
            outStream.println("Process " + name() + " used quantum!");
            return ScheduledProcess.State.TIMEOUT;
        }
        return ScheduledProcess.State.RUNNING;
    }

    private String name() {
        if (currentProcess == null) {
            throw new IllegalStateException("Called name when process is not initialized!");
        }
        return currentProcess.getScheduledProcess().getName();
    }


    public int getRuntime() {
        return currentTimeRunning;
    }
}
