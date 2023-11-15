package univ.lab.scheduling;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoundRobinScheduler {
    private final List<RunningProcess> activeProcessList;
    private final List<RunningProcess> timeoutProcesses;
    private final int quantumNumber;
    private final int priority;
    public RoundRobinScheduler(int priority, int quantumNumber) {
        this.activeProcessList = new ArrayList<>();
        this.quantumNumber = quantumNumber;
        this.priority = priority;
        timeoutProcesses = new ArrayList<>();
    }
    public Optional<RunningProcess> getNextProcess() {
        if (activeProcessList.isEmpty()) {
            swapLists();
        }
        for (RunningProcess process : activeProcessList) {
            if (process.getScheduledProcess().getState()== ScheduledProcess.State.READY) {
                return Optional.of(process);
            }
        }
        return Optional.empty();
    }

    private void swapLists() {
        activeProcessList.addAll(timeoutProcesses);
        timeoutProcesses.clear();
    }

    public void registerProcess(ScheduledProcess process) {
        RunningProcess runningProcess = RunningProcess.fromScheduledProcess(this, process);
        this.activeProcessList.add(runningProcess);
    }

    public void enqueue(RunningProcess process) {
        if (process.isBlocked()) {
            putInExtraQueue(process);
            return;
        }
        if (process.usedProvidedQuantum()) {
            putInExtraQueue(process);
            return;
        }
        activeProcessList.add(process);
    }

    private void putInExtraQueue(RunningProcess process) {
        process.provideQuantum(quantumNumber);
        timeoutProcesses.add(process);
    }

    public int getMaxQuantum() {
        return quantumNumber;
    }

    public int getPriority() {
        return priority;
    }

    public List<RunningProcess> eachProcess() {
        List<RunningProcess> runningProcesses = new ArrayList<>();
        runningProcesses.addAll(activeProcessList);
        runningProcesses.addAll(timeoutProcesses);
        return runningProcesses;
    }

    public void remove(RunningProcess process) {
        activeProcessList.remove(process);
        timeoutProcesses.remove(process);
    }
}
