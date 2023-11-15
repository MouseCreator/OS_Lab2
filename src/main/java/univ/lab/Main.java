package univ.lab;

import univ.lab.configuration.Configuration;
import univ.lab.configuration.ConfigurationManager;
import univ.lab.configuration.SimulationProperties;
import univ.lab.ontko.Results;
import univ.lab.scheduling.*;

public class Main {
    /*
    TODO:
    1. Round-Robin algorithm for Queues
    2. Extra task list
    3. Tick or millis?
     */
    public static void main(String[] args) {
        String file = getFile(args);

        ConfigurationManager configurationManager = new ConfigurationManager();
        Configuration configuration = configurationManager.parseInput(file);
        SimulationProperties properties = configuration.getProperties();

        SchedulingAlgorithm<ScheduledProcess> schedulingAlgorithm = SchedulingAlgorithmFactory.buildForQueues(configuration);
        Results results = new Results("null","null",0);
        schedulingAlgorithm.run(properties.getRuntime(), configuration.getScheduledProcesses(), results);
    }

    private static String getFile(String[] args) {
        if (args.length == 0) {
            return "src/main/resources/xml/queues.xml";
        }
        if (args.length == 1) {
            return args[0];
        }
        throw new IllegalArgumentException("Expected 0 or 1 arguments!");
    }
}