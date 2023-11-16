package univ.lab;

import univ.lab.configuration.Configuration;
import univ.lab.configuration.ConfigurationManager;
import univ.lab.lib.filemanager.FileManager;
import univ.lab.scheduling.*;

public class Main {
    /*
    TODO: Add special queue for boosted processes?
     */
    public static void main(String[] args) {
        String file = getFile(args);

        ConfigurationManager configurationManager = new ConfigurationManager();
        Configuration configuration = configurationManager.parseInput(file);

        MultipleQueuesSchedulingAlgorithm schedulingAlgorithm = SchedulingAlgorithmFactory.buildForQueues(configuration);
        CustomResults results = schedulingAlgorithm.run(configuration.getScheduledProcesses());
        results.setTitle("4 Queues");
        results.setProperties(configuration.getProperties());
        writeResults(results);
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
    private static void writeResults(CustomResults results) {
        String resultStr = results.toResultString();
        String filename = "src/main/resources/Summary-Results.txt";
        FileManager fileManager = new FileManager();
        fileManager.write(filename, resultStr);
    }
}