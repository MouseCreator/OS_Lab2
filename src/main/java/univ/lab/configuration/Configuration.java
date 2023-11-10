package univ.lab.configuration;

import univ.lab.scheduling.ScheduledProcess;

import java.util.Vector;

public class Configuration {
    private SimulationProperties properties;
    private Vector<ScheduledProcess> scheduledProcesses;

    public Configuration(SimulationProperties properties, Vector<ScheduledProcess> scheduledProcesses) {
        this.properties = properties;
        this.scheduledProcesses = scheduledProcesses;
    }

    public void setProperties(SimulationProperties properties) {
        this.properties = properties;
    }

    public void setScheduledProcesses(Vector<ScheduledProcess> scheduledProcesses) {
        this.scheduledProcesses = scheduledProcesses;
    }
}
