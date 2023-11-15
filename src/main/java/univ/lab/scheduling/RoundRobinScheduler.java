package univ.lab.scheduling;

import java.util.ArrayList;
import java.util.List;

public class RoundRobinScheduler {
    private final List<ScheduledProcess> processList;
    private final List<ScheduledProcess> timeoutProcesses;
    private final int quantumNumber;
    private ScheduledProcess currentProcess;
    public RoundRobinScheduler(List<ScheduledProcess> list, int quantumNumber) {
        this.processList = list;
        this.quantumNumber = quantumNumber;
        timeoutProcesses = new ArrayList<>();
    }
    enum RRState {
        DONE, FREE, RUNNING
    }
    public RRState run() {
        if (currentProcess == null) {
            if (processList == null) {
                throw new IllegalStateException("Round-robin scheduler is not initialized with list of processes");
            }
            if (processList.isEmpty()) {
                if (timeoutProcesses.isEmpty()) {
                    return RRState.DONE;
                }
                processList.addAll(timeoutProcesses);
                timeoutProcesses.clear();
            }
            currentProcess = processList.remove(0);
        }
        currentProcess.nextTick();
        return RRState.FREE;
    }
}
