package univ.lab.configuration;

import univ.lab.lib.fill.FillableCreator;
import univ.lab.lib.fill.Filler;
import univ.lab.lib.fill.FillerImpl;
import univ.lab.lib.parser.StAXParser;
import univ.lab.scheduling.ScheduledProcess;
import univ.lab.scheduling.ScheduledProcessBuilder;

import java.util.Vector;

public class ConfigurationManager {
    public Configuration parseInput(String xmlFilename) {
        Filler filler = new FillerImpl();
        FillableCreator creator = initFillableCreator();
        StAXParser STAXParser = new StAXParser(creator, filler);
        Processes processes = (Processes) STAXParser.parse(xmlFilename);
        return new Configuration(processes.getSimulationProperties(), toProcessVector(processes));
    }

    private Vector<ScheduledProcess> toProcessVector(Processes processes) {
        Vector<ScheduledProcess> vector = new Vector<>();
        for (ProcessInfo info : processes.getProcessInfoList()) {
            ScheduledProcess process = toScheduledProcess(info);
            vector.add(process);
        }
        return vector;
    }

    private ScheduledProcess toScheduledProcess(ProcessInfo info) {
        ScheduledProcessBuilder builder = new ScheduledProcessBuilder();
        builder.withTimeToComplete(info.getRuntime())
                .withTimeArrive(info.getArrive())
                .withBlockingTime(info.getAverageBlockTime(), info.getDeviationBlockTime())
                .withWorkingTime(info.getAverageWorkTime(), info.getDeviationRunTime())
                .withName(info.getName());
        return builder.get();
    }

    private FillableCreator initFillableCreator() {
        FillableCreator creator = new FillableCreator();
        creator.add(Processes.class);
        creator.add(ProcessInfo.class);
        creator.add(SimulationProperties.class);
        return creator;
    }
}
