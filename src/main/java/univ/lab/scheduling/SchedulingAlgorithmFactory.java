package univ.lab.scheduling;

import univ.lab.configuration.Configuration;
import univ.lab.configuration.SimulationProperties;


public class SchedulingAlgorithmFactory {
    public static MultipleQueuesSchedulingAlgorithm buildForQueues(Configuration configuration) {
        SimulationProperties properties = configuration.getProperties();
        QueueContainer queueContainer = QueueContainer.commonContainer(properties.getBreaks());
        MultipleQueuesSchedulingAlgorithm algorithm = new MultipleQueuesSchedulingAlgorithm(queueContainer);
        algorithm.setRuntime(properties.getRuntime());
        algorithm.setBoostFrequency(properties.getBoostFrequency());
        algorithm.setQuantumDuration(properties.getQuantumDuration());
        return algorithm;
    }


}
