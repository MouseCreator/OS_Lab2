package univ.lab.scheduling;

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
    public ScheduledProcess.State run() {
        currentTimeRunning++;
        if (currentProcess == null) {
            throw new IllegalStateException("Process Manager is not initialized");
        }
        currentProcess.getScheduledProcess().nextTick();
        ScheduledProcess.State state = currentProcess.getScheduledProcess().getState();
        switch (state) {
            case READY -> throw new IllegalStateException("Process is running in READY state");
            case TERMINATED, BLOCKED -> {
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
