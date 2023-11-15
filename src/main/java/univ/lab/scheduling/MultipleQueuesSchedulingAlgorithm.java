package univ.lab.scheduling;

import univ.lab.ontko.Results;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class MultipleQueuesSchedulingAlgorithm implements SchedulingAlgorithm<ScheduledProcess>{
    private final QueueContainer queueContainer;
    private final int quantumDuration = 20;
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
        RunningProcess process = null;
        ProcessManager processManager = new ProcessManager();
        processManager.setQuantumDuration(quantumDuration);
        for (computationTime = 0; computationTime < runtime; computationTime++) {
            int arrived = checkForArriveProcess(computationTime, scheduledProcesses, outStream);
            if (processManager.isEmpty()) {
                if (arrived > 0) {
                    process = startNextProcess(processManager);
                } else {
                    outStream.println("Idle...");
                    queueContainer.applyElapsedTime(1);
                    process = startNextProcess(processManager);
                    logProcessStart(outStream, process);
                    continue;
                }
            }
            ScheduledProcess.State currentProcessState = processManager.run();
            switch (currentProcessState) {
                case RUNNING, READY -> {
                    //continue
                }
                case BLOCKED, TIMEOUT -> {
                    addElapsedTime(processManager);
                    if (process == null) {
                        throw new NullPointerException("Trying to enqueue null process");
                    }
                    queueContainer.enqueue(process);
                    boostIterCounter = tryBoost(computationTime, boostFrequency, boostIterCounter);
                    process = startNextProcess(processManager);
                }
                case TERMINATED -> {
                    addElapsedTime(processManager);
                    boostIterCounter = tryBoost(computationTime, boostFrequency, boostIterCounter);
                    process = startNextProcess(processManager);
                }
            }
        }
        outStream.close();
        return computationTime;
    }

    private static void logProcessStart(PrintStream outStream, RunningProcess process) {
        outStream.println(process.getScheduledProcess().getName() + " started");
    }

    private RunningProcess startNextProcess(ProcessManager manager) {
        RunningProcess process;
        process = getNext();
        if (process != null) {
            manager.startProcess(process);
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

    private int tryBoost(int computation, int boostFrequency, int lastBoost) {
        if (computation - lastBoost >= boostFrequency) {
            lastBoost = computation;
            queueContainer.boost();
        }
        return lastBoost;
    }


    private RunningProcess getNext() {
        RunningProcess process;
        Optional<RunningProcess> dequeue = queueContainer.dequeue();
        process = dequeue.orElse(null);
        return process;
    }

    private void addElapsedTime(ProcessManager manager) {
        int timeElapsed = manager.getRuntime();
        queueContainer.applyElapsedTime(timeElapsed);
    }

    private void registerProcess(ScheduledProcess process, PrintStream out) {
        queueContainer.register(process);
        out.println("Registered process: " + process.getName());
    }


}
