package univ.lab.scheduling;

import univ.lab.ontko.Results;
import univ.lab.ontko.SProcess;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

public class MultipleQueuesSchedulingAlgorithm implements SchedulingAlgorithm{
    private final QueueContainer queueContainer;
    public MultipleQueuesSchedulingAlgorithm(QueueContainer queueContainer) {
        this.queueContainer = queueContainer;
    }
    @Override
    public Results run(int runtime, Vector<SProcess> processVector, Results result) {
        Vector<ScheduledProcess> scheduledProcesses = transformVector(processVector);
        return simulate(runtime, scheduledProcesses, result);
    }

    private Results simulate(int runtime, Vector<ScheduledProcess> scheduledProcesses, Results result) {
        int currentProcess = 0;
        int previousProcess;
        int size = scheduledProcesses.size();
        int completed = 0;
        int comptime = 0;
        String resultsFile = "src/main/resources/Summary-Processes.txt";
        result.schedulingType = "Batch (Nonpreemptive)";
        result.schedulingName = "4 Queues";
        try {
            PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
            ScheduledProcess process = registerProcess(scheduledProcesses, currentProcess, out);
            ProcessRunner runner = ProcessRunner.runProcess(process, 0);
            for (comptime = 0; comptime < runtime; comptime++) {
                ScheduledProcess.State currentProcessState = runner.run();
                switch (currentProcessState) {
                    case RUNNING -> { continue; }
                    case BLOCKED -> {
                        int timeElapsed = comptime - runner.timeStart();
                        queueContainer.applyElapsedTime(timeElapsed);
                    }
                    case TERMINATED -> {

                    }
                }
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        result.compuTime = comptime;
        return result;
    }

    private ScheduledProcess registerProcess(Vector<ScheduledProcess> processVector, int currentProcess, PrintStream out) {
        ScheduledProcess process = processVector.elementAt(currentProcess);
        /*out.println("Process: " + currentProcess + " registered... (" + process.cpuTime + " " + process.ioBlocking +
                " " + process.cpuDone + " " + process.cpuDone + ")");
        */
        return process;
    }
    private Vector<ScheduledProcess> transformVector(Vector<SProcess> processVector) {
        throw new UnsupportedOperationException();
    }
}
