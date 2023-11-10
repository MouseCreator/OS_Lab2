package univ.lab.scheduling;

import univ.lab.ontko.Results;
import univ.lab.ontko.SProcess;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Optional;
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
        int comptime = 0;
        String resultsFile = "src/main/resources/Summary-Processes.txt";
        result.schedulingType = "Batch (Nonpreemptive)";
        result.schedulingName = "4 Queues";
        ProcessRunner runner = new ProcessRunner();
        try {
            PrintStream outStream = new PrintStream(new FileOutputStream(resultsFile));
            ScheduledProcess process = registerProcess(scheduledProcesses, currentProcess, outStream);
            runner.startProcess(process, 0);
            for (comptime = 0; comptime < runtime; comptime++) {
                if (process == null) {
                    outStream.println("Idle...");
                    queueContainer.applyElapsedTime(1); //may be replaced with "find min value"
                    continue;
                }
                ScheduledProcess.State currentProcessState = runner.nextTick();
                switch (currentProcessState) {
                    case RUNNING -> {
                        //continue
                    }
                    case BLOCKED -> {
                        addElapsedTime(comptime, runner);
                        queueContainer.enqueueAndModify(process);
                        process = getNext();
                    }
                    case TERMINATED -> {
                        addElapsedTime(comptime, runner);
                        process = getNext();
                    }
                }
            }
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        result.compuTime = comptime;
        return result;
    }

    private ScheduledProcess getNext() {
        ScheduledProcess process;
        Optional<ScheduledProcess> dequeue = queueContainer.dequeue();
        process = dequeue.orElse(null);
        return process;
    }

    private void addElapsedTime(int comptime, ProcessRunner runner) {
        int timeElapsed = comptime - runner.timeStart();
        queueContainer.applyElapsedTime(timeElapsed);
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
