package univ.lab.scheduling;

import univ.lab.ontko.Results;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

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
        return new ArrayList<>(processVector.stream().sorted(Comparator.comparingInt(ScheduledProcess::getTimeArrive)).toList());
    }

    private Results simulate(int runtime, List<ScheduledProcess> scheduledProcesses, Results result) {
        result.schedulingType = "Batch (Nonpreemptive)";
        result.schedulingName = "4 Queues";
        int computationTime = 0;
        try {
            computationTime = calculate(runtime, scheduledProcesses);
        } catch (IOException e) {
            e.printStackTrace();
        }
        result.compuTime = computationTime;
        return result;
    }

    private int calculate(int runtime, List<ScheduledProcess> scheduledProcesses) throws FileNotFoundException {
        String resultsFile = "src/main/resources/Summary-Processes.txt";
        PrintStream outStream = new PrintStream(new FileOutputStream(resultsFile));
        int computationTime;
        int boostFrequency = 500;
        int boostIterCounter = 0;
        ScheduledProcess process = null;
        ProcessRunner runner = new ProcessRunner();
        for (computationTime = 0; computationTime < runtime; computationTime++, boostIterCounter++) {
            int arrived = checkForArriveProcess(computationTime, scheduledProcesses, outStream);
            boostIterCounter = tryBoost(boostFrequency, boostIterCounter);
            if (process == null) {
                if (arrived > 0) {
                    process = startNextProcess(computationTime, runner);
                } else {
                    outStream.println("Idle...");
                    queueContainer.applyElapsedTime(1); //may be replaced with "find min value" and skip iterations
                    process = startNextProcess(computationTime, runner);
                    logProcessStart(outStream, process);
                    continue;
                }
            }
            ScheduledProcess.State currentProcessState = runner.nextTick();
            switch (currentProcessState) {
                case RUNNING, READY -> {
                    //continue
                }
                case BLOCKED -> {
                    addElapsedTime(computationTime, runner);
                    queueContainer.enqueueAndModify(process, outStream);
                    process = startNextProcess(computationTime, runner);
                    logProcessStart(outStream, process);
                }
                case TERMINATED -> {
                    addElapsedTime(computationTime, runner);
                    outStream.println(process.getName() + " terminated");
                    process = startNextProcess(computationTime, runner);
                    logProcessStart(outStream, process);
                }
            }
        }
        outStream.close();
        return computationTime;
    }

    private static void logProcessStart(PrintStream outStream, ScheduledProcess process) {
        outStream.println(process.getName() + " started with quantum " + process.getMaxQuantum());
    }

    private ScheduledProcess startNextProcess(int computationTime, ProcessRunner runner) {
        ScheduledProcess process;
        process = getNext();
        if (process != null) {
            runner.startProcess(process, computationTime);
        }
        return process;
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

    private void addElapsedTime(int computationTime, ProcessRunner runner) {
        int timeElapsed = computationTime - runner.timeStart();
        queueContainer.applyElapsedTime(timeElapsed);
    }

    private void registerProcess(ScheduledProcess process, PrintStream out) {
        queueContainer.enqueue(process);
        out.println("Registered process: " + process.getName());
        //out prints info
    }


}
