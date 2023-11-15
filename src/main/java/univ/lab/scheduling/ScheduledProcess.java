package univ.lab.scheduling;

public class ScheduledProcess {
    private State state;
    private int fullElapsedTime;
    private int timeToComplete;
    private TimeGenerator blockTimeGenerator;
    private TimeGenerator workTimeGenerator;
    private boolean toBoost;
    private int timeArrive;
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
    }

    public void nextTick() {
        if (state != State.RUNNING) {
            throw new IllegalStateException("Process is not in running state, while next tick is called");
        }
        fullElapsedTime++;
        if (fullElapsedTime >= timeToComplete) {
            state = State.TERMINATED;
        }
    }

    public void applyTime(int timeElapsed) {

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

    public void setState(State newState) {
        state = newState;
    }

    enum State {
        RUNNING, BLOCKED, READY, TERMINATED
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

    public int getNextBlockTime() {
        return blockTimeGenerator.generateTime();
    }
    public int getNextWorkTime() {
        return workTimeGenerator.generateTime();
    }
}
