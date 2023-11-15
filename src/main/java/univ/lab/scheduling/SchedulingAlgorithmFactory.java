package univ.lab.scheduling;

import univ.lab.configuration.Configuration;
import univ.lab.configuration.SimulationProperties;

public class SchedulingAlgorithmFactory {
    public static SchedulingAlgorithm<ScheduledProcess> buildForQueues(Configuration configuration) {
        SimulationProperties properties = configuration.getProperties();
        QueueContainer queueContainer = QueueContainer.commonContainer();
        return new MultipleQueuesSchedulingAlgorithm(queueContainer);
    }


}
