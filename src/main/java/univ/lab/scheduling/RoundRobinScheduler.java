package univ.lab.scheduling;

import java.util.ArrayList;
import java.util.Iterator;
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
        int size = activeProcessList.size();
        if (size==0) {
            return Optional.empty();
        }
        for (int i = 0; i < size; i++) {
            RunningProcess first = activeProcessList.remove(0);
            if (first.isReady()) {
                return Optional.of(first);
            } else {
                activeProcessList.add(first);
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

    public List<RunningProcess> removeBoostable() {
        List<RunningProcess> removed = new ArrayList<>();
        removeBoostableFromList(activeProcessList, removed);
        removeBoostableFromList(timeoutProcesses, removed);
        return removed;
    }

    private void removeBoostableFromList(List<RunningProcess> target, List<RunningProcess> removed) {
        Iterator<RunningProcess> iterator = target.iterator();
        while (iterator.hasNext()) {
            RunningProcess process = iterator.next();
            if (process.getScheduledProcess().isToBoost()) {
                removed.add(process);
                iterator.remove();
            }
        }
    }

    public void enqueueFirst(RunningProcess process) {
        process.resetBreaks();
        process.provideQuantum(quantumNumber);
        process.setCurrentPriority(priority);
        activeProcessList.add(0, process);
    }
}
