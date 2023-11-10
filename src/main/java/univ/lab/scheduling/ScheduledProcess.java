package univ.lab.scheduling;

public class ScheduledProcess {
    private State state;
    private int timesBreak;
    private int currentPriority;
    private int maxQuantum;
    private int currentQuantum;
    private int currentPhaseTimeElapsed;
    private int fullElapsedTime;
    private int timeToComplete;
    private int nextBlockTime;
    private int timeBlocking;
    private BlockTimeGenerator timeToBlockGenerator;
    private BlockTimeGenerator timeToWorkGenerator;
    private boolean toBoost;
    private int timeArrive;
    private String name;

    public State getState() {
        return state;
    }
    public void setState(State state) {
        this.state = state;
    }

    public ScheduledProcess() {
        resetPhase();
        fullElapsedTime = 0;
        state = State.READY;
    }

    public void start() {
        nextBlockTime = timeToWorkGenerator.generateTime();
        state = State.RUNNING;
    }

    public void nextTick() {
        currentPhaseTimeElapsed++;
        fullElapsedTime++;
        if (fullElapsedTime >= timeToComplete) {
            state = State.TERMINATED;
            resetPhase();
        }
        else if (currentPhaseTimeElapsed >= nextBlockTime) {
            state = State.BLOCKED;
            timeBlocking = timeToBlockGenerator.generateTime();
            setCurrentQuantum(currentPhaseTimeElapsed);
            resetPhase();
        }
        else if (currentPhaseTimeElapsed >= maxQuantum) {
            state = State.BLOCKED;
            timeBlocking = 0;
            setCurrentQuantum(currentPhaseTimeElapsed);
            resetPhase();
        }
    }

    private void resetPhase() {
        currentPhaseTimeElapsed = 0;
    }

    public void applyTime(int timeElapsed) {
        currentPhaseTimeElapsed += timeElapsed;
        if (currentPhaseTimeElapsed >= timeBlocking) {
            state = State.READY;
            resetPhase();
        }
    }

    public void setTimeToComplete(int timeToComplete) {
        this.timeToComplete = timeToComplete;
    }

    public void setTimeToBlockGenerator(BlockTimeGenerator timeToBlockGenerator) {
        this.timeToBlockGenerator = timeToBlockGenerator;
    }

    public void setTimeToWorkGenerator(BlockTimeGenerator timeToWorkGenerator) {
        this.timeToWorkGenerator = timeToWorkGenerator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    enum State {
        RUNNING, BLOCKED, READY, TERMINATED
    }

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

    public boolean isToBoost() {
        return toBoost;
    }

    public void setToBoost(boolean toBoost) {
        this.toBoost = toBoost;
    }

    public int getTimeArrive() {
        return timeArrive;
    }

    public void setTimeArrive(int timeArrive) {
        this.timeArrive = timeArrive;
    }
}
