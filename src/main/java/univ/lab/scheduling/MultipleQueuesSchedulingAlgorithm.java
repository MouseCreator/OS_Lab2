package univ.lab.scheduling;

import univ.lab.ontko.Results;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.Optional;
import java.util.Vector;
import java.util.List;

public class MultipleQueuesSchedulingAlgorithm implements SchedulingAlgorithm<ScheduledProcess>{
    private final QueueContainer queueContainer;
    public MultipleQueuesSchedulingAlgorithm(QueueContainer queueContainer) {
        this.queueContainer = queueContainer;
    }
    @Override
    public Results run(int runtime, Vector<ScheduledProcess> processVector, Results result) {
        List<ScheduledProcess> processList = toSortedList(processVector);
        return simulate(runtime, processList, result);
    }

    private List<ScheduledProcess> toSortedList(Vector<ScheduledProcess> processVector) {
        return processVector.stream().sorted(Comparator.comparingInt(ScheduledProcess::getTimeArrive)).toList();
    }

    private Results simulate(int runtime, List<ScheduledProcess> scheduledProcesses, Results result) {
        int computationTime = 0;
        int boostFrequency = 500;
        int boostIterCounter = 0;
        String resultsFile = "src/main/resources/Summary-Processes.txt";
        result.schedulingType = "Batch (Nonpreemptive)";
        result.schedulingName = "4 Queues";
        ProcessRunner runner = new ProcessRunner();
        try {
            PrintStream outStream = new PrintStream(new FileOutputStream(resultsFile));
            ScheduledProcess process = null;
            for (computationTime = 0; computationTime < runtime; computationTime++, boostIterCounter++) {
                int arrived = checkForArriveProcess(computationTime, scheduledProcesses, outStream);
                boostIterCounter = tryBoost(boostFrequency, boostIterCounter);
                if (process == null) {
                    if (arrived > 0) {
                        process = getNext();
                    } else {
                        outStream.println("Idle...");
                        queueContainer.applyElapsedTime(1); //may be replaced with "find min value" and skip iterations
                        continue;
                    }
                }
                ScheduledProcess.State currentProcessState = runner.nextTick();
                switch (currentProcessState) {
                    case RUNNING -> {
                        //continue
                    }
                    case BLOCKED -> {
                        addElapsedTime(computationTime, runner);
                        queueContainer.enqueueAndModify(process);
                        process = getNext();
                    }
                    case TERMINATED -> {
                        addElapsedTime(computationTime, runner);
                        process = getNext();
                    }
                }
            }
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        result.compuTime = computationTime;
        return result;
    }

    private int checkForArriveProcess(int computationTime, List<ScheduledProcess> scheduledProcesses,
                                       PrintStream out) {
        int arrived = 0;
        while (!scheduledProcesses.isEmpty() && scheduledProcesses.get(0).getTimeArrive()==computationTime) {
            registerProcess(scheduledProcesses.remove(0), out);
            arrived++;
        }
        return arrived;
    }

    private int tryBoost(int boostFrequency, int boostIterCounter) {
        if (boostIterCounter >= boostFrequency) {
            boostIterCounter = 0;
            boost();
        }
        return boostIterCounter;
    }

    private void boost() {
        queueContainer.applyBoost();
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

    private void registerProcess(ScheduledProcess process, PrintStream out) {
        queueContainer.enqueue(process);
        out.println("Registered process: " + process.getName());
        //out prints info
    }


}
