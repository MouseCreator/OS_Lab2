package univ.lab.scheduling;

public class RunningProcess {
    private ScheduledProcess scheduledProcess;
    private int currentQuantum;
    private int timesBreak;
    private int currentPriority;
    private boolean boosted;
    private boolean wasBlocked;
    public static RunningProcess fromScheduledProcess(SimpleScheduler roundRobinScheduler, ScheduledProcess process) {
        RunningProcess runningProcess = new RunningProcess();
        runningProcess.scheduledProcess = process;
        runningProcess.timesBreak = 0;
        runningProcess.currentQuantum = roundRobinScheduler.getMaxQuantum();
        runningProcess.currentPriority = roundRobinScheduler.getPriority();
        return runningProcess;
    }
    public ScheduledProcess getScheduledProcess() {
        return scheduledProcess;
    }
    public void provideQuantum(int count){
        currentQuantum = count;
    }
    public void useQuantum() {
        currentQuantum--;
    }
    public boolean usedProvidedQuantum() {
        return currentQuantum == 0;
    }
    public void addBreak() {
        timesBreak++;
    }
    public void resetBreaks() {
        timesBreak = 0;
    }

    public int getCurrentPriority() {
        return currentPriority;
    }

    public void setCurrentPriority(int currentPriority) {
        this.currentPriority = currentPriority;
    }

    public boolean isBlocked() {
        return scheduledProcess.getState()== ScheduledProcess.State.BLOCKED;
    }

    public int getBreaks() {
        return timesBreak;
    }

    public void start() {
        this.scheduledProcess.start();
        useQuantum();
    }

    public boolean isReady() {
        return scheduledProcess.getState()== ScheduledProcess.State.READY;
    }

    public int getQuantumRemain() {
        return currentQuantum;
    }

    public void setBoosted(boolean boosted) {
        this.boosted = boosted;
    }
    public boolean getBoosted() {
        return boosted;
    }

    public boolean wasBlocked() {
        return wasBlocked;
    }

    public void setWasBlocked(boolean wasBlocked) {
        this.wasBlocked = wasBlocked;
    }
}
