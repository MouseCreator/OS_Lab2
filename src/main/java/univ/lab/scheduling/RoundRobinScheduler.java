package univ.lab.scheduling;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Optional<ScheduledProcess> getNextProcess() {
        for (ScheduledProcess process : processList) {
            if (process.getState()== ScheduledProcess.State.READY) {
                return Optional.of(process);
            }
        }
        return Optional.empty();
    }

    public void addProcess(ScheduledProcess process) {
        this.processList.add(process);
    }

    public void addAndModifyProcess(ScheduledProcess process) {
        //process.incrementQuantumNumberUsed
        // if process used max quantum number
            //add process to extra queue
        // else
            // add process to current queue
    }
}
