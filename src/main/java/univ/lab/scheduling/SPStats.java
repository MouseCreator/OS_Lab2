package univ.lab.scheduling;

public class SPStats {
    private int timesBlocked = 0;
    private int timesBoosted = 0;
    private int totalTimeBlocked = 0;
    private int totalTimeRunning = 0;

    public int getTimesBlocked() {
        return timesBlocked;
    }

    public int getTimesBoosted() {
        return timesBoosted;
    }

    public int getTotalTimeBlocked() {
        return totalTimeBlocked;
    }

    public int getTotalTimeRunning() {
        return totalTimeRunning;
    }
    public void addBlocked() {
        timesBlocked++;
    }
    public void addBoosted() {
        timesBoosted++;
    }
    public void addTotalTimeBlocked(int time) {
        totalTimeBlocked += time;
    }
    public void addTotalTimeRunning(int time) {
        totalTimeRunning += time;
    }
}
