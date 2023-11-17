package univ.lab.scheduling;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class FCFSScheduler implements SimpleScheduler{
    private final List<RunningProcess> activeProcessList;
    private final int quantumNumber;
    private final int priority;
    public FCFSScheduler(int priority, int quantumNumber) {
        this.activeProcessList = new ArrayList<>();
        this.quantumNumber = quantumNumber;
        this.priority = priority;
    }

    @Override
    public Optional<RunningProcess> getNextProcess() {
        int size = activeProcessList.size();
        int i;
        for (i = 0; i < size; i++) {
            RunningProcess current = activeProcessList.get(0);
            if (current.isReady()) {
                break;
            }
        }
        if (i == size)
            return Optional.empty();
        return Optional.of(activeProcessList.remove(i));
    }

    @Override
    public void registerProcess(ScheduledProcess process) {
        activeProcessList.add(RunningProcess.fromScheduledProcess(this, process));
    }

    @Override
    public void enqueue(RunningProcess process) {
        provideQuantumIfNeeded(process);
        activeProcessList.add(0, process);
    }
    private void provideQuantumIfNeeded(RunningProcess process) {
        if (process.wasBlocked() || process.usedProvidedQuantum()) {
            process.provideQuantum(quantumNumber);
            process.setWasBlocked(false);
        }
    }

    @Override
    public int getMaxQuantum() {
        return quantumNumber;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public List<RunningProcess> eachProcess() {
        return activeProcessList;
    }

    @Override
    public List<RunningProcess> removeBoostable() {
        Iterator<RunningProcess> iterator = activeProcessList.iterator();
        List<RunningProcess> list = new ArrayList<>();
        while (iterator.hasNext()) {
            RunningProcess process = iterator.next();
            if (process.getScheduledProcess().isToBoost()) {
                list.add(process);
                iterator.remove();
            }
        }
        return list;
    }

    @Override
    public void enqueueFirst(RunningProcess process) {
        provideQuantumIfNeeded(process);
        activeProcessList.add(0, process);
    }
}
