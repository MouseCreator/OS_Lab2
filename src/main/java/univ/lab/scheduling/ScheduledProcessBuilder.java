package univ.lab.scheduling;

public class ScheduledProcessBuilder {
    private final ScheduledProcess process;

    public ScheduledProcessBuilder() {
        this.process = new ScheduledProcess();
    }

    public ScheduledProcess get() {
        return process;
    }

    public ScheduledProcessBuilder withTimeToComplete(int fullTime) {
        process.setTimeToComplete(fullTime);
        return this;
    }
    public ScheduledProcessBuilder withWorkingTime(int average, int deviation) {
        process.setTimeToWorkGenerator(new BlockTimeGenerator(average, deviation));
        return this;
    }
    public ScheduledProcessBuilder withBlockingTime(int average, int deviation) {
        process.setTimeToBlockGenerator(new BlockTimeGenerator(average, deviation));
        return this;
    }

    public ScheduledProcessBuilder boost(boolean toBoost) {
        process.setToBoost(toBoost);
        return this;
    }

    public ScheduledProcessBuilder withTimeArrive(int timeArrive) {
        process.setTimeArrive(timeArrive);
        return this;
    }

    public ScheduledProcessBuilder withName(String name) {
        process.setName(name);
        return this;
    }
}
