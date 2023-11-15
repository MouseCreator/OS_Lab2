package univ.lab.scheduling;

import java.io.PrintStream;

public class ProcessManager {
    public RunningProcess currentProcess;
    private int quantumDuration;
    private int currentTimeRunning = 0;
    public void startProcess(RunningProcess process) {
        this.currentProcess = process;
        currentTimeRunning = 0;
        process.start();
    }

    public void setQuantumDuration(int quantumDuration) {
        this.quantumDuration = quantumDuration;
    }

    public boolean isEmpty() {
        return currentProcess == null;
    }
    public ScheduledProcess.State run(PrintStream outStream) {
        currentTimeRunning++;
        if (currentProcess == null) {
            throw new IllegalStateException("Process Manager is not initialized");
        }
        currentProcess.getScheduledProcess().nextTick();
        ScheduledProcess.State state = currentProcess.getScheduledProcess().getState();
        String name = currentProcess.getScheduledProcess().getName();
        switch (state) {
            case READY -> throw new IllegalStateException("Process is running in READY state");
            case TERMINATED -> {
                outStream.println("Process " + name + " was terminated!");
                return state;
            }
            case BLOCKED -> {
                outStream.println("Process" + name + " was blocked!");
                return state;
            }
            case RUNNING -> {
                //continue
            }
        }

        if (currentTimeRunning >= quantumDuration) {
            currentProcess.getScheduledProcess().stop();
            return ScheduledProcess.State.TIMEOUT;
        }
        return ScheduledProcess.State.RUNNING;
    }


    public int getRuntime() {
        return currentTimeRunning;
    }
}
