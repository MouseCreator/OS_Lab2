package univ.lab.scheduling;

import univ.lab.configuration.Configuration;
import univ.lab.configuration.SimulationProperties;

public class SchedulingAlgorithmFactory {
    public static SchedulingAlgorithm<ScheduledProcess> buildForQueues(Configuration configuration) {
        SimulationProperties properties = configuration.getProperties();
        QueueContainer queueContainer = QueueContainer.commonContainer();
        initContainer(queueContainer, properties);
        return new MultipleQueuesSchedulingAlgorithm(queueContainer);
    }

    private static void initContainer(QueueContainer queueContainer, SimulationProperties properties) {
        queueContainer.setQuantumDuration(properties.getQuantumDuration());
        queueContainer.setMaxTimeBreaks(properties.getBreaks());
    }
}
