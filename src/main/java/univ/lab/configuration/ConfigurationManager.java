package univ.lab.configuration;

import org.xml.sax.SAXException;
import univ.lab.lib.fill.FillableCreator;
import univ.lab.lib.fill.Filler;
import univ.lab.lib.fill.FillerImpl;
import univ.lab.lib.parser.StAXParser;
import univ.lab.lib.validator.XSDValidator;
import univ.lab.scheduling.ScheduledProcess;
import univ.lab.scheduling.ScheduledProcessBuilder;

import java.io.IOException;
import java.util.Vector;

public class ConfigurationManager {
    private static final String xsdFile = "src/main/resources/xml/queues.xsd";
    public Configuration parseInput(String xmlFilename) {
        validate(xmlFilename);
        StAXParser STAXParser = initParser();
        Processes processes = (Processes) STAXParser.parse(xmlFilename);
        return new Configuration(processes.getSimulationProperties(), toProcessVector(processes));
    }

    private StAXParser initParser() {
        Filler filler = new FillerImpl();
        FillableCreator creator = initFillableCreator();
        return new StAXParser(creator, filler);
    }

    private static void validate(String xmlFilename) {
        XSDValidator validator = new XSDValidator();
        try {
            validator.validate(xsdFile, xmlFilename);
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }
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
        int[] block = parseMeanDeviationFormat(info.getBlockTime());
        int[] work = parseMeanDeviationFormat(info.getWorkTime());
        ScheduledProcessBuilder builder = new ScheduledProcessBuilder();
        builder.withTimeToComplete(info.getRuntime())
                .withTimeArrive(info.getArrive())
                .withBlockingTime(block[0], block[1])
                .withWorkingTime(work[0], work[1])
                .withName(info.getName())
                .boost(info.getBoost());
        return builder.get();
    }

    private int[] parseMeanDeviationFormat(String input) {
        input = input.replace(" ", "");
        if (input.contains("+")) {
            String[] tokens = input.split("\\+"); //as min-max value
            int[] result = new int[tokens.length];
            assert tokens.length == 2;
            int a = Integer.parseInt(tokens[0]);
            int b = Integer.parseInt(tokens[1]);
            result[0] = (a+b) >>> 1;
            result[1] = (b-a) >>> 1;
            return result;
        } else if (input.contains("/")) {
            String[] tokens = input.split("/"); //as mean and deviation
            int[] result = new int[tokens.length];
            assert tokens.length == 2;
            for (int i = 0; i < tokens.length; i++) {
                result[i] = Integer.parseInt(tokens[i]);
            }
            return result;
        } else {
            int[] result = new int[2];
            result[0] = Integer.parseInt(input);
            return result;
        }
    }

    private FillableCreator initFillableCreator() {
        FillableCreator creator = new FillableCreator();
        creator.add(Processes.class);
        creator.add(ProcessInfo.class);
        creator.add(SimulationProperties.class);
        return creator;
    }
}
