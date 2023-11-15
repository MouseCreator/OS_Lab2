package univ.lab.scheduling;

public class ScheduledProcess {
    private State state;
    private int timesBreak;
    private int currentPriority;
    private int quantumDuration;
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
    int timeouts = 0;
    public State getState() {
        return state;
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
            timeouts = 0;
            resetPhase();
        }
        else if (currentPhaseTimeElapsed >= nextBlockTime) {
            state = State.BLOCKED;
            timeBlocking = timeToBlockGenerator.generateTime();
            setCurrentQuantum(currentPhaseTimeElapsed);
            timeouts = 0;
            resetPhase();
        }
        else if (currentPhaseTimeElapsed >= quantumDuration) {
            state = State.TIMEOUT;
            timeouts++;
            setCurrentQuantum(currentPhaseTimeElapsed);
            resetPhase();
        }
    }

    public int getTimeouts() {
        return timeouts;
    }
    public void resetTimeouts() {
        timeouts = 0;
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

    public void setState(State newState) {
        state = newState;
    }

    enum State {
        RUNNING, BLOCKED, READY, TERMINATED, TIMEOUT
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

    public int getQuantumDuration() {
        return quantumDuration;
    }

    public void setQuantumDuration(int quantumDuration) {
        this.quantumDuration = quantumDuration;
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
