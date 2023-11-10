package univ.lab.scheduling;

public class ScheduledProcessFactory {
    private final ScheduledProcess process;

    public ScheduledProcessFactory() {
        this.process = new ScheduledProcess();
    }

    public ScheduledProcess get() {
        return process;
    }

    public ScheduledProcessFactory withTimeToComplete(int fullTime) {
        process.setTimeToComplete(fullTime);
        return this;
    }
    public ScheduledProcessFactory withWorkingTime(int average, int deviation) {
        process.setTimeToWorkGenerator(new BlockTimeGenerator(average, deviation));
        return this;
    }
    public ScheduledProcessFactory withBlockingTime(int average, int deviation) {
        process.setTimeToBlockGenerator(new BlockTimeGenerator(average, deviation));
        return this;
    }
}
