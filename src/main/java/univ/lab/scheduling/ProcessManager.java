package univ.lab.scheduling;

public class ProcessManager {
    public ScheduledProcess currentProcess;
    private int quantumDuration;
    private int currentTimeRunning = 0;
    public void startProcess(ScheduledProcess process) {
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
        currentProcess.nextTick();

        ScheduledProcess.State state = currentProcess.getState();
        switch (state) {
            case READY -> throw new IllegalStateException("Process is running in READY state");
            case TERMINATED, BLOCKED -> {
                reset();
                return state;
            }
            case RUNNING -> {
                //continue
            }
        }

        if (currentTimeRunning >= quantumDuration) {
            currentProcess.stop();
            reset();
            return ScheduledProcess.State.TIMEOUT;
        }
        return state;
    }

    private void reset() {
        this.currentProcess = null;
    }

    public int getRuntime() {
        return currentTimeRunning;
    }
}
