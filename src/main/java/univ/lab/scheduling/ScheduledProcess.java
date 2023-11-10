package univ.lab.scheduling;

public class ScheduledProcess {
    private int timesBreak;
    private int currentPriority;

    public int getTimesBreak() {
        return timesBreak;
    }

    public void setTimesBreak(int timesBreak) {
        this.timesBreak = timesBreak;
    }

    public int getCurrentPriority() {
        return currentPriority;
    }

    public void setCurrentPriority(int currentPriority) {
        this.currentPriority = currentPriority;
    }
}
