package univ.lab.configuration;

import univ.lab.scheduling.ScheduledProcess;

import java.util.Vector;

public class Configuration {
    private final SimulationProperties properties;

    private final Vector<ScheduledProcess> scheduledProcesses;

    public Configuration(SimulationProperties properties, Vector<ScheduledProcess> scheduledProcesses) {
        this.properties = properties;
        this.scheduledProcesses = scheduledProcesses;
    }

    public SimulationProperties getProperties() {
        return properties;
    }

    public Vector<ScheduledProcess> getScheduledProcesses() {
        return scheduledProcesses;
    }
}
