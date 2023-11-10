package univ.lab.scheduling;

public class ScheduledProcess {
    private int timesBreak;
    private int currentPriority;
    private int maxQuantum;
    private int currentQuantum;
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

    public int getMaxQuantum() {
        return maxQuantum;
    }

    public void setMaxQuantum(int maxQuantum) {
        this.maxQuantum = maxQuantum;
    }

    public int getCurrentQuantum() {
        return currentQuantum;
    }

    public void setCurrentQuantum(int currentQuantum) {
        this.currentQuantum = currentQuantum;
    }
}
