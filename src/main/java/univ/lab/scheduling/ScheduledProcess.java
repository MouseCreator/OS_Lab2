package univ.lab.scheduling;

public class ScheduledProcess {
    private State state;
    private int fullElapsedTime;
    private int timeToComplete;
    private int nextBlockTime;
    private int nextWorkTime;
    private int currentPhaseTime;
    private TimeGenerator blockTimeGenerator;
    private TimeGenerator workTimeGenerator;
    private boolean toBoost;
    private int timeArrive;
    private final SPStats stats = new SPStats();
    private String name;
    public State getState() {
        return state;
    }

    public ScheduledProcess() {
        fullElapsedTime = 0;
        state = State.READY;
    }

    public void start() {
        state = State.RUNNING;
        nextWorkTime = getNextWorkTime();
        resetPhase();
    }

    private void block() {
        state = State.BLOCKED;
        nextBlockTime = getNextBlockTime();
        stats.addBlocked();
        resetPhase();
    }

    public void stop() {
        if (state != State.RUNNING) {
            throw new IllegalStateException("Process is not stopped while not running");
        }
        state = State.READY;
        resetPhase();
    }

    private void resetPhase() {
        currentPhaseTime = 0;
    }

    public void nextTick() {
        if (state != State.RUNNING) {
            throw new IllegalStateException("Process is not in running state, while next tick is called");
        }
        fullElapsedTime++;
        currentPhaseTime++;
        stats.addTotalTimeRunning(1);
        if (fullElapsedTime >= timeToComplete) {
            state = State.TERMINATED;
        }
        if (currentPhaseTime >= nextWorkTime) {
            block();
        }
    }

    public void applyTime(int timeElapsed) {
        if (state == State.READY) {
            return;
        }
        if (state != State.BLOCKED) {
            throw new IllegalStateException("Called apply time, when process is not blocked");
        }
        currentPhaseTime += timeElapsed;
        stats.addTotalTimeBlocked(timeElapsed);
        if (currentPhaseTime > nextBlockTime) {
            state = State.READY;
            resetPhase();
        }
    }

    public void setTimeToComplete(int timeToComplete) {
        this.timeToComplete = timeToComplete;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    enum State {
        RUNNING, BLOCKED, READY, TIMEOUT, TERMINATED
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

    public void setBlockTimeGenerator(TimeGenerator generator) {
        this.blockTimeGenerator = generator;
    }
    public void setWorkTimeGenerator(TimeGenerator generator) {
        this.workTimeGenerator = generator;
    }

    private int getNextBlockTime() {
        return blockTimeGenerator.generateTime();
    }
    private int getNextWorkTime() {
        return workTimeGenerator.generateTime();
    }

    public SPStats getStats() {
        return stats;
    }
}
