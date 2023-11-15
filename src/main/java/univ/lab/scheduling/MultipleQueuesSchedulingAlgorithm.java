package univ.lab.scheduling;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class MultipleQueuesSchedulingAlgorithm {
    private final QueueContainer queueContainer;
    private int quantumDuration = 20;
    private int boostFrequency = 500;

    public MultipleQueuesSchedulingAlgorithm(QueueContainer queueContainer) {
        this.queueContainer = queueContainer;
    }

    public CustomResults run(Vector<ScheduledProcess> processVector) {
        List<ScheduledProcess> processList = toSortedList(processVector);
        return simulate(processList);
    }

    private List<ScheduledProcess> toSortedList(Vector<ScheduledProcess> processVector) {
        return new ArrayList<>(processVector.stream().sorted(Comparator.comparingInt(ScheduledProcess::getTimeArrive)).toList());
    }

    private CustomResults simulate(List<ScheduledProcess> scheduledProcesses) {
        try {
             return calculate(scheduledProcesses);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private CustomResults calculate(List<ScheduledProcess> scheduledProcesses) throws FileNotFoundException {
        String resultsFile = "src/main/resources/Summary-Processes.txt";
        PrintStream outStream = new PrintStream(new FileOutputStream(resultsFile));
        CustomResults results = new CustomResults();
        int computationTime;
        int lastBoost = 0;
        RunningProcess process = null;
        results.setProcessList(new ArrayList<>(scheduledProcesses));
        ProcessManager processManager = new ProcessManager();
        processManager.setQuantumDuration(quantumDuration);
        for (computationTime = 0; computationTime < runtime; computationTime++) {
            int arrived = checkForArriveProcess(computationTime, scheduledProcesses, outStream);
            if (process == null) {
                if (arrived > 0) {
                    process = startNextProcess(processManager, outStream);
                } else {
                    if (scheduledProcesses.isEmpty())
                        break;
                    outStream.println("Idle...");
                    queueContainer.applyElapsedTime(1);
                    process = startNextProcess(processManager, outStream);
                    continue;
                }
            }
            ScheduledProcess.State currentProcessState = processManager.run(outStream);
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
                    lastBoost = tryBoost(computationTime, boostFrequency, lastBoost, outStream);
                    process = startNextProcess(processManager, outStream);
                }
                case TERMINATED -> {
                    addElapsedTime(processManager);
                    lastBoost = tryBoost(computationTime, boostFrequency, lastBoost, outStream);
                    process = startNextProcess(processManager, outStream);
                }
            }
        }
        outStream.close();
        results.setCalculationTime(computationTime);
        return results;
    }

    private RunningProcess startNextProcess(ProcessManager manager, PrintStream outStream) {
        RunningProcess process = getNext();
        if (process != null) {
            manager.startProcess(process, outStream);
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

    private int tryBoost(int computation, int boostFrequency, int lastBoost, PrintStream outSteam) {
        if (computation - lastBoost >= boostFrequency) {
            lastBoost = computation;
            queueContainer.boost(outSteam);
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

    private int runtime;

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public void setQuantumDuration(int quantumDuration) {
        this.quantumDuration = quantumDuration;
    }

    public void setBoostFrequency(int boostFrequency) {
        this.boostFrequency = boostFrequency;
    }


}
